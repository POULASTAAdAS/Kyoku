package com.poulastaa.auth.ui.components

import org.koin.core.module.Module

interface GoogleAuthWrapper {
    fun startGoogleAuth()
    var onResult: ((GoogleAuthResult) -> Unit)?
}

sealed interface GoogleAuthResult {
    data class Success(val token: String) : GoogleAuthResult
    data object Canceled : GoogleAuthResult
    data class Error(val exception: Exception) : GoogleAuthResult
}

expect val googleAuthModule: Module
