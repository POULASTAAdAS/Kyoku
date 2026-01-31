package com.poulastaa.kyoku.activity.database.user.entity

import com.poulastaa.kyoku.activity.database.BaseIdEntity
import com.poulastaa.kyoku.activity.utils.UserTypeId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "UserType")
class EntityUserType(
    @Column(name = "type", unique = true, length = 8, nullable = false)
    val type: String = "",
) : BaseIdEntity<UserTypeId>()
