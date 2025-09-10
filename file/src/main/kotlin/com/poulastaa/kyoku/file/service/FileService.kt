package com.poulastaa.kyoku.file.service

import com.poulastaa.kyoku.file.model.dto.CacheTypes
import com.poulastaa.kyoku.file.model.dto.CachedContent
import com.poulastaa.kyoku.file.model.dto.DtoFileData
import com.poulastaa.kyoku.file.model.dto.MiniOBuckets
import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.StatObjectArgs
import org.springframework.core.io.InputStreamResource
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream

@Service
class FileService(
    private val bucket: MiniOBuckets,
    private val client: MinioClient,
    private val cache: FileCacheService,
) {
    fun getStaticFiles(
        fileName: String,
        type: CacheTypes,
    ) = cache.cache(fileName, type)?.let {
        DtoFileData(
            size = it.size,
            fileName = it.fileName,
            contentType = it.contentType,
            content = InputStreamResource(it.toInputStream()!!)
        )
    } ?: getBucket(type).let { bucket ->
        val obj = client.getObject(
            GetObjectArgs.builder()
                .bucket(bucket)
                .`object`(fileName)
                .build()
        )

        val info = client.statObject(
            StatObjectArgs.builder()
                .bucket(bucket)
                .`object`(fileName)
                .build()
        )

        val contentBytes = obj.use { it.readBytes() }

        DtoFileData(
            size = info.size(),
            fileName = fileName,
            contentType = info.contentType(),
            content = InputStreamResource(ByteArrayInputStream(contentBytes))
        ).also {
            cache.set(
                key = fileName,
                types = type,
                CachedContent(
                    fileName = fileName,
                    contentType = info.contentType(),
                    content = contentBytes,
                    size = info.size(),
                )
            )
        }
    }

    private fun getBucket(type: CacheTypes): String = when (type) {
        CacheTypes.STATIC -> bucket.static
        CacheTypes.POSTER -> bucket.poster
        CacheTypes.ARTIST_IMAGE -> bucket.artist
        CacheTypes.GENRE_IMAGE -> bucket.genre
    }
}