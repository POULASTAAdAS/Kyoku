package com.poulastaa.kyoku.file.model.dto

import java.io.ByteArrayInputStream
import java.io.Serializable

data class CachedContent(
    val fileName: String,
    val contentType: String,
    val size: Long,
    val content: ByteArray? = null,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CachedContent

        if (size != other.size) return false
        if (fileName != other.fileName) return false
        if (contentType != other.contentType) return false
        if (!content.contentEquals(other.content)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = size.hashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + (content?.contentHashCode() ?: 0)
        return result
    }

    fun toInputStream() = content?.let { ByteArrayInputStream(it) }

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