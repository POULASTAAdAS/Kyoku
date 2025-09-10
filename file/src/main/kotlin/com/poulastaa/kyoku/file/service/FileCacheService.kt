package com.poulastaa.kyoku.file.service

import com.poulastaa.kyoku.file.model.dto.CacheTypes
import com.poulastaa.kyoku.file.model.dto.CachedContent
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.cache.get
import org.springframework.stereotype.Service

@Service
class FileCacheService(
    @param:Qualifier("provideCacheManager")
    private val cache: CacheManager,
) {
    private val static by lazy {
        cache.getCache(CacheTypes.STATIC.name)
    }

    fun cache(key: String, types: CacheTypes) = when (types) {
        CacheTypes.STATIC -> static?.get<CachedContent>(key)
        else -> TODO("not yet implemented")
    }

    fun set(key: String, types: CacheTypes, data: CachedContent) = when (types) {
        CacheTypes.STATIC -> static?.put(key, data)
        else -> TODO("not yet implemented")
    }
}