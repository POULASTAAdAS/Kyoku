package com.poulastaa.notification.domain.repository

interface MailServiceRepository {
    fun sendEmailVerificationMail(email: String)

    fun sendPasswordResetMail(email: String)

    fun sendWelcomeMail(email: String, username: String)
    fun sendWelcomeBackMail(email: String, username: String)
}