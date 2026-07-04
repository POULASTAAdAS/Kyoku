package com.poulastaa.auth.domain

interface AuthLocalDatasource {
    suspend fun saveUser()
}