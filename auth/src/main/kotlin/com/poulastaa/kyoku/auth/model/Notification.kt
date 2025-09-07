package com.poulastaa.kyoku.auth.model

import org.springframework.beans.factory.annotation.Value
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes


sealed class Notification(
    val channel: String,
) {
    enum class Channel {
        EMAIL,
    }

    sealed class Property(val expTime: Duration) {
        data class Endpoint(
            @param:Value("\${jwt.mail.verify.time}")
            val exp: Int = 10,
            @param:Value("\${jwt.mail.verify.unit}")
            val unit: String = "MINUTES",
            val endPoint: String = Endpoints.GET_VERIFY_EMAIL,
        ) : Property(
            expTime = when (unit.uppercase()) {
                "MINUTES" -> exp.minutes
                else -> 10.minutes
            }
        )

        data class Default(
            val exp: Int = 10,
            val unit: String = "MINUTES",
        ) : Property(
            expTime = when (unit.uppercase()) {
                "MINUTES" -> exp.minutes
                else -> 10.minutes
            }
        )
    }

    enum class Type(val prop: Property) {
        AUTHENTICATE(Property.Endpoint()),
        WELCOME(Property.Default()),
        WELCOME_BACK(Property.Default()),
    }

    data class Email(
        val email: String,
        val username: String,
        val data: Any = "",
        val type: Type,
    ) : Notification(channel = Channel.EMAIL.name)
}