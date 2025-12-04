package com.poulastaa.kyoku.validator.service

import com.poulastaa.kyoku.grpc.validation.*
import com.poulastaa.kyoku.validator.model.dto.UserType
import com.poulastaa.kyoku.validator.model.dto.ValidatorStatus
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import com.poulastaa.kyoku.grpc.validation.UserType as GRPCUserType

@GrpcService
class GRPCRequestValidatorService(
    private val jwt: JWTService,
) : ValidationServiceGrpc.ValidationServiceImplBase() {
    override fun validateAccessToken(
        request: ValidationRequest,
        responseObserver: StreamObserver<ValidationResponse>,
    ) {
        val payload = jwt.verifyAccessToken(request.token)

        responseObserver.onNext(
            ValidationResponse.newBuilder()
                .setStatus(
                    when (payload.first) {
                        ValidatorStatus.SUCCESS -> ResponseStatus.SUCCESS
                        ValidatorStatus.TOKEN_EXPIRED -> ResponseStatus.TOKEN_EXPIRED
                        ValidatorStatus.TOKEN_INVALID -> ResponseStatus.ERROR
                    }
                ).apply {
                    payload.second?.let { user ->
                        this.payload = Payload.newBuilder().apply {
                            email = user.email
                            type = when (user.userType) {
                                UserType.EMAIL -> GRPCUserType.EMAIL
                                UserType.GOOGLE -> GRPCUserType.GOOGLE
                                UserType.DEFAULT -> GRPCUserType.DEFAULT
                            }
                        }.build()
                    }
                }
                .build()
        )
        responseObserver.onCompleted()
    }
}