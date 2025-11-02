package com.poulastaa.kyoku.gateway.config

import com.netflix.discovery.EurekaClient
import io.grpc.Attributes
import io.grpc.EquivalentAddressGroup
import io.grpc.NameResolver
import io.grpc.NameResolverProvider
import io.grpc.Status
import org.springframework.stereotype.Component
import java.net.InetSocketAddress
import java.net.URI
import io.grpc.NameResolverRegistry
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcConfig(
    private val eurekaNameResolverProvider: EurekaNameResolverProvider,
) {
    @PostConstruct
    fun registerNameResolver() {
        NameResolverRegistry.getDefaultRegistry()
            .register(eurekaNameResolverProvider)
    }
}

@Component
class EurekaNameResolverProvider(
    private val eurekaClient: EurekaClient,
) : NameResolverProvider() {
    override fun getDefaultScheme(): String = "eureka"
    override fun isAvailable(): Boolean = true
    override fun priority(): Int = 5
    override fun newNameResolver(
        targetUri: URI,
        args: NameResolver.Args,
    ): NameResolver = EurekaNameResolver(targetUri.authority, eurekaClient)
}

class EurekaNameResolver(
    private val serviceName: String,
    private val eurekaClient: EurekaClient,
) : NameResolver() {
    private var listener: Listener2? = null

    override fun getServiceAuthority(): String = serviceName

    override fun start(listener: Listener2) {
        this.listener = listener
        resolve()
    }

    override fun refresh() {
        resolve()
    }

    override fun shutdown() {
        listener = null
    }

    private fun resolve() {
        try {
            val instances = eurekaClient.getApplication(serviceName.uppercase())?.instances

            if (instances.isNullOrEmpty()) {
                listener?.onError(
                    Status.UNAVAILABLE
                        .withDescription("No instances available for service: $serviceName")
                )
                return
            }

            val addresses = instances.mapNotNull { instance ->
                val grpcPort = instance.metadata["gRpcPort"]?.toIntOrNull()
                    ?: instance.metadata["grpc.port"]?.toIntOrNull()
                    ?: return@mapNotNull null

                val address = InetSocketAddress(instance.ipAddr, grpcPort)
                EquivalentAddressGroup(address)
            }

            if (addresses.isEmpty()) {
                listener?.onError(
                    Status.UNAVAILABLE
                        .withDescription("No valid gRPC endpoints found for service: $serviceName")
                )
                return
            }

            listener?.onResult(
                ResolutionResult.newBuilder()
                    .setAddresses(addresses)
                    .setAttributes(Attributes.EMPTY)
                    .build()
            )
        } catch (e: Exception) {
            listener?.onError(
                Status.UNAVAILABLE
                    .withDescription("Failed to resolve service: $serviceName")
                    .withCause(e)
            )
        }
    }
}