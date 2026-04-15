package com.poulastaa.kyoku.search.database.entity

import com.poulastaa.kyoku.search.database.BaseIdEntity
import com.poulastaa.kyoku.search.domain.model.dto.DtoCountry
import com.poulastaa.kyoku.search.utils.CountryId
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

    fun toDtoCountry() = DtoCountry(
        id = this.id,
        name = this.country,
        code = this.code,
    )
}