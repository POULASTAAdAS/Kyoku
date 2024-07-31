package com.poulastaa.domain.model

data class UserResult(
    val id: Long = -1,
    val email: String = "",
    val userName: String = "",
    val profilePic: String = "",
    val password: String = "",
    val bDate: Long = -1,
    val countryId: Int = -1,
    val userType: UserType,
)
