package com.poulastaa.kyoku.file.service

import com.poulastaa.kyoku.file.model.dto.DtoFileData
import com.poulastaa.kyoku.file.model.dto.MiniOBuckets
import com.poulastaa.kyoku.file.model.dto.StaticFileType
import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.StatObjectArgs
import org.springframework.core.io.InputStreamResource
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class FileService(
    private val bucket: MiniOBuckets,
    private val client: MinioClient,
    private val cache: FileCacheService,
) {
    fun getStaticFiles(
        fileName: String,
        type: StaticFileType,
    ) {
        when (type) {
            StaticFileType.LOGO -> cache.cacheLogo(fileName)?.let {
                DtoFileData(
                    size = it.size,
                    fileName = it.fileName,
                    contentType = it.contentType,
                    content = it.toInputStream()
                )
            } ?: client.getObject(
                GetObjectArgs.builder()
                    .bucket(bucket.static)
                    .`object`(fileName)
                    .build()
            ).let {
                val info = client.statObject(
                    StatObjectArgs.builder()
                        .bucket(bucket.static)
                        .`object`(fileName)
                        .build()
                )

                DtoFileData(
                    size = info.size(),
                    fileName = fileName,
                    contentType = info.contentType(),
                    content = InputStreamResource(it as InputStream)
                )
            }

            StaticFileType.PAGE -> cache.cacheHtml(fileName)?.let {
                DtoFileData(
                    size = it.size,
                    fileName = fileName,
                    contentType = it.contentType,
                    content = it.content
                )
            } ?: client.getObject(
                GetObjectArgs.builder()
                    .bucket(bucket.static)
                    .`object`(fileName)
                    .build()
            ).let {
                val info = client.statObject(
                    StatObjectArgs.builder()
                        .bucket(bucket.static)
                        .`object`(fileName)
                        .build()
                )

                DtoFileData(
                    size = info.size(),
                    fileName = fileName,
                    contentType = info.contentType(),
                    content = InputStreamResource(it as InputStream)
                )
            }

            else -> TODO("not yet implemented")
        }
    }
}