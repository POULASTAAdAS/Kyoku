package com.poulastaa.kyoku.notification.utils

private enum class Social(val link: String) {
    TWITTER("https://x.com/PoulastaaD22643"),
    LINKEDIN("https://www.linkedin.com/in/poulastaa-das-7a5332235/"),
    MEDIUM("https://poulastaa.medium.com/"),
    GITHUB("https://github.com/POULASTAAdAS"),
}

fun generateVerificationMailContent(
    token: JWTToken,
    username: Username,
    endpoint: String,
    devMail: Email,
) = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta name="x-apple-disable-message-reformatting" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <title>Verify Your Email Address - Kyoku</title>
        <!--[if mso]>
        <noscript>
            <xml>
                <o:OfficeDocumentSettings>
                    <o:PixelsPerInch>96</o:PixelsPerInch>
                </o:OfficeDocumentSettings>
            </xml>
        </noscript>
        <![endif]-->
        <style>
            /* Reset styles */
            body, table, td, a { -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; }
            table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; }
            img { -ms-interpolation-mode: bicubic; border: 0; outline: none; text-decoration: none; }

            body {
                margin: 0 !important;
                padding: 0 !important;
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                background-color: #f5f5f5;
                color: #2c3e50;
                line-height: 1.6;
                width: 100% !important;
                min-width: 100% !important;
            }

            .email-wrapper {
                width: 100%;
                background-color: #f5f5f5;
                padding: 40px 0;
            }

            .email-container {
                max-width: 600px;
                margin: 0 auto;
                background-color: #ffffff;
                border-radius: 16px;
                overflow: hidden;
                box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
            }

            .header {
                background: linear-gradient(135deg, #F2C94C 0%, #E6B82C 100%);
                padding: 50px 30px;
                text-align: center;
                position: relative;
            }

            .header::after {
                content: '';
                position: absolute;
                bottom: -2px;
                left: 0;
                right: 0;
                height: 4px;
                background: linear-gradient(90deg, transparent, rgba(255,255,255,0.5), transparent);
            }

            .logo-container {
                display: inline-block;
                width: 80px;
                height: 80px;
                background: #2c3e50;
                border-radius: 12px;
                transform: rotate(45deg) !important;
                margin-bottom: 30px;
                box-shadow: 0 8px 24px rgba(44, 62, 80, 0.2);
            }

            .logo-text {
                display: inline-block;
                transform: rotate(-45deg) !important;
                font-size: 48px;
                font-weight: 700;
                color: #F2C94C;
                line-height: 80px;
                letter-spacing: -2px;
            }

            .header-title {
                color: #2c3e50 !important;
                font-size: 32px;
                font-weight: 700;
                margin: 0;
                letter-spacing: -0.5px;
                text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }

            .header-subtitle {
                color: #2c3e50 !important;
                font-size: 16px;
                font-weight: 400;
                margin: 10px 0 0 0;
                opacity: 0.9;
            }

            .content {
                padding: 50px 40px;
            }

            .greeting {
                font-size: 20px;
                font-weight: 600;
                color: #2c3e50;
                margin-bottom: 25px;
            }

            .message {
                font-size: 16px;
                color: #5a6c7d;
                margin-bottom: 35px;
                line-height: 1.8;
            }

            .highlight {
                color: #E6B82C;
                font-weight: 600;
            }

            .cta-container {
                text-align: center;
                margin: 45px 0;
            }

            .cta-button {
                display: inline-block;
                padding: 18px 40px;
                background: linear-gradient(135deg, #F2C94C 0%, #E6B82C 100%);
                color: #2c3e50 !important;
                text-decoration: none;
                border-radius: 8px;
                font-weight: 700;
                font-size: 16px;
                letter-spacing: 1px;
                transition: all 0.3s ease;
                box-shadow: 0 6px 20px rgba(242, 201, 76, 0.4);
                text-transform: uppercase;
            }

            .cta-button:hover {
                transform: translateY(-3px);
                box-shadow: 0 8px 30px rgba(242, 201, 76, 0.5);
            }

            .divider {
                width: 60px;
                height: 3px;
                background: linear-gradient(90deg, #F2C94C, #E6B82C);
                margin: 35px auto;
                border-radius: 2px;
            }

            .alternative-section {
                margin-top: 40px;
                padding: 25px;
                background: linear-gradient(135deg, #fafafa 0%, #f5f5f5 100%);
                border-radius: 12px;
                border: 2px solid #F2C94C;
            }

            .alternative-section-title {
                margin: 0 0 12px 0;
                font-size: 14px;
                font-weight: 600;
                color: #2c3e50;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .alternative-section p {
                margin: 0 0 10px 0;
                font-size: 14px;
                color: #5a6c7d;
            }

            .alternative-section a {
                color: #E6B82C;
                word-break: break-all;
                font-size: 13px;
                font-weight: 500;
                text-decoration: none;
            }

            .alternative-section a:hover {
                text-decoration: underline;
            }

            .security-badge {
                display: inline-block;
                margin-top: 30px;
                padding: 20px;
                background: linear-gradient(135deg, #fff8e1 0%, #fff3cd 100%);
                border: 2px solid #F2C94C;
                border-radius: 12px;
                width: 100%;
                box-sizing: border-box;
            }

            .security-icon {
                display: inline-block;
                width: 24px;
                height: 24px;
                background: #F2C94C;
                border-radius: 50%;
                margin-right: 10px;
                vertical-align: middle;
                text-align: center;
                line-height: 24px;
                font-weight: bold;
                color: #2c3e50;
            }

            .security-badge p {
                margin: 0;
                font-size: 14px;
                color: #856404;
                line-height: 1.6;
            }

            .footer {
                background: linear-gradient(135deg, #fafafa 0%, #f5f5f5 100%);
                padding: 40px 30px;
                text-align: center;
                border-top: 1px solid #e9ecef;
            }

            .footer-links {
                margin-bottom: 25px;
            }

            .footer-links a {
                color: #E6B82C;
                text-decoration: none;
                font-size: 14px;
                margin: 0 15px;
                font-weight: 500;
                transition: color 0.3s ease;
            }

            .footer-links a:hover {
                color: #F2C94C;
                text-decoration: underline;
            }

            .footer p {
                margin: 8px 0;
                font-size: 14px;
                color: #5a6c7d;
            }

            .footer .company-name {
                font-weight: 700;
                color: #2c3e50;
                font-size: 16px;
            }

            .social-icons {
                margin: 20px 0;
            }

            .social-icon {
                display: inline-block;
                width: 36px;
                height: 36px;
                background: #2c3e50;
                border-radius: 50%;
                margin: 0 8px;
                line-height: 36px;
                text-align: center;
                color: #F2C94C;
                text-decoration: none;
                transition: all 0.3s ease;
            }

            .social-icon:hover {
                background: #F2C94C;
                color: #2c3e50;
                transform: translateY(-3px);
            }

            .copyright {
                margin-top: 20px;
                padding-top: 20px;
                border-top: 1px solid #e9ecef;
                font-size: 12px;
                color: #8898aa;
            }

            /* Mobile Responsive */
            @media only screen and (max-width: 600px) {
                .email-wrapper {
                    padding: 20px 10px;
                }

                .header {
                    padding: 40px 20px;
                }

                .header-title {
                    font-size: 26px;
                }

                .content {
                    padding: 35px 25px;
                }

                .greeting {
                    font-size: 18px;
                }

                .cta-button {
                    padding: 16px 32px;
                    font-size: 15px;
                    color: #2c3e50 !important;
                }

                .footer {
                    padding: 30px 20px;
                }

                .footer-links a {
                    display: block;
                    margin: 10px 0;
                }
            }

            /* Dark mode support */
            @media (prefers-color-scheme: dark) {
                body {
                    background-color: #1a1a1a !important;
                }
                .email-wrapper {
                    background-color: #1a1a1a !important;
                }
            }
        </style>
    </head>
    <body>
    <div class="email-wrapper">
        <div class="email-container">
            <!-- Header Section -->
            <div class="header">
                <div class="logo-container">
                    <span class="logo-text">K</span>
                </div>
                <h1 class="header-title">Welcome to Kyoku</h1>
                <p class="header-subtitle">Your journey begins here</p>
            </div>

            <!-- Main Content -->
            <div class="content">
                <div class="greeting">
                    Hello ${username.uppercase()} 👋
                </div>

                <p class="message">
                    We're thrilled to welcome you to <span class="highlight">Kyoku</span>!
                    Your account is just one step away from being fully activated.
                    Please verify your email address to start your journey and listen to thousands of songs for free.
                </p>

                <div class="cta-container">
                    <a href="$endpoint?token=$token" class="cta-button">
                        Verify My Email
                    </a>
                </div>

                <div class="divider"></div>

                <div class="alternative-section">
                    <p class="alternative-section-title">Can't click the button?</p>
                    <p>No worries! Copy and paste this link into your browser:</p>
                    <a href="$endpoint?token=$token">
                        $endpoint?token=$token
                    </a>
                </div>

                <div class="security-badge">
                    <p>
                        <span class="security-icon">🔒</span>
                        <strong>Security Notice:</strong> This verification link expires in <strong>10 minutes</strong> for your protection.
                        If you didn't create an account with Kyoku, please disregard this email or contact our support team immediately.
                    </p>
                </div>
            </div>

            <!-- Footer Section -->
            <div class="footer">
                <div class="social-icons">
                    <a href="${Social.TWITTER.link}" class="social-icon" title="Twitter">𝕏</a>
                    <a href="${Social.LINKEDIN.link}" class="social-icon" title="LinkedIn">in</a>
                    <a href="${Social.MEDIUM.link}" class="social-icon" title="Medium">Md</a>
                    <a href="${Social.GITHUB.link}" class="social-icon" title="GitHub">Gh</a>
                </div>

                <p>Questions? We're here to help!</p>
                <p>
                    <a href="mailto:$devMail" style="color: #E6B82C; font-weight: 600; text-decoration: none;">
                        $devMail
                    </a>
                </p>

                <p style="margin-top: 25px;">
                    Best regards,<br>
                    <span class="company-name">Team Kyoku</span>
                </p>

                <div class="copyright">
                    © 2024 Kyoku. All rights reserved.<br>
                    Made with ❤️ by Team Kyoku
                </div>
            </div>
        </div>
    </div>
    </body>
    </html>
""".trimIndent()

fun generateWelcomeMailContent(
    username: Username,
    devMail: Email,
    appUrl: String = "http://kyoku.poulastaa.shop:8080",
) = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <meta name="x-apple-disable-message-reformatting"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Welcome to Kyoku - Let the Music Begin!</title>
        <!--[if mso]>
        <noscript>
            <xml>
                <o:OfficeDocumentSettings>
                    <o:PixelsPerInch>96</o:PixelsPerInch>
                </o:OfficeDocumentSettings>
            </xml>
        </noscript>
        <![endif]-->
        <style>
            body, table, td, a {
                -webkit-text-size-adjust: 100%;
                -ms-text-size-adjust: 100%;
            }

            table, td {
                mso-table-lspace: 0;
                mso-table-rspace: 0;
            }

            img {
                -ms-interpolation-mode: bicubic;
                border: 0;
                outline: none;
                text-decoration: none;
            }

            body {
                margin: 0 !important;
                padding: 0 !important;
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                background-color: #f5f5f5;
                color: #2c3e50;
                line-height: 1.6;
                width: 100% !important;
                min-width: 100% !important;
            }

            .email-wrapper {
                width: 100%;
                background-color: #f5f5f5;
                padding: 40px 0;
            }

            .email-container {
                max-width: 600px;
                margin: 0 auto;
                background-color: #ffffff;
                border-radius: 16px;
                overflow: hidden;
                box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
            }

            .header {
                background: linear-gradient(135deg, #F2C94C 0%, #E6B82C 100%);
                padding: 50px 30px;
                text-align: center;
                position: relative;
            }

            .header::after {
                content: '';
                position: absolute;
                bottom: -2px;
                left: 0;
                right: 0;
                height: 4px;
                background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.5), transparent);
            }

            .logo-container {
                display: inline-block;
                width: 80px;
                height: 80px;
                background: #2c3e50;
                border-radius: 12px;
                transform: rotate(45deg);
                margin-bottom: 30px;
                box-shadow: 0 8px 24px rgba(44, 62, 80, 0.2);
            }

            .logo-text {
                display: inline-block;
                transform: rotate(-45deg) !important;
                font-size: 48px;
                font-weight: 700;
                color: #F2C94C;
                line-height: 80px;
                letter-spacing: -2px;
            }

            .header-title {
                color: #2c3e50 !important;
                font-size: 32px;
                font-weight: 700;
                margin: 0;
                letter-spacing: -0.5px;
                text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }

            .header-subtitle {
                color: #2c3e50 !important;
                font-size: 16px;
                font-weight: 400;
                margin: 10px 0 0 0;
                opacity: 0.9;
            }

            .content {
                padding: 50px 40px;
            }

            .greeting {
                font-size: 20px;
                font-weight: 600;
                color: #2c3e50;
                margin-bottom: 25px;
            }

            .message {
                font-size: 16px;
                color: #5a6c7d;
                margin-bottom: 35px;
                line-height: 1.8;
            }

            .highlight {
                color: #E6B82C;
                font-weight: 600;
            }

            .cta-container {
                text-align: center;
                margin: 45px 0;
            }

            .cta-button {
                display: inline-block;
                padding: 18px 40px;
                background: linear-gradient(135deg, #F2C94C 0%, #E6B82C 100%);
                color: #2c3e50 !important;
                text-decoration: none;
                border-radius: 8px;
                font-weight: 700;
                font-size: 16px;
                letter-spacing: 1px;
                transition: all 0.3s ease;
                box-shadow: 0 6px 20px rgba(242, 201, 76, 0.4);
                text-transform: uppercase;
            }

            .cta-button:hover {
                transform: translateY(-3px);
                box-shadow: 0 8px 30px rgba(242, 201, 76, 0.5);
            }

            .tips-section p {
                margin: 10px 0;
                font-size: 14px;
                color: #856404;
                line-height: 1.6;
            }

            .footer {
                background: linear-gradient(135deg, #fafafa 0%, #f5f5f5 100%);
                padding: 40px 30px;
                text-align: center;
                border-top: 1px solid #e9ecef;
            }

            .footer-links a {
                color: #E6B82C;
                text-decoration: none;
                font-size: 14px;
                margin: 0 15px;
                font-weight: 500;
                transition: color 0.3s ease;
            }

            .footer-links a:hover {
                color: #F2C94C;
                text-decoration: underline;
            }

            .footer p {
                margin: 8px 0;
                font-size: 14px;
                color: #5a6c7d;
            }

            .footer .company-name {
                font-weight: 700;
                color: #2c3e50;
                font-size: 16px;
            }

            .social-icons {
                margin: 20px 0;
            }

            .social-icon {
                display: inline-block;
                width: 36px;
                height: 36px;
                background: #2c3e50;
                border-radius: 50%;
                margin: 0 8px;
                line-height: 36px;
                text-align: center;
                color: #F2C94C;
                text-decoration: none;
                transition: all 0.3s ease;
            }

            .social-icon:hover {
                background: #F2C94C;
                color: #2c3e50;
                transform: translateY(-3px);
            }

            .copyright {
                margin-top: 20px;
                padding-top: 20px;
                border-top: 1px solid #e9ecef;
                font-size: 12px;
                color: #8898aa;
            }

            /* Mobile Responsive */
            @media only screen and (max-width: 600px) {
                .email-wrapper {
                    padding: 20px 10px;
                }

                .header {
                    padding: 40px 20px;
                }

                .header-title {
                    font-size: 26px;
                }

                .content {
                    padding: 35px 25px;
                }

                .greeting {
                    font-size: 18px;
                }

                .cta-button {
                    padding: 16px 32px;
                    font-size: 15px;
                    color: #2c3e50 !important;
                }

                .footer {
                    padding: 30px 20px;
                }

                .footer-links a {
                    display: block;
                    margin: 10px 0;
                }
            }

            /* Dark mode support */
            @media (prefers-color-scheme: dark) {
                body {
                    background-color: #1a1a1a !important;
                }

                .email-wrapper {
                    background-color: #1a1a1a !important;
                }
            }
        </style>
    </head>
    <body>
    <div class="email-wrapper">
        <div class="email-container">
            <!-- Header Section -->
            <div class="header">
                <div class="logo-container">
                    <span class="logo-text">K</span>
                </div>
                <h1 class="header-title">Welcome to Kyoku!</h1>
                <p class="header-subtitle">Let the music begin 🎵</p>
            </div>

            <!-- Main Content -->
            <div class="content">
                <div class="greeting">
                    Hello ${username.uppercase()}! 🎉
                </div>

                <p class="message">
                    Congratulations! Your email has been successfully verified and your
                    <span class="highlight">Kyoku</span> account is now fully activated.
                    You're all set to explore thousands of songs, create playlists, and discover your new favorite tracks!
                </p>

                <div class="cta-container">
                    <a href="$appUrl" class="cta-button">
                        Start Listening Now
                    </a>
                </div>
            </div>

            <!-- Footer Section -->
            <div class="footer">
                <div class="social-icons">
                    <a href="${Social.TWITTER.link}" class="social-icon" title="Twitter">𝕏</a>
                    <a href="${Social.LINKEDIN.link}" class="social-icon" title="LinkedIn">in</a>
                    <a href="${Social.MEDIUM.link}" class="social-icon" title="Medium">Md</a>
                    <a href="${Social.GITHUB.link}" class="social-icon" title="GitHub">Gh</a></div>

                <p>Questions or feedback? We'd love to hear from you!</p>
                <p>
                    <a href="mailto:$devMail" style="color: #E6B82C; font-weight: 600; text-decoration: none;">
                        $devMail
                    </a>
                </p>

                <p style="margin-top: 25px;">
                    Enjoy the music,<br>
                    <span class="company-name">Team Kyoku</span>
                </p>

                <div class="copyright">
                    © 2025 Kyoku. All rights reserved.<br>
                    Made with ❤️ by Team Kyoku
                </div>
            </div>
        </div>
    </div>
    </body>
    </html>
""".trimIndent()

// TODO pull info from user or content service
fun generateWelcomeBackContent(
    devMail: Email,
    allLibrarySongs: Int,
    playlist: Int,
    totalHour: Int,
    appUrl: String = "http://kyoku.poulastaa.shop:8080",
) = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <meta name="color-scheme" content="light"/>
        <meta name="supported-color-schemes" content="light"/>
        <title>Welcome Back to Kyoku</title>
        <!--[if mso]>
        <noscript>
            <xml>
                <o:OfficeDocumentSettings>
                    <o:PixelsPerInch>96</o:PixelsPerInch>
                </o:OfficeDocumentSettings>
            </xml>
        </noscript>
        <![endif]-->
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            [data-ogsc] * {
                color-scheme: light !important;
            }

            .email-container {
                width: 100%;
                max-width: 600px;
                margin: 0 auto;
                background-color: #ffffff;
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Arial, sans-serif;
            }

            .header-section {
                background-color: #F2C94C;
                background-image: linear-gradient(135deg, #F2C94C 0%, #E6B82C 100%);
                padding: 40px 20px;
                text-align: center;
            }

            .logo-container {
                margin-bottom: 20px;
            }

            .logo-box {
                display: inline-block;
                width: 80px;
                height: 80px;
                background-color: #2c3e50;
                border-radius: 12px;
                text-align: center;
                line-height: 80px;
                transform: rotate(45deg);
            }

            .logo-letter {
                font-size: 48px;
                font-weight: 800;
                color: #F2C94C;
                display: inline-block;
                transform: rotate(-45deg);
            }

            .welcome-title {
                color: #2c3e50;
                font-size: 32px;
                font-weight: 700;
                margin: 0 0 10px 0;
                line-height: 1.2;
            }

            .welcome-subtitle {
                color: #2c3e50;
                font-size: 16px;
                margin: 0;
                opacity: 0.9;
            }

            .content-section {
                padding: 40px 20px;
                background-color: #ffffff;
                text-align: center;
            }

            .greeting {
                font-size: 22px;
                font-weight: 600;
                color: #2c3e50;
                margin: 0 0 20px 0;
            }

            .message-text {
                font-size: 16px;
                color: #5a6c7d;
                line-height: 1.6;
                margin: 0 0 30px 0;
                text-align: left;
            }

            .highlight-text {
                color: #E6B82C;
                font-weight: 600;
            }

            .stats-table {
                width: 100%;
                margin: 30px 0;
            }

            .stats-table td {
                padding: 20px 10px;
                text-align: center;
                vertical-align: top;
                width: 33.33%;
            }

            .stat-card {
                background-color: #fafafa;
                border-radius: 8px;
                padding: 20px;
                border: 1px solid #e9ecef;
                display: inline-block;
                width: 100%;
                max-width: 150px;
            }

            .stat-number {
                font-size: 28px;
                font-weight: 700;
                color: #F2C94C;
                margin: 0 0 5px 0;
                line-height: 1;
            }

            .stat-label {
                font-size: 12px;
                color: #5a6c7d;
                text-transform: uppercase;
                letter-spacing: 0.5px;
                margin: 0;
            }

            .cta-container {
                margin: 40px 0;
                text-align: center;
            }

            .cta-button {
                display: inline-block;
                padding: 16px 32px;
                background-color: #F2C94C;
                background-image: linear-gradient(135deg, #F2C94C 0%, #E6B82C 100%);
                color: #2c3e50 !important;
                text-decoration: none;
                border-radius: 8px;
                font-weight: 700;
                font-size: 16px;
                letter-spacing: 0.5px;
                text-transform: uppercase;
                border: none;
            }

            .footer-section {
                background-color: #fafafa;
                padding: 30px 20px;
                text-align: center;
                border-top: 1px solid #e9ecef;
            }

            .social-links {
                margin-bottom: 20px;
            }

            .social-link {
                display: inline-block;
                width: 36px;
                height: 36px;
                background-color: #2c3e50;
                border-radius: 50%;
                color: #F2C94C !important;
                text-decoration: none;
                font-weight: 700;
                font-size: 14px;
                line-height: 36px;
                text-align: center;
                margin: 0 5px;
            }

            .footer-text {
                color: #5a6c7d;
                font-size: 14px;
                margin: 5px 0;
                line-height: 1.4;
            }

            .footer-email {
                color: #E6B82C !important;
                font-weight: 600;
                text-decoration: none;
            }

            .footer-copyright {
                color: #8898aa;
                font-size: 12px;
                margin: 20px 0 0 0;
                padding-top: 20px;
                border-top: 1px solid #e9ecef;
                line-height: 1.4;
            }

            @media screen and (max-width: 480px) {
                .email-container {
                    width: 100% !important;
                }

                .header-section {
                    padding: 30px 15px !important;
                }

                .welcome-title {
                    font-size: 24px !important;
                }

                .content-section {
                    padding: 30px 15px !important;
                }

                .stats-table td {
                    display: block !important;
                    width: 100% !important;
                    margin-bottom: 10px;
                }

                .message-text {
                    text-align: center !important;
                }

                .cta-button {
                    display: block !important;
                    width: calc(100% - 40px) !important;
                }
            }

            @media (prefers-color-scheme: dark) {
                .email-container,
                .header-section,
                .content-section,
                .footer-section {
                    background-color: #ffffff !important;
                }

                .welcome-title,
                .greeting,
                .message-text,
                .footer-text {
                    color: #2c3e50 !important;
                }
            }
        </style>
    </head>
    <body style="margin: 0; padding: 20px; background-color: #f5f5f5; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Arial, sans-serif;">
    <table role="presentation" cellspacing="0" cellpadding="0" border="0" class="email-container" style="margin: 0 auto;">
        <tr>
            <td class="header-section">
                <div class="logo-container">
                    <div class="logo-box">
                        <span class="logo-letter">K</span>
                    </div>
                </div>
                <h1 class="welcome-title">Welcome Back to Kyoku!</h1>
                <p class="welcome-subtitle">Your musical journey continues 🎵</p>
            </td>
        </tr>

        <tr>
            <td class="content-section">
                <h2 class="greeting">Great to see you again! 👋</h2>

                <p class="message-text">
                    We've missed you! While you were away, we've added
                    <span class="highlight-text">thousands of new tracks</span> and exciting features
                    just for you. Your playlists are right where you left them, and we've got some
                    fresh recommendations based on your listening history.
                </p>

                <table role="presentation" cellspacing="0" cellpadding="0" border="0" class="stats-table">
                    <tr>
                        <td>
                            <div class="stat-card">
                                <div class="stat-number">$allLibrarySongs</div>
                                <div class="stat-label">Songs in Library</div>
                            </div>
                        </td>
                        <td>
                            <div class="stat-card">
                                <div class="stat-number">$playlist</div>
                                <div class="stat-label">Playlists</div>
                            </div>
                        </td>
                        <td>
                            <div class="stat-card">
                                <div class="stat-number">$totalHour</div>
                                <div class="stat-label">Hours Listened</div>
                            </div>
                        </td>
                    </tr>
                </table>

                <!-- CTA Button -->
                <div class="cta-container">
                    <a href="$appUrl" class="cta-button">Continue Listening</a>
                </div>
            </td>
        </tr>

        <tr>
            <td class="footer-section">
                <div class="social-links">
                    <a href="${Social.TWITTER.link}" class="social-link">𝕏</a>
                    <a href="${Social.LINKEDIN.link}" class="social-link">in</a>
                    <a href="${Social.MEDIUM.link}" class="social-link">Me</a>
                    <a href="${Social.GITHUB.link}" class="social-link">Gh</a>
                </div>

                <p class="footer-text">Need help? Contact us at</p>
                <p class="footer-text">
                    <a href="mailto:$devMail" class="footer-email">$devMail</a>
                </p>
                <p class="footer-text" style="font-weight: 600; color: #2c3e50;">Team Kyoku</p>

                <div class="footer-copyright">
                    © 2025 Kyoku. All rights reserved.<br>
                    Made with ❤️ for music lovers
                </div>
            </td>
        </tr>
    </table>
    </body>
    </html>
""".trimIndent()

fun generateForgotPasswordMailContent(
    code: String,
    username: Username,
    devMail: Email,
) = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="x-apple-disable-message-reformatting" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>Password Reset Request - Kyoku</title>
    <!--[if mso]>
    <noscript>
        <xml>
            <o:OfficeDocumentSettings>
                <o:PixelsPerInch>96</o:PixelsPerInch>
            </o:OfficeDocumentSettings>
        </xml>
    </noscript>
    <![endif]-->
    <style>
        /* Reset styles */
        body, table, td, a {
            -webkit-text-size-adjust: 100%;
            -ms-text-size-adjust: 100%;
        }

        table, td {
            mso-table-lspace: 0pt;
            mso-table-rspace: 0pt;
        }

        img {
            -ms-interpolation-mode: bicubic;
            border: 0;
            outline: none;
            text-decoration: none;
        }

        body {
            margin: 0 !important;
            padding: 0 !important;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
            background-color: #f5f5f5;
            color: #2c3e50;
            line-height: 1.6;
            width: 100% !important;
            min-width: 100% !important;
        }

        .email-wrapper {
            width: 100%;
            background-color: #f5f5f5;
            padding: 40px 0;
        }

        .email-container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #ffffff;
            border-radius: 16px;
            overflow: hidden;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
        }

        .header {
            background: linear-gradient(135deg, #F2C94C 0%, #E6B82C 100%);
            padding: 50px 30px;
            text-align: center;
            position: relative;
        }

        .header::after {
            content: '';
            position: absolute;
            bottom: -2px;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, transparent, rgba(255,255,255,0.5), transparent);
        }

        .logo-container {
            display: inline-block;
            width: 80px;
            height: 80px;
            background: #2c3e50;
            border-radius: 12px;
            transform: rotate(45deg);
            margin-bottom: 30px;
            box-shadow: 0 8px 24px rgba(44, 62, 80, 0.2);
        }

        .logo-text {
            display: inline-block;
            transform: rotate(-45deg);
            font-size: 48px;
            font-weight: 700;
            color: #F2C94C;
            line-height: 80px;
            letter-spacing: -2px;
        }

        .header-title {
            color: #2c3e50 !important;
            font-size: 32px;
            font-weight: 700;
            margin: 0;
            letter-spacing: -0.5px;
            text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .header-subtitle {
            color: #2c3e50 !important;
            font-size: 16px;
            font-weight: 400;
            margin: 10px 0 0 0;
            opacity: 0.9;
        }

        .content {
            padding: 50px 40px;
        }

        .greeting {
            font-size: 20px;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 25px;
        }

        .message {
            font-size: 16px;
            color: #5a6c7d;
            margin-bottom: 35px;
            line-height: 1.8;
        }

        .highlight {
            color: #E6B82C;
            font-weight: 600;
        }

        .code-container {
            text-align: center;
            margin: 45px 0;
            padding: 40px 20px;
            background: linear-gradient(135deg, #fafafa 0%, #f5f5f5 100%);
            border-radius: 16px;
            border: 3px solid #F2C94C;
        }

        .code-label {
            font-size: 14px;
            font-weight: 600;
            color: #5a6c7d;
            text-transform: uppercase;
            letter-spacing: 1px;
            margin-bottom: 20px;
        }

        .verification-code {
            display: inline-flex;
            gap: 16px;
            justify-content: center;
            margin: 20px 0;
        }

        .code-digit {
            display: inline-block;
            width: 60px;
            height: 70px;
            background: linear-gradient(135deg, #F2C94C 0%, #E6B82C 100%);
            color: #2c3e50;
            font-size: 36px;
            font-weight: 700;
            line-height: 70px;
            text-align: center;
            border-radius: 12px;
            box-shadow: 0 6px 20px rgba(242, 201, 76, 0.3);
            letter-spacing: 2px;
        }

        .selectable-code {
            display: inline-block;
            padding: 12px 24px;
            background: rgba(242, 201, 76, 0.15);
            border-radius: 8px;
            font-family: 'Courier New', monospace;
            font-size: 24px;
            font-weight: 700;
            color: #2c3e50;
            letter-spacing: 6px;
            user-select: all;
            margin: 20px 0 10px 0;
            border: 2px dashed #E6B82C;
        }

        .copy-hint {
            font-size: 13px;
            color: #5a6c7d;
            font-style: italic;
            margin-top: 10px;
        }

        .divider {
            width: 60px;
            height: 3px;
            background: linear-gradient(90deg, #F2C94C, #E6B82C);
            margin: 35px auto;
            border-radius: 2px;
        }

        .security-badge {
            display: block;
            margin-top: 30px;
            padding: 20px;
            background: linear-gradient(135deg, #fff3cd 0%, #ffe4a8 100%);
            border: 2px solid #E6B82C;
            border-radius: 12px;
            width: 100%;
            box-sizing: border-box;
        }

        .security-icon {
            display: inline-block;
            width: 24px;
            height: 24px;
            background: #E6B82C;
            border-radius: 50%;
            margin-right: 10px;
            vertical-align: middle;
            text-align: center;
            line-height: 24px;
            font-weight: bold;
            color: #2c3e50;
        }

        .security-badge p {
            margin: 0;
            font-size: 14px;
            color: #856404;
            line-height: 1.6;
        }

        .warning-section {
            margin-top: 30px;
            padding: 20px;
            background: linear-gradient(135deg, #fff5f5 0%, #ffe5e5 100%);
            border-left: 4px solid #e74c3c;
            border-radius: 8px;
        }

        .warning-section p {
            margin: 0;
            font-size: 14px;
            color: #c0392b;
            line-height: 1.6;
        }

        .footer {
            background: linear-gradient(135deg, #fafafa 0%, #f5f5f5 100%);
            padding: 40px 30px;
            text-align: center;
            border-top: 1px solid #e9ecef;
        }

        .footer p {
            margin: 8px 0;
            font-size: 14px;
            color: #5a6c7d;
        }

        .footer .company-name {
            font-weight: 700;
            color: #2c3e50;
            font-size: 16px;
        }

        .social-icons {
            margin: 20px 0;
        }

        .social-icon {
            display: inline-block;
            width: 36px;
            height: 36px;
            background: #2c3e50;
            border-radius: 50%;
            margin: 0 8px;
            line-height: 36px;
            text-align: center;
            color: #F2C94C;
            text-decoration: none;
            transition: all 0.3s ease;
        }

        .social-icon:hover {
            background: #F2C94C;
            color: #2c3e50;
            transform: translateY(-3px);
        }

        .copyright {
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #e9ecef;
            font-size: 12px;
            color: #8898aa;
        }

        /* Mobile Responsive */
        @media only screen and (max-width: 600px) {
            .email-wrapper {
                padding: 20px 10px;
            }

            .header {
                padding: 40px 20px;
            }

            .header-title {
                font-size: 26px;
            }

            .content {
                padding: 35px 25px;
            }

            .greeting {
                font-size: 18px;
            }

            .code-digit {
                width: 50px;
                height: 60px;
                font-size: 30px;
                line-height: 60px;
            }

            .verification-code {
                gap: 10px;
            }

            .selectable-code {
                font-size: 20px;
                letter-spacing: 4px;
                padding: 10px 16px;
            }

            .footer {
                padding: 30px 20px;
            }
        }

        /* Dark mode support */
        @media (prefers-color-scheme: dark) {
            body {
                background-color: #1a1a1a !important;
            }
            .email-wrapper {
                background-color: #1a1a1a !important;
            }
        }
    </style>
</head>
<body>
    <div class="email-wrapper">
        <div class="email-container">
            <!-- Header Section -->
            <div class="header">
                <div class="logo-container">
                    <span class="logo-text">K</span>
                </div>
                <h1 class="header-title">Password Reset Request</h1>
                <p class="header-subtitle">Secure account recovery</p>
            </div>

            <!-- Main Content -->
            <div class="content">
                <div class="greeting">
                    Hello ${username.uppercase()} 👋
                </div>

                <p class="message">
                    We received a request to reset your <span class="highlight">Kyoku</span> account password.
                    Use the verification code below to complete the password reset process.
                    If you didn't request this, you can safely ignore this email.
                </p>

                <div class="code-container">
                    <div class="code-label">Your Verification Code</div>
                    <div class="verification-code">
                        <span class="code-digit">${code[0]}</span>
                        <span class="code-digit">${code[1]}</span>
                        <span class="code-digit">${code[2]}</span>
                        <span class="code-digit">${code[3]}</span>
                        <span class="code-digit">${code[4]}</span>
                    </div>
                    <br>
                    <div class="selectable-code">$code</div>
                    <div class="copy-hint">Click to select and copy</div>
                </div>

                <div class="divider"></div>

                <div class="security-badge">
                    <p>
                        <span class="security-icon">🔒</span>
                        <strong>Security Notice:</strong> This verification code expires in <strong>10 minutes</strong> for your security.
                        The code can only be used once and will be invalidated after successful password reset.
                    </p>
                </div>

                <div class="warning-section">
                    <p>
                        <strong>⚠️ Didn't request a password reset?</strong><br>
                        If you did not initiate this request, your account may be at risk. Please contact our support team immediately at 
                        <a href="mailto:$devMail" style="color: #c0392b; font-weight: 600;">$devMail</a> and consider changing your password.
                    </p>
                </div>
            </div>

            <!-- Footer Section -->
            <div class="footer">
                <div class="social-icons">
                    <a href="${Social.TWITTER.link}" class="social-icon" title="Twitter">𝕏</a>
                    <a href="${Social.LINKEDIN.link}" class="social-icon" title="LinkedIn">in</a>
                    <a href="${Social.MEDIUM.link}" class="social-icon" title="Medium">Md</a>
                    <a href="${Social.GITHUB.link}" class="social-icon" title="GitHub">Gh</a>
                </div>

                <p>Questions or concerns? We're here to help!</p>
                <p>
                    <a href="mailto:$devMail" style="color: #E6B82C; font-weight: 600; text-decoration: none;">
                        $devMail
                    </a>
                </p>

                <p style="margin-top: 25px;">
                    Best regards,<br>
                    <span class="company-name">Team Kyoku</span>
                </p>

                <div class="copyright">
                    © 2024 Kyoku. All rights reserved.<br>
                    Made with ❤️ by Team Kyoku
                </div>
            </div>
        </div>
    </div>
</body>
</html>
""".trimIndent()