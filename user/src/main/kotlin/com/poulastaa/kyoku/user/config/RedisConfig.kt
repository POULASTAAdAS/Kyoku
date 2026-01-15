package com.poulastaa.kyoku.user.config
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.databind.SerializationFeature
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
//import com.fasterxml.jackson.module.kotlin.registerKotlinModule
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
//import org.springframework.cache.annotation.EnableCaching
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.redis.connection.RedisConnectionFactory
//import org.springframework.data.redis.core.RedisTemplate
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
//import org.springframework.data.redis.serializer.StringRedisSerializer
//
///**
// * Redis Configuration for Song Caching
// *
// * Enable/disable via application.yml:
// * spring:
// *   redis:
// *     enabled: true
// *     host: localhost
// *     port: 6379
// */
//@Configuration
//@EnableCaching
//@ConditionalOnProperty(
//    prefix = "spring.data.redis",
//    name = ["enabled"],
//    havingValue = "true",
//    matchIfMissing = false
//)
//class RedisConfig {
//
//    /**
//     * Configure ObjectMapper for Redis serialization
//     * Handles Kotlin data classes and Java Time API
//     */
//    @Bean
//    fun redisObjectMapper(): ObjectMapper {
//        return ObjectMapper().apply {
//            registerKotlinModule()
//            registerModule(JavaTimeModule())
//            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//            // Enable default typing for polymorphic deserialization
//            activateDefaultTyping(
//                polymorphicTypeValidator,
//                ObjectMapper.DefaultTyping.NON_FINAL
//            )
//        }
//    }
//
//    /**
//     * Configure RedisTemplate with JSON serialization
//     */
//    @Bean
//    fun redisTemplate(
//        connectionFactory: RedisConnectionFactory,
//        redisObjectMapper: ObjectMapper,
//    ): RedisTemplate<String, Any> {
//        return RedisTemplate<String, Any>().apply {
//            setConnectionFactory(connectionFactory)
//
//            // Use String serializer for keys
//            keySerializer = StringRedisSerializer()
//            hashKeySerializer = StringRedisSerializer()
//
//            // Use JSON serializer for values
//            val jsonSerializer = GenericJackson2JsonRedisSerializer(redisObjectMapper)
//            valueSerializer = jsonSerializer
//            hashValueSerializer = jsonSerializer
//
//            afterPropertiesSet()
//        }
//    }
//}
