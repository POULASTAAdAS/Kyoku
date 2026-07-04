package com.poulastaa.auth.network

import com.poulastaa.auth.domain.AuthRemoteDatasource
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single(binds = [AuthRemoteDatasource::class])
class KtorAuthDatasource(
    private val client: HttpClient,
) : AuthRemoteDatasource {
    override suspend fun signIn(email: String, password: String) = Unit
}
