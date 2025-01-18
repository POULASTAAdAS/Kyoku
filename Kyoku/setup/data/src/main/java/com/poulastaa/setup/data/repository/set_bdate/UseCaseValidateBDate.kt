package com.poulastaa.setup.data.repository.set_bdate

import com.poulastaa.setup.domain.repository.set_bdate.BDateValidator
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class UseCaseValidateBDate : BDateValidator {
    override fun toDate(time: Long): String {
        val instant = Instant.ofEpochMilli(time)
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            .withZone(ZoneId.systemDefault())

        return formatter.format(instant)
    }

    override fun validate(time: Long): BDateValidator.ValidationType {
        val zone = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault())
        val currentDate = LocalDate.now()
        val birthDate = zone.toLocalDate()

        val age = Period.between(birthDate, currentDate).years

        return when {
            age < 18 -> BDateValidator.ValidationType.LESS_THAN_18
            age > 100 -> BDateValidator.ValidationType.GREATER_THAN_100
            else -> BDateValidator.ValidationType.VALID
        }
    }
}