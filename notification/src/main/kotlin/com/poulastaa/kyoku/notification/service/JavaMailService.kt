package com.poulastaa.kyoku.notification.service

import com.poulastaa.kyoku.notification.utils.Email
import com.poulastaa.kyoku.notification.utils.JWTToken
import com.poulastaa.kyoku.notification.utils.Username
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
    private val senderEmail: String,
    @param:Value("\${notification.sender.password}")
    private val password: String,
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
        content = """
            <!DOCTYPE html>
            <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Welcome to App Name</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            color: #333;
                            background-color: #f9f9f9;
                            margin: 0;
                            line-height: 1.6;
                            padding: 0;
                            padding: 20px;
                        }
                        .container {
                            max-width: 600px;
                            margin: 0 auto;
                            background-color: #BDC1FD;
                            padding: 20px;
                            border-radius: 10px;
                            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                            text-align: center;
                        }
                        .header {
                            color: #232772;
                        }
                        .appLogo img {
                            width: 150px;
                            margin-bottom: 20px;
                            display: block;
                            margin-left: auto;
                            margin-right: auto;
                        }
                        .content {
                            margin: 20px 0;
                            color: #232772;
                        }
                        .footer {
                            margin-top: 20px;
                            font-size: 0.9em;
                            color: #232772;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <p class="appLogo">
                            <a>
                                <img src="http://kyoku.poulastaa.shop:9000/images/app_logo.png" alt="App Logo">${ /*TODO check proper url*/ ""}
                            </a>
                        </p>
                        <h1 class="header">Welcome to App Name!</h1>
                        <div class="content">
                            <p>
                                app content
                            </p>
                        </div>
                        <div class="footer">
                            <p>If you encounter any bugs, please email me at: <strong>${senderEmail}</strong></p>
                            <p>Hope you have a great experience with App Name!</p>
                            <p>Thank you! :)</p>
                        </div>
                    </div>
                </body>
            </html>
        """.trimIndent(),
    ).also { if (it) println("welcome mail send to :$username") }

    fun sendWelcomeBackMail(
        email: Email,
        username: String,
    ) = sendMail(
        to = email,
        subject = "It's nice to have you back $username, at Kyoku.",
        content = """
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Welcome Back to Kyoku</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        color: #333;
                        background-color: #f9f9f9;
                        margin: 0;
                        padding: 0;
                        padding: 20px;
                    }

                    .container {
                        max-width: 600px;
                        margin: 0 auto;
                        background-color: #BDC1FD;
                        padding: 20px;
                        border-radius: 10px;
                        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                        text-align: center;
                    }

                    .header {
                        color: #232772;
                    }

                    .appLogo img {
                        width: 150px;
                        margin-bottom: 20px;
                        display: block;
                        margin-left: auto;
                        margin-right: auto;
                    }

                    .content {
                        margin: 20px 0;
                        color: #232772;
                    }

                    .footer {
                        margin-top: 20px;
                        font-size: 0.9em;
                        color: #232772;
                    }
                </style>
            </head>

            <body>
                <div class="container">
                    <p class="appLogo">
                        <a>
                            <img src="http://kyoku.poulastaa.shop:9000/images/app_logo.png" alt="App Logo">${ /*TODO check proper url*/ ""}
                        </a>
                    </p>
                    <h1 class="header">Welcome back to App name!</h1>
                    <div class="content">
                        <p>It's fantastic to see you back with us!</p>
                        <p>We've missed you and are thrilled to have you back.</p>
                        <p>We hope you're ready for an exciting journey ahead.</p>
                    </div>
                    <br />
                    <div class="footer">
                        <p>Best regards</p>
                        <p>From App Name</p>
                    </div>
                </div>
            </body>

            </html>
        """.trimIndent(),
    ).also { if (it) println("welcome back mail send to :$username") }

    fun sendVerificationMail(
        email: Email,
        username: Username,
        endPoint: String,
        token: JWTToken,
    ) = sendMail(
        to = email,
        subject = "Verify Your Email to Complete Registration",
        content = """
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <title>Verify Your Email</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            color: #333;
                            line-height: 1.6;
                            margin: 0;
                            padding: 0;
                            background: #f4f4f4;
                        }
    
                        .container {
                            width: 80%;
                            max-width: 600px;
                            margin: 40px auto;
                            padding: 20px;
                            background-color: #BDC1FD;
                            border-radius: 10px;
                            box-shadow: 0 0 10px rgba(29, 28, 28, 0.1);
    
                            text-align: center;
                        }
    
                        .appLogo img {
                            width: 150px;
                            margin-bottom: 20px;
                        }
    
                        .header {
                            color: #232772;
                            margin-bottom: 20px;
                        }
    
                        .content {
                            margin: 20px 0;
                            color: #232772;
                        }
    
                        .cta-button {
                            display: inline-block;
                            padding: 10px 20px;
                            color: #BDC1FD;
                            background-color: #232772;
                            text-decoration: none;
                            border-radius: 5px;
                            margin-top: 20px;
                        }
    
                        .footer {
                            margin-top: 20px;
                            font-size: 0.9em;
                            color: #232772;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <p class="appLogo">
                              <img src="http://kyoku.poulastaa.shop:9000/images/app_logo.png" alt="App Logo">${ /*TODO check proper url*/ ""}
                        </p>
    
                        <h1 class="header">Welcome to App Name!</h1>
    
                        <p class="content">
                            Thank you for signing up. To complete your registration, please verify your email address by clicking the
                            link below:
                        </p>
    
                        <p class="content">
                            <a href="http://kyoku.poulastaa.shop:8080/$endPoint?token=$token" class="cta-button">Verify Email</a> ${ /*TODO check proper url*/ ""}
                        </p>
    
                        <p class="footer">
                            If you didn’t sign up for an account, please ignore this email.
                        </p>
                        <p class="footer">
                            Best regards
                            <br />
                            App Name
                        </p>
                    </div>
                </body>
            </html>
        """.trimIndent(),
    ).also { if (it) println("verification mail send to :$email") }

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