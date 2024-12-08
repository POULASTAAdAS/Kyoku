package com.poulastaa.notification.data.repository

import com.poulastaa.core.domain.repository.JWTRepository
import com.poulastaa.notification.domain.repository.MailServiceRepository

class GoogleMailService(
    private val jwt: JWTRepository,
) : MailServiceRepository {
    override fun sendEmailVerificationMail(email: String) {

    }

    override fun sendPasswordResetMail(email: String) {

    }

    override fun sendWelcomeMail(email: String) {

    }

    override fun sendWelcomeBackMail(email: String) {

    }
}