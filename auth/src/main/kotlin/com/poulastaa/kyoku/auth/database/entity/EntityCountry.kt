package com.poulastaa.kyoku.auth.database.entity

import com.poulastaa.kyoku.auth.utils.CountryId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "Country",
    uniqueConstraints = [
        UniqueConstraint(
            columnNames = ["code"],
            name = "uq_code"
        ),
        UniqueConstraint(
            columnNames = ["country"],
            name = "uq_country"
        )
    ]
)
class EntityCountry : BaseIdEntity<CountryId>() {
    @Column(name = "country", nullable = false, length = 40)
    val country: String = ""

    @Column(name = "code", nullable = false, length = 4)
    val code: String = ""
}