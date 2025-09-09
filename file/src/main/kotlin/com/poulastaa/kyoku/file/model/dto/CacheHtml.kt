package com.poulastaa.kyoku.file.model.dto

import java.io.Serializable

data class CacheHtml(
    val fileName: String,
    val contentType: String = FileType.PAGE.name,
    val content: String,
    val size: Long,
    val encoding: String = "UTF-8",
) : Serializable {
    /**
     *      Increment only for breaking changes:
     *      - Removing fields
     *      - Changing field types
     *      - Changing class hierarchy
     *
     *      These are usually safe (don't increment):
     *      - Adding new fields with defaults
     *      - Adding methods
     *      - Changing method implementations
     */
    companion object {
        private const val serialVersionUID = 1L
    }
}
