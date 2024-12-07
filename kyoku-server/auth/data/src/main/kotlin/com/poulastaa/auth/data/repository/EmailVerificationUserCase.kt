package com.poulastaa.auth.data.repository

import java.net.InetAddress

class EmailVerificationUserCase {
    suspend fun validateEmail(email: String): Boolean {
        if (!email.isValidEmail() || !email.isValidDomain()) return false


        // todo make api call
        return true
    }

    private fun String.isValidEmail(): Boolean =
        this.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex())

    private fun String.isValidDomain(): Boolean = try {
        InetAddress.getByName(this.substringAfter("@")) != null
    } catch (_: Exception) {
        false
    }
}