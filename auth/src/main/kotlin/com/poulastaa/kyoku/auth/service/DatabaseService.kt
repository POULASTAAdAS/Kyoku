package com.poulastaa.kyoku.auth.service

import com.poulastaa.kyoku.auth.model.dto.DtoUser
import com.poulastaa.kyoku.auth.utils.Email
import org.springframework.stereotype.Service

@Service
class DatabaseService {
    fun getUserByEmail(email: Email): DtoUser? = null
}