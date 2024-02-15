package com.poulastaa.data.model

import com.poulastaa.domain.repository.user_db.EmailAuthUserRepository
import com.poulastaa.domain.repository.user_db.GoogleAuthUserRepository
import com.poulastaa.domain.repository.user_db.PasskeyAuthUserRepository

data class DbUser(
    val emailUser: EmailAuthUserRepository,
    val googleUser: GoogleAuthUserRepository,
    val passekyUser: PasskeyAuthUserRepository
)
