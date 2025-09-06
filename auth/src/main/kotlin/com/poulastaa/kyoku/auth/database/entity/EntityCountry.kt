package com.poulastaa.kyoku.auth.database.entity

import com.poulastaa.kyoku.auth.utils.CountryId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "Country")
class EntityCountry : BaseIdEntity<CountryId>() {
    @Column(name = "country", nullable = false, length = 40)
    var country: String = ""

    @Column(name = "code", nullable = false, length = 4)
    var code: String = ""
}