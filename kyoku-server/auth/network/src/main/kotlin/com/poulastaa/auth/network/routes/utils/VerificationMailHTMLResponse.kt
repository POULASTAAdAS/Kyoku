package com.poulastaa.auth.network.routes.utils

fun responseVerificationMailHtml(
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