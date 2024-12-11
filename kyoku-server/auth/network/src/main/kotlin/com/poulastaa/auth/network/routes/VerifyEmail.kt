package com.poulastaa.auth.network.routes

import com.poulastaa.auth.domain.model.EmailVerificationStatusDto
import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.core.domain.model.Endpoints
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.verifyEmail(
    repo: AuthRepository,
) {
    route(Endpoints.VerifyEmail.route) {
        get {
            val token = call.parameters["token"] ?: return@get call.respondRedirect(Endpoints.UnAuthorized.route)
            val status = repo.verifyEmail(token)

            val response = when (status) {
                EmailVerificationStatusDto.VERIFIED -> responseVerificationMailHtml(
                    headingColor = "#309786",
                    heading = "Authentication Successful",
                    title = "Authentication Successful",
                    content1 = "Email Verification Successful.",
                    content2 = "Please Navigate back to app to continue."
                )

                EmailVerificationStatusDto.TOKEN_USED -> responseVerificationMailHtml(
                    headingColor = "#e65353",
                    heading = "Authentication Failed",
                    title = "Link Already Used",
                    content1 = "The Verification link has been used.",
                    content2 = "Please try again."
                )

                EmailVerificationStatusDto.USER_NOT_FOUND -> responseVerificationMailHtml(
                    headingColor = "#e65353",
                    heading = "Authentication Failed",
                    title = "No User Found",
                    content1 = "No User found on This Email.",
                    content2 = "Please SignUp. First"
                )

                EmailVerificationStatusDto.SERVER_ERROR -> responseVerificationMailHtml(
                    headingColor = "#e65353",
                    heading = "Authentication Failed",
                    title = "Error Occurred",
                    content1 = "Opp's Something went wrong.",
                    content2 = "Please try again."
                )
            }

            call.respondText(
                text = response,
                contentType = ContentType.Text.Html,
            )
        }
    }
}

private fun responseVerificationMailHtml(
    headingColor: String,
    title: String,
    heading: String,
    content1: String,
    content2: String,
) = """
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>$title</title>
            <style>
                body,
                html {
                    height: 100%;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    background-color: #f0f0f0;
                    margin: 0;
                }

                 .container {
                    width: 400px;
                    position: relative;
                    padding: 70px;
                    background-color: #f4f4f4;
                    border-radius: 30px;
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                }

                .appLogo img {
                    width: 150px;
                    display: block;
                    margin: 0 auto;
                }

                .header {
                    color: $headingColor;
                    text-align: center;
                }

                .content {
                    text-align: center;
                }
            </style>
        </head>

        <body>
            <div class="container">
                <p class="appLogo">
                    <a>
                        <img src="${System.getenv("BASE_URL")}/images/app_logo.png" alt="Logo" />
                    </a>
                </p>

                <h1 class="header">$heading</h1>
                <h4 class="content">
                    <p>
                        $content1
                        <br>
                        $content2
                    </p>
                </h4>
            </div>
        </body>

        </html>
    """.trimIndent()