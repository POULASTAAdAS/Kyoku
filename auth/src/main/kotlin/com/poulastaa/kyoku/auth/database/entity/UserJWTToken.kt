package com.poulastaa.kyoku.auth.database.entity

import com.poulastaa.kyoku.auth.utils.UserId
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table


@Entity
@Table(name = "UserJWTToken")
@AttributeOverride(
    name = "id",
    column = Column(name = "userId", nullable = false)
)
class UserJWTToken(
    lateinit var user: EntityUser,
) : BaseIdEntity<UserId>()