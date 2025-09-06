package com.poulastaa.kyoku.auth.database.entity

import com.poulastaa.kyoku.auth.utils.UserId
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Date
import java.sql.Timestamp

@Entity
@Table(
    name = "User",
    indexes = [
        Index(columnList = "email", name = "idx_email"),
        Index(columnList = "user_type_id", name = "idx_user_type_id"),
    ],
    uniqueConstraints = [
        UniqueConstraint(
            columnNames = ["user_type_id", "email"],
            name = "uq_user_type_email"
        )
    ]
)
class EntityUser : BaseIdEntity<UserId>() {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_type_id",
        nullable = false,
        foreignKey = ForeignKey(
            name = "fk_user_user_type",
            foreignKeyDefinition = "FOREIGN KEY (`user_type_id`) REFERENCES `UserType`(`id`) ON DELETE"
        )
    )
    lateinit var userType: EntityUserType

    @Column(name = "email", nullable = false, length = 320)
    var email: String = ""

    @Column(name = "username", nullable = false, length = 320)
    var username: String = ""

    @Column(name = "display_name", nullable = false, length = 320)
    var displayName: String = ""

    @Column(name = "password_hash", nullable = false, length = 700)
    var passwordHash: String = ""

    @Column(name = "profile_pic", nullable = false, length = 700)
    var profilePicUrl: String? = null

    @Column(name = "birth_date", unique = false, nullable = true)
    @Temporal(TemporalType.DATE)
    var birthDate: Date? = null

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "country_id",
        nullable = false,
        foreignKey = ForeignKey(
            name = "fk_user_country",
            foreignKeyDefinition = "FOREIGN KEY (`country_id`) REFERENCES `Country`(`id`) ON DELETE"
        )
    )
    lateinit var country: EntityCountry

    @Column(name = "created_at", unique = false, updatable = false)
    @CreationTimestamp
    var createdAt: Timestamp? = null

    @Column(name = "last_updated", unique = false)
    @UpdateTimestamp
    var lastUpdated: Timestamp = Timestamp(System.currentTimeMillis())
}
