package com.poulastaa.kyoku.file.config

import com.poulastaa.kyoku.file.model.dto.CacheTypes
import com.poulastaa.kyoku.file.model.dto.CachedContent
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ExpiryPolicyBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.ehcache.config.units.MemoryUnit
import org.ehcache.core.config.DefaultConfiguration
import org.ehcache.jsr107.EhcacheCachingProvider
import org.springframework.cache.CacheManager
import org.springframework.cache.jcache.JCacheCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

@Configuration
class CacheConfig {
    @Bean
    fun provideCacheManager(): CacheManager = EhcacheCachingProvider().let {
        JCacheCacheManager(
            it.getCacheManager(
                it.defaultURI,
                DefaultConfiguration(
                    CacheManagerBuilder.newCacheManagerBuilder()
                        .withCache(
                            CacheTypes.STATIC.name,
                            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                String::class.java,
                                CachedContent::class.java,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                    .heap(500, MemoryUnit.KB) // update id needed ||| for current 9 files not needed
                            ).withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(30.minutes.toJavaDuration()))
                        )
                        .withCache(
                            CacheTypes.POSTER.name,
                            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                String::class.java,
                                CachedContent::class.java,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                    .heap(150, MemoryUnit.MB)
                                    .offheap(1, MemoryUnit.GB)
                            ).withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(45.minutes.toJavaDuration()))
                        )
                        .withCache(
                            CacheTypes.ARTIST_IMAGE.name,
                            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                String::class.java,
                                CachedContent::class.java,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                    .heap(50, MemoryUnit.MB)
                                    .offheap(200, MemoryUnit.MB)
                            ).withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(45.minutes.toJavaDuration()))
                        )
                        .withCache(
                            CacheTypes.GENRE_IMAGE.name,
                            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                String::class.java,
                                CachedContent::class.java,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                    .heap(8, MemoryUnit.MB)
                                    .offheap(12, MemoryUnit.MB)
                            ).withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(20.minutes.toJavaDuration()))
                        ).build(true).runtimeConfiguration.cacheConfigurations,
                    it.javaClass.classLoader
                )
            )
        )
    }
}