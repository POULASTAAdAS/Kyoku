package com.poulastaa.kyoku.notification.service

import com.poulastaa.kyoku.notification.utils.*
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

private const val SMS_EMAIL_GOOGLE_SMTP_HOST = "smtp.gmail.com"
private const val SMS_EMAIL_PORT = "587"

@Service
class JavaMailService(
    @param:Value("\${notification.sender.email}")
    private val senderEmail: Email,
    @param:Value("\${notification.sender.password}")
    private val password: String,
    @param:Value("\${notification.dev}")
    private val devMail: Email,
    @param:Value("\${basUrl}")
    private val baseUrl: String,
) {
    private val session by lazy {
        Session.getInstance(
            Properties().apply {
                this["mail.smtp.host"] = SMS_EMAIL_GOOGLE_SMTP_HOST
                this["mail.smtp.port"] = SMS_EMAIL_PORT
                this["mail.smtp.auth"] = "true"
                this["mail.smtp.starttls.enable"] = "true"

                put("mail.smtp.connectiontimeout", "10000")
                put("mail.smtp.timeout", "10000")
            },
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(
                        senderEmail,
                        password
                    )
                }
            }
        )
    }

    fun sendWelcomeMail(
        email: Email,
        username: Username,
    ) = sendMail(
        to = email,
        subject = "Welcome $username to Kyoku.",
        content = generateWelcomeMailContent(username, this.devMail),
    ).also { if (it) println("welcome mail send to :$email") }

    // TODO pull data from user or content service
    fun sendWelcomeBackMail(
        email: Email,
        username: String,
    ) = sendMail(
        to = email,
        subject = "It's nice to have you back $username, at Kyoku.",
        content = generateWelcomeBackContent(
            this.devMail,
            allLibrarySongs = 234,
            playlist = 12,
            totalHour = 89
        ),
    ).also { if (it) println("welcome back mail send to :$email") }

    fun sendVerificationMail(
        email: Email,
        username: Username,
        endPoint: String,
        token: JWTToken,
    ) = sendMail(
        to = email,
        subject = "$username Verify Your Email to Complete Registration",
        content = generateVerificationMailContent(
            token = token,
            username = username,
            endpoint = "$baseUrl/$endPoint",
            devMail = devMail
        ),
    ).also { if (it) println("verification mail send to :$email") }

    fun sendForgotPasswordCodeWithMail(
        username: Username,
        email: Email,
        code: String,
    ) = sendMail(
        to = email,
        subject = "$username Reset Your Kyoku Password",
        content = generateForgotPasswordMailContent(
            username = username,
            code = code,
            devMail = devMail
        ),
    ).also { if (it) println("forgot password code mail send to :$email") }

    private fun sendMail(
        to: String,
        subject: String,
        content: String,
    ): Boolean = try {
        MimeMessage(session).apply {
            setFrom(InternetAddress(senderEmail))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
            this.subject = subject
            sentDate = Date()
            setContent(content, "text/html; charset=UTF-8")
        }.also { Transport.send(it) }
        true
    } catch (e: Exception) {
        println(e.printStackTrace())
        false
    }
}