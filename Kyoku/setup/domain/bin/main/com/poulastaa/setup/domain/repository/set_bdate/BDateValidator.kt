package com.poulastaa.setup.domain.repository.set_bdate

interface BDateValidator {
    fun toDate(time: Long): String
    fun validate(time: Long): ValidationType

    enum class ValidationType(val message: String, val isErr: Boolean = true) {
        VALID("", false),
        LESS_THAN_18("You must be 18 years old to use this app"),
        GREATER_THAN_100("You must be less than 100 years old to use this app"),
    }
}