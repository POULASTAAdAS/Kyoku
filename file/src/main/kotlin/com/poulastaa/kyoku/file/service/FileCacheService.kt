package com.poulastaa.kyoku.file.service

import com.poulastaa.kyoku.file.model.dto.CacheHtml
import com.poulastaa.kyoku.file.model.dto.CacheTypes
import com.poulastaa.kyoku.file.model.dto.CachedImage
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.cache.get
import org.springframework.stereotype.Service

@Service
class FileCacheService(
    @param:Qualifier("provideCacheManager")
    private val cache: CacheManager,
) {
    private val htmlCache by lazy {
        cache.getCache(CacheTypes.STATIC_PAGE.name)
    }
    private val logoCache by lazy {
        cache.getCache(CacheTypes.LOGO.name)
    }

    fun cacheHtml(key: String) = htmlCache?.get<CacheHtml>(key)
    fun cacheLogo(key: String) = logoCache?.get<CachedImage>(key)

    fun setHtml(key: String, html: CacheHtml) = htmlCache?.put(key, html)
    fun setLogo(key: String, logo: CachedImage) = logoCache?.put(key, logo)
}