package com.poulastaa.kyoku.file.model.dto

import org.apache.http.entity.ContentType

enum class FileType(val type: String) {
    IMAGE_PNG(ContentType.IMAGE_PNG.mimeType),
    IMAGE_SVG(ContentType.IMAGE_SVG.mimeType),
    PAGE(ContentType.TEXT_HTML.mimeType)
}