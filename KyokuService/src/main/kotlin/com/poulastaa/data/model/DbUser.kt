package com.poulastaa.data.model

import com.poulastaa.domain.repository.users.EmailAuthUserRepository
import com.poulastaa.domain.repository.users.GoogleAuthUserRepository
import com.poulastaa.domain.repository.users.PasskeyAuthUserRepository

data class DbUser(
    val emailUser: EmailAuthUserRepository,
    val googleUser: GoogleAuthUserRepository,
    val passekyUser: PasskeyAuthUserRepository
)
