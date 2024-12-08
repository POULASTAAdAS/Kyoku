package com.poulastaa.notification.data.repository

import com.poulastaa.core.domain.model.Endpoints
import com.poulastaa.core.domain.repository.JWTRepository
import com.poulastaa.core.domain.utils.Constants.SMS_EMAIL_GOOGLE_SMTP_HOST
import com.poulastaa.core.domain.utils.Constants.SMS_EMAIL_PORT
import com.poulastaa.notification.domain.repository.MailServiceRepository
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.*

class GoogleMailService(
    private val jwt: JWTRepository,
) : MailServiceRepository {
    private val appLogoUrl = "${System.getenv("BASE_URL")}/images/app_logo.png"

    override fun sendEmailVerificationMail(email: String) {
        val token = jwt.generateToken(email, JWTRepository.TokenType.TOKEN_VERIFICATION_MAIL)
        val verificationUrl = "${System.getenv("BASE_URL")}${Endpoints.VerifyEmail.route}?token=$token"

        val sub = "Verify Your Email to Complete Registration"
        val content = """
                    <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Verify Your Email</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    color: #333;
                    line-height: 1.6;
                }
                .container {
                    width: 80%;
                    margin: 0 auto;
                    padding: 20px;
                    background-color: #f4f4f4;
                    border-radius: 10px;
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                }
                .appLogo img {
                    width: 150px;
                    margin-bottom: 20px;
                    display: block;
                    margin-left: auto;
                    margin-right: auto;
                }
                .header {
                    color: #4CAF50;
                    text-align: center;
                }
                .content {
                    margin: 20px 0;
                    text-align: center;
                }
                .cta-button {
                    display: inline-block;
                    padding: 10px 20px;
                    color: white;
                    background-color: #4CAF50;
                    text-decoration: none;
                    border-radius: 5px;
                    margin-top: 20px;
                }
                .footer {
                    margin-top: 20px;
                    text-align: center;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <p class="appLogo">
                    <a>
                        <img src="$appLogoUrl" alt="Kyoku Logo">
                    </a>
                </p>
            
                <h1 class="header">Welcome to Kyoku!</h1>
                <p class="content">Thank you for signing up. To complete your registration, please verify your email address by clicking the link below:</p>
                <p class="content">
                    <a href="$verificationUrl" class="cta-button">Verify Email</a>
                </p>
                <p class="footer">If you didn't sign up for an account, please ignore this email.</p>
                <p class="footer">Best regards,<br>Kyoku</p>
            </div>
        </body>
        </html>
        """.trimIndent()

        sendMail(
            to = email,
            subject = sub,
            content = content,
        )
    }

    override fun sendPasswordResetMail(email: String) {
        val token = jwt.generateToken(email, JWTRepository.TokenType.TOKEN_VERIFICATION_MAIL)
        val resetPasswordUrl = "${System.getenv("BASE_URL")}${Endpoints.ResetPassword.route}?token=$token"

        val sub = "Password Reset Mail"
        val content = """
            <!DOCTYPE html>
            <html lang="en">
                <head>
                  <meta charset="UTF-8" />
                  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                  <title>Password Reset Mail</title>
                  <style>
                    body {
                      font-family: Arial, sans-serif;
                      color: #333;
                      line-height: 1.6;
                    }
    
                    .container {
                      width: 80%;
                      margin: 0 auto;
                      padding: 20px;
                      background-color: #f4f4f4;
                      border-radius: 10px;
                      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                    }
    
                    .appLogo img {
                      width: 150px;
                      margin-bottom: 20px;
                      display: block;
                      margin-left: auto;
                      margin-right: auto;
                    }
    
                    .header {
                      color: #4caf50;
                      text-align: center;
                    }
    
                    .content {
                      margin: 20px 0;
                      text-align: center;
                    }
    
                    .cta-button {
                      display: inline-block;
                      padding: 10px 20px;
                      color: white;
                      background-color: #4caf50;
                      text-decoration: none;
                      border-radius: 5px;
                      margin-top: 20px;
                    }
    
                    .footer {
                      margin-top: 20px;
                      text-align: center;
                      text-decoration: underline;
                    }
                  </style>
                </head>

                <body>
                  <div class="container">
                    <p class="appLogo">
                      <a>
                        <img src="$appLogoUrl" alt="Kyoku Logo" />
                      </a>
                    </p>
    
                    <h1 class="header">Password reset Mail!</h1>
    
                    <p class="content">You̥ are seeing this email because you requested for a password reset.</p>
                    <p class="content">
                      Follow the bellow link to reset you password.
                      Please login again through app after changing you password.
                    </p>
                    
                    <p class="content">
                     This link is only valid for 10 minute.
                    </p>
                    
                    <p class="content">
                      <a href="$resetPasswordUrl" class="cta-button">Reset Password</a>
                    </p>
                    <p class="footer">
                      If you didn't requested for password reset, please ignore this email.
                    </p>
                    <p class="footer">Best regards,<br />Kyoku</p>
                  </div>
                </body>
            </html>
        """.trimIndent()

        sendMail(
            to = email,
            subject = sub,
            content = content,
        )
    }

    override fun sendWelcomeMail(email: String, username: String) {
        val sub = "Welcome $username to Kyoku."
        val content = """
            <!DOCTYPE html>
            <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Welcome to Kyoku</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            color: #333;
                            background-color: #f9f9f9;
                            margin: 0;
                            padding: 20px;
                        }
                        .container {
                            max-width: 600px;
                            margin: 0 auto;
                            background-color: #fff;
                            padding: 20px;
                            border-radius: 10px;
                            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                            text-align: center;
                        }
                        .header {
                            color: #4CAF50;
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
                        }
                        .footer {
                            margin-top: 20px;
                            font-size: 0.9em;
                            color: #777;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <p class="appLogo">
                            <a>
                                <img src="$appLogoUrl" alt="Kyoku Logo">
                            </a>
                        </p>
                        <h1 class="header">Welcome to Kyoku!</h1>
                        <div class="content">
                            <p>
                                <h3>Listen to millions of songs ad-free.</h3>
                                <h3>Download as many songs as you want.</h3>
                            </p>
                        </div>
                        <div class="footer">
                            <p>This app is developed by a single developer.</p>
                            <p>If you encounter any bugs, please email me at: <strong>${System.getenv("devEmail")}</strong></p>
                            <p>Hope you have a great experience with Kyoku!</p>
                            <p>Thank you! :)</p>
                        </div>
                    </div>
                </body>
            </html>
        """.trimIndent()

        sendMail(
            to = email,
            subject = sub,
            content = content,
        )
    }

    override fun sendWelcomeBackMail(email: String, username: String) {
        val sub = "It's nice to have you back $username, at Kyoku."
        val content = """
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
                            padding: 20px;
                        }
                        .container {
                            max-width: 600px;
                            margin: 0 auto;
                            background-color: #fff;
                            padding: 20px;
                            border-radius: 10px;
                            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                            text-align: center;
                        }
                        .header {
                            color: #4CAF50;
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
                        }
                        .footer {
                            margin-top: 20px;
                            font-size: 0.9em;
                            color: #777;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                         <p class="appLogo">
                            <a>
                                <img src="$appLogoUrl" alt="Kyoku Logo">
                            </a>
                        </p>
                        <h1 class="header">Welcome back to Kyoku!</h1>
                        <div class="content">
                            <p>It's fantastic to see you back with us!</p>
                            <p>We've missed you and are thrilled to have you back.</p>
                            <p>We hope you're ready for an exciting journey ahead.</p>
                            <p>Here's to making great memories and having a blast together!</p>
                        </div>
                        <div class="footer">
                            <p>Best regards,</p>
                            <p>From Kyoku :)</p>
                        </div>
                    </div>
                </body>
            </html>
    """.trimIndent()

        sendMail(
            to = email,
            subject = sub,
            content = content
        )
    }

    private fun sendMail(
        to: String,
        subject: String,
        content: String,
        senderEmail: String = System.getenv("SENDER_EMAIL"),
        password: String = System.getenv("SENDER_PASSWORD"),
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
                        senderEmail,
                        password
                    )
                }
            }
        )

        return try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(senderEmail))
            message.setRecipients(Message.RecipientType.TO, to)

            message.subject = subject
            message.sentDate = Date()

            message.setContent(content, "text/html")
            Transport.send(message)
            true
        } catch (_: Exception) {
            false
        }
    }
}