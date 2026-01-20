package com.poulastaa.kyoku.user.database.user.entity

import com.poulastaa.kyoku.user.database.BaseIdEntity
import com.poulastaa.kyoku.user.utils.UserTypeId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "UserType")
class EntityUserType(
    @Column(name = "type", unique = true, length = 8, nullable = false)
    val type: String = "",
) : BaseIdEntity<UserTypeId>()
