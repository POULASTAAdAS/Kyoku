package com.poulastaa.core.domain.model

enum class DtoUpsertOperation {
    INSERT,
    UPDATE,
    DELETE
}

data class DtoUpsert<T>(
    val idList: List<T>,
    val operation: DtoUpsertOperation,
)