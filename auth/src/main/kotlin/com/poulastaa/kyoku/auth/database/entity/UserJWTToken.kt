package com.poulastaa.kyoku.auth.database.entity

import com.poulastaa.kyoku.auth.utils.UserId
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp

@Entity
@Table(name = "UserJWTToken")
@AttributeOverride(
    name = "id",
    column = Column(name = "user_id", nullable = false)
)
class UserJWTToken : BaseIdExtendedEntity<UserId>() {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = ForeignKey(
            name = "fk_user_token",
            foreignKeyDefinition = "FOREIGN KEY (`user_id`) REFERENCES `User`(`id`) ON DELETE CASCADE"
        )
    )
    lateinit var user: EntityUser

    @Column(name = "refresh_token", nullable = false, length = 1000)
    var refreshToken: String = ""

    @Column(name = "created_at", updatable = false, nullable = true)
    @CreationTimestamp
    var createdAt: Timestamp? = null
}