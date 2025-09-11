package com.poulastaa.kyoku.auth.database.entity

import com.poulastaa.kyoku.auth.utils.UserId
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp

@Entity
@Table(name = "UserJWTToken")
@AttributeOverride(
    name = "id",
    column = Column(name = "user_id", nullable = false)
)
class EntityJWTToken : BaseIdExtendedEntity<UserId>() {
    @Column(name = "refresh_token", nullable = false, length = 1000)
    var refreshToken: String = ""

    @Column(name = "created_at", nullable = false)
    @UpdateTimestamp
    var createdAt: Timestamp? = null
}