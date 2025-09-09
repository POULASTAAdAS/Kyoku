package com.poulastaa.kyoku.file.config

import com.poulastaa.kyoku.file.model.dto.MiniOBuckets
import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MiniOConfig {
    @Bean
    fun provideMiniOBuckets(
        @Value("\${minio.bucket.static}")
        staticBucket: String,
        @Value("\${minio.bucket.file.artist}")
        artistBucket: String,
        @Value("\${minio.bucket.file.genre}")
        genreBucket: String,
        @Value("\${minio.bucket.file.song}")
        posterBucket: String,
    ) = MiniOBuckets(
        static = staticBucket,
        artist = artistBucket,
        genre = genreBucket,
        poster = posterBucket,
    )

    @Bean
    fun provideMiniOClient(
        @Value("\${minio.url}")
        url: String,
        @Value("\${minio.username}")
        username: String,
        @Value("\${minio.password}")
        password: String,
    ) = MinioClient.builder()
        .endpoint(url)
        .credentials(username, password)
        .build()!!
}