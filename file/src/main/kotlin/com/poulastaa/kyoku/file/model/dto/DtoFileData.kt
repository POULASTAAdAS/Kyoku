package com.poulastaa.kyoku.file.model.dto

data class DtoFileData<T>(
    val size: Long,
    val fileName: String,
    val contentType: String,
    val content: T,
)
