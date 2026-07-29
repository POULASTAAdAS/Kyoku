package com.poulastaa.auth.network

import com.poulastaa.auth.domain.GoogleTokenExchange
import com.poulastaa.common.network.platformHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.bodyAsText
import io.ktor.http.parameters
import org.koin.core.module.Module
import org.koin.dsl.module

private const val TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token"

val googleTokenExchangeModule: Module = module {
    single<GoogleTokenExchange> { KtorGoogleTokenExchange() }
}

private class KtorGoogleTokenExchange(
    private val client: HttpClient = platformHttpClient(),
) : GoogleTokenExchange {
    override suspend fun exchange(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String,
        verifier: String,
    ): String {
        val response = client.submitForm(
            url = TOKEN_ENDPOINT,
            formParameters = parameters {
                append("client_id", clientId)
                append("client_secret", clientSecret)
                append("code", code)
                append("code_verifier", verifier)
                append("grant_type", "authorization_code")
                append("redirect_uri", redirectUri)
            },
        )

        if (response.status.value !in 200..299) {
            error("Google token exchange failed with HTTP ${response.status.value}")
        }

        return response.bodyAsText().jsonString("id_token")
            ?: error("Google ID token is missing")
    }
}

private fun String.jsonString(name: String): String? =
    Regex("\"${Regex.escape(name)}\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"")
        .find(this)
        ?.groupValues
        ?.getOrNull(1)
        ?.replace("\\\"", "\"")
        ?.replace("\\\\", "\\")
