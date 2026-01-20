package com.poulastaa.kyoku.playlist.database.content.entity

import com.poulastaa.kyoku.playlist.database.playlist.entity.BaseIdEntity
import com.poulastaa.kyoku.playlist.utils.CountryId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "Country")
class EntityCountry(
    @Column(name = "country", nullable = false, length = 40, unique = true)
    val country: String,

    @Column(name = "code", nullable = false, length = 4, unique = true)
    val code: String,
) : BaseIdEntity<CountryId>()
