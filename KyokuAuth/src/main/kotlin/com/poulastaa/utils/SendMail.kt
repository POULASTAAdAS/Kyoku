package com.poulastaa.utils

import com.poulastaa.utils.Constants.SMS_EMAIL_GOOGLE_SMTP_HOST
import com.poulastaa.utils.Constants.SMS_EMAIL_PORT
import com.sun.mail.util.MailConnectException
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.*

fun sendEmail(
    to: String,
    subject: String,
    content: String,
): Boolean {
    val props = Properties().apply {
        this["mail.smtp.host"] = SMS_EMAIL_GOOGLE_SMTP_HOST
        this["mail.smtp.port"] = SMS_EMAIL_PORT
        this["mail.smtp.auth"] = "true"
        this["mail.smtp.starttls.enable"] = "true"
    }


    val session = Session.getInstance(
        props,
        object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(
                    System.getenv("email"),
                    System.getenv("password")
                )
            }
        }
    )

    return try {
        val message = MimeMessage(session)
        message.setFrom(InternetAddress(System.getenv("email")))
        message.setRecipients(Message.RecipientType.TO, to)

        message.subject = subject
        message.sentDate = Date()

        message.setContent(content, "text/html")
        Transport.send(message)
        true
    } catch (e: MessagingException) {
        false
    } catch (e: MailConnectException) {
        false
    } catch (e: java.net.ConnectException) {
        false
    } catch (e: Exception) {
        false
    }
}