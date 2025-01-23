package com.poulastaa.core.domain.model

enum class DtoUpsertOperation {
    INSERT,
    UPDATE,
    DELETE
}

data class DtoUpsert<T>(
    val id: T,
    val operation: DtoUpsertOperation,
)