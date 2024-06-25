package com.poulastaa.routes.forgot_password

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.VerifiedMailStatus
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.routes.verify_mail.components.responseVerificationMailHtml
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.resetPasswordPage(
    service: ServiceRepository,
) {
    route(EndPoints.ResetPassword.route) {
        get {
            val token = call.parameters["token"] ?: return@get call.respondText(EndPoints.UnAuthorised.route)
            val response = service.validateForgotPasswordMailToken(token)

            when (response.second) {
                VerifiedMailStatus.VERIFIED -> resetPasswordPage(
                    email = response.first
                )

                VerifiedMailStatus.TOKEN_USED -> responseVerificationMailHtml(
                    headingColor = "#e65353",
                    heading = "Error",
                    title = "Link Already Used",
                    content1 = "The link has been used.",
                    content2 = "Please try again."
                )

                VerifiedMailStatus.TOKEN_NOT_VALID -> responseVerificationMailHtml(
                    headingColor = "#e65353",
                    heading = "Error",
                    title = "Link Expired",
                    content1 = "The Link has expired",
                    content2 = "Please try again."
                )

                else -> responseVerificationMailHtml(
                    headingColor = "#e65353",
                    heading = "Error",
                    title = "Error Occurred",
                    content1 = "Opp's Something went wrong.",
                    content2 = "Please try again."
                )
            }.let {
                call.respondText(it, ContentType.Text.Html)
            }
        }
    }
}

private fun resetPasswordPage(
    email: String,
): String {
    val url = System.getenv("AUTH_URL")

    val eyeOnUrl = "$url/.well-known/eye_on.png"
    val eyeOffUrl = "$url/.well-known/eye_off.png"
    val appLogo = "$url/.well-known/app_logo.png"
    val submitUrl = url + EndPoints.SubmitNewPassword.route

    return """
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Password Confirmation</title>
        <style>
            body {
                margin: 0;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                background: linear-gradient(to top,
                        black,
                        #1A3A38);
                font-family: Arial, sans-serif;
            }
    
            .container {
                text-align: center;
                background-color: transparent;
                color: #fff;
                margin-right: 10rem;
                margin-bottom: 10rem;
            }
    
            .logo {
                margin-bottom: 20px;
                margin-left: 10.5rem;
            }
    
            .logo img {
                width: 11rem;
                height: 11rem;
                background-color: transparent;
                border-radius: 10px;
            }
    
            .form {
                max-width: 300px;
                margin: 0 auto;
            }
    
            .input-group {
                position: relative;
                margin-bottom: 15px;
            }
    
            .input-group img {
                width: 2rem;
                margin-left: 10rem;
            }
    
            .input-group input {
                width: 30rem;
                height: 4.5rem;
                padding: 10px;
                border: none;
                border-radius: 5px;
                box-sizing: border-box;
                background-color: transparent;
                border: 1px solid #09BEAA;
                color: white;
                font-size: 1.1rem;
            }
    
            .input-group label {
                position: absolute;
                top: 50%;
                left: 10px;
                transform: translateY(-50%);
                color: white;
                font-weight: bold;
                transition: 0.2s ease all;
                pointer-events: none;
                font-size: 1.2rem;
            }
    
            .input-group input:focus+label,
            .input-group input:not(:placeholder-shown)+label {
                top: 0;
                left: 10px;
                font-size: 1.5rem;
                color: #09BEAA;
                background: linear-gradient(to top,
                        #112423,
                        #102524);
            }
    
            button {
                width: 100%;
                height: 6vh;
                background-color: #008f8f;
                border: none;
                border-radius: 5px;
                font-size: 16px;
                cursor: pointer;
                color: white;
                margin-left: 5.5rem;
                margin-top: 3rem;
                letter-spacing: 10px;
                font-weight: bold;
            }
    
            button:hover {
                background-color: #09BEAA;
            }
    
            .info {
                font-size: 1.3rem;
                color: #ffffff;
                margin-left: 10.5rem;
                margin-top: 0.7rem;
            }
    
            .toggle-password {
                position: absolute;
                top: 30%;
                right: 10px;
                transform: translateY(-50%);
                transform: translatex(550%);
                cursor: pointer;
                width: 2rem;
                height: auto;
            }
        </style>
    </head>

    <body>
        <div class="container">
            <div class="logo">
                <img src="$appLogo" alt="Logo">
            </div>
            <form class="form" id="password-form">
                <div class="input-group">
                    <input type="password" id="password" placeholder=" " required>
                    <label for="password">Password</label>
                    <img src="$eyeOnUrl" alt="toggle" class="toggle-password" onclick="togglePassword('password', this)">
                </div>
                <div class="input-group">
                    <input type="password" id="repeat-password" placeholder=" " required>
                    <label for="repeat-password">Repeat Password</label>
                    <img src="$eyeOnUrl" alt="toggle" class="toggle-password"
                        onclick="togglePassword('repeat-password', this)">
                </div>
                <button type="button" onclick="sendPassword()">CONFIRM</button>
            </form>
            <div class="info">Please login from the app after resetting password</div>
        </div>
        <script>
            function togglePassword(fieldId, toggleImg) {
                const passwordField = document.getElementById(fieldId);
                const isPasswordeye_on = passwordField.type === 'password';
                passwordField.type = isPasswordeye_on ? 'text' : 'password';
                toggleImg.src = isPasswordeye_on ? "$eyeOffUrl" : "$eyeOnUrl";
            }

            function sendPassword() {
                const password = document.getElementById('password').value.trim();
                const repeatPassword = document.getElementById('repeat-password').value.trim();
                const email = $email;

                if (!password || !repeatPassword) {
                    alert('Password fields cannot be empty!');
                    return;
                }

                if (password.length < 4 || password.length > 15) {
                    alert('Password length must be between 4 and 15 characters!');
                    return;
                }

                if (password !== repeatPassword) {
                    alert('Passwords do not match!');
                    return;
                }


                const data = {
                    password: password,
                    email: email
                };

                fetch("$submitUrl", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                })
                    .then(response => response.json())
                    .then(data => {
                        console.log('Success:', data);
                        alert('Password reset successful!');
                    })
                    .catch((error) => {
                        console.error('Error:', error);
                        alert('An error occurred while resetting the password. Please try again.');
                    });
            }
        </script>
    </body>

    </html>
""".trimIndent()
}