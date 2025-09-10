package com.poulastaa.kyoku.file.controller

import com.poulastaa.kyoku.file.model.Endpoints
import com.poulastaa.kyoku.file.model.dto.CacheTypes
import com.poulastaa.kyoku.file.service.FileService
import jakarta.validation.Valid
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FileController(
    private val service: FileService,
) {
    @GetMapping(Endpoints.GET_STATIC_CONTENT)
    fun getStaticLogo(
        @Valid @RequestParam("fileName") filename: String,
    ) = try {
        service.getStaticFiles(filename, CacheTypes.STATIC).let {
            ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(it.contentType))
                .contentLength(it.size)
                .header("Cache-Control", "public, max-age=36000")
                .body(it.content as Resource)
        }
    } catch (_: Exception) {
        ResponseEntity.notFound().build()
    }
}