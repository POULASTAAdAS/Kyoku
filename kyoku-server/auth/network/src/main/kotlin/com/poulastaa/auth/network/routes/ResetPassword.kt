package com.poulastaa.auth.network.routes

import com.poulastaa.auth.domain.model.ResetPasswordStatusDto
import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.auth.network.routes.utils.responseVerificationMailHtml
import com.poulastaa.core.domain.model.EndPoints
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.resetPassword(
    repo: AuthRepository,
) {
    route(EndPoints.ResetPassword.route) {
        get {
            val token = call.request.queryParameters["token"]
                ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

            val status = repo.verifyResetPasswordRequest(token)

            when (status.second) {
                ResetPasswordStatusDto.TOKEN_VALID -> resetPasswordPage(repo.getSubmitNewPasswordToken(status.first))
                ResetPasswordStatusDto.TOKEN_USED -> responseVerificationMailHtml(
                    headingColor = "#e65353",
                    heading = "Error",
                    title = "Link Already Used",
                    content1 = "The link has been used.",
                    content2 = "Please try again."
                )

                ResetPasswordStatusDto.TOKEN_EXPIRED -> responseVerificationMailHtml(
                    headingColor = "#e65353",
                    heading = "Error",
                    title = "Link Expired",
                    content1 = "The Link has expired",
                    content2 = "Please try again."
                )

                ResetPasswordStatusDto.SERVER_ERROR -> responseVerificationMailHtml(
                    headingColor = "#e65353",
                    heading = "Authentication Failed",
                    title = "Error Occurred",
                    content1 = "Opp's Something went wrong.",
                    content2 = "Please try again."
                )
            }.let {
                call.respondText(
                    text = it,
                    contentType = ContentType.Text.Html
                )
            }
        }
    }
}

private fun resetPasswordPage(
    token: String,
): String {
    val url = System.getenv("BASE_URL")

    val eyeOnUrl = "$url/images/eye_on.png"
    val eyeOffUrl = "$url/images/eye_off.png"
    val appLogo = "$url/images/app_logo.png"
    val submitUrl = "$url${EndPoints.SubmitNewPassword.route}"

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
            background: linear-gradient(to top, black, #1A3A38);
            font-family: Arial, sans-serif;
        }

            .container {
                text-align: center;
                color: #fff;
                margin: 2rem;
            }
    
            .logo {
                margin-bottom: 20px;
            }
    
            .logo img {
                width: 20vw;
                max-width: 150px;
                height: auto;
                border-radius: 10px;
            }
    
            .form {
                display: flex;
                flex-direction: column;
                align-items: center;
            }
    
            .input-group {
                position: relative;
                margin-bottom: 15px;
                width: 100%;
                max-width: 300px;
            }
    
            .input-group img {
                width: 2rem;
                position: absolute;
                right: 10px;
                top: 50%;
                transform: translateY(-50%);
                cursor: pointer;
            }
    
            .input-group input {
                width: 100%;
                padding: 10px;
                border: none;
                border-radius: 5px;
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
                font-size: 0.9rem;
                color: #09BEAA;
                background: linear-gradient(to top, #112423, #102524);
                padding: 0 5px;
            }
    
            button {
                width: 100%;
                max-width: 300px;
                padding: 10px;
                background-color: #008f8f;
                border: none;
                border-radius: 5px;
                font-size: 1rem;
                cursor: pointer;
                color: white;
                margin-top: 3rem;
                letter-spacing: 2px;
                font-weight: bold;
            }
    
            button:hover {
                background-color: #09BEAA;
            }
    
            .info {
                font-size: 1rem;
                color: #ffffff;
                margin-top: 0.7rem;
            }

            @media (max-width: 600px) {
                .input-group input {
                    font-size: 1rem;
                }
    
                .input-group label {
                    font-size: 1rem;
                }
    
                .info {
                    font-size: 0.9rem;
                }
    
                button {
                    font-size: 0.9rem;
                }
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
                const isPasswordVisible = passwordField.type === 'password';
                passwordField.type = isPasswordVisible ? 'text' : 'password';
                toggleImg.src = isPasswordVisible ? "$eyeOffUrl" : "$eyeOnUrl";
            }

            function sendPassword() {
                const password = document.getElementById('password').value.trim();
                const repeatPassword = document.getElementById('repeat-password').value.trim();
                const token = "$token";

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
                    token: token
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

                        if (data.status === "SAME_PASSWORD") {
                            alert('New password cannot be the same as the current password!');
                        } else {
                            window.location.href = data.successUrl;
                        }
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