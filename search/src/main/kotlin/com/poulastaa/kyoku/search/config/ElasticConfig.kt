package com.poulastaa.kyoku.search.config

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient
import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import com.fasterxml.jackson.databind.ObjectMapper
import com.poulastaa.kyoku.search.domain.model.dto.DtoElasticSearchConfigInfo
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.impl.client.BasicCredentialsProvider
import org.elasticsearch.client.RestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ElasticConfig {
    @Bean
    fun provideElasticSearchClientConfiguration(
        @Value("\${elasticsearch.artist.host}") host: String,
        @Value("\${elasticsearch.artist.port}") port: Int,
        @Value("\${elasticsearch.artist.username}") username: String,
        @Value("\${elasticsearch.artist.password}") password: String,
        @Value("\${elasticsearch.artist.scheme}") schema: String,
    ) = DtoElasticSearchConfigInfo(
        host = host,
        port = port,
        username = username,
        password = password,
        schema = schema
    )

    @Bean
    fun provideRestClient(config: DtoElasticSearchConfigInfo): RestClient {
        val credentialsProvider = BasicCredentialsProvider().apply {
            setCredentials(
                AuthScope.ANY,
                UsernamePasswordCredentials(
                    config.username,
                    config.password
                )
            )
        }

        return RestClient.builder(
            HttpHost(
                config.host,
                config.port,
                config.schema
            )
        ).setHttpClientConfigCallback { httpClientBuilder ->
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
        }.build()
    }

    @Bean
    fun provideElasticTransport(
        restClient: RestClient,
        mapper: ObjectMapper,
    ) = RestClientTransport(
        restClient,
        JacksonJsonpMapper(mapper)
    )

    @Bean
    fun provideElasticSearchClient(transport: RestClientTransport) = ElasticsearchClient(transport)

    @Bean
    fun provideElasticSearchAsyncClient(transport: RestClientTransport) = ElasticsearchAsyncClient(transport)
}