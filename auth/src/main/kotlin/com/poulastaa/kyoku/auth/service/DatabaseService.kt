package com.poulastaa.kyoku.auth.service

import com.poulastaa.kyoku.auth.model.dto.DtoUser
import com.poulastaa.kyoku.auth.model.dto.UserType
import com.poulastaa.kyoku.auth.utils.Email
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DatabaseService(

) {
    fun getUserByEmail(email: Email, type: UserType): DtoUser? = null

    fun createUser(user: DtoUser): DtoUser = DtoUser(
        id = 1,
        user.username,
        user.email,
        user.passwordHash,
        user.code,
        user.type,
        user.profileUrl,
        user.lastUpdated,
    )
}