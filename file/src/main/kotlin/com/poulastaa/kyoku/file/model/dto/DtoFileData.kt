package com.poulastaa.kyoku.file.model.dto

import org.springframework.core.io.InputStreamResource

data class DtoFileData(
    val size: Long,
    val fileName: String,
    val contentType: String,
    val content: InputStreamResource,
)
