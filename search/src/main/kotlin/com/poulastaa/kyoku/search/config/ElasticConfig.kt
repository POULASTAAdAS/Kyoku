package com.poulastaa.kyoku.search.config

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient
import co.elastic.clients.elasticsearch.ElasticsearchClient
import com.poulastaa.kyoku.search.domain.model.dto.DtoElasticSearchConfigInfo
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
    fun provideElasticSearchClint(
        config: DtoElasticSearchConfigInfo,
    ) = ElasticsearchClient.of { builder ->
        builder.host(config.host)
            .usernameAndPassword(config.username, config.password)
    }!!

    @Bean
    fun provideElasticSearchAsyncClient(
        config: DtoElasticSearchConfigInfo,
    ) = ElasticsearchAsyncClient.of { builder ->
        builder.host(config.host)
            .usernameAndPassword(config.username, config.password)
    }!!
}