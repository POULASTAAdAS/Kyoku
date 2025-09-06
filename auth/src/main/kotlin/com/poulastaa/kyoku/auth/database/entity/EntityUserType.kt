package com.poulastaa.kyoku.auth.database.entity

import com.poulastaa.kyoku.auth.utils.UserTypeId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "UserType")
class EntityUserType(
    @Column(name = "type", unique = true, length = 8)
    val type: String = "",
) : BaseIdEntity<UserTypeId>()
