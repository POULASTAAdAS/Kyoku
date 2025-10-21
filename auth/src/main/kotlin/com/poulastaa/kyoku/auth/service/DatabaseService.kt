package com.poulastaa.kyoku.auth.service

import com.poulastaa.kyoku.auth.database.entity.EntityJWTToken
import com.poulastaa.kyoku.auth.database.entity.EntityUser
import com.poulastaa.kyoku.auth.database.repository.CountryDataSource
import com.poulastaa.kyoku.auth.database.repository.UserDataSource
import com.poulastaa.kyoku.auth.database.repository.UserJWTTokenDataSource
import com.poulastaa.kyoku.auth.database.repository.UserTypeDataSource
import com.poulastaa.kyoku.auth.model.dto.DtoUser
import com.poulastaa.kyoku.auth.model.dto.UserType
import com.poulastaa.kyoku.auth.utils.Email
import com.poulastaa.kyoku.auth.utils.JWTToken
import com.poulastaa.kyoku.auth.utils.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DatabaseService(
    private val user: UserDataSource,
    private val country: CountryDataSource,
    private val jwt: UserJWTTokenDataSource,
    private val userType: UserTypeDataSource,
) {
    fun getUserByEmailAndType(
        email: Email,
        type: UserType,
    ) = user.getEntityUserByEmailAndUserTypeId(
        email = email,
        userTypeId = getUserTypeByType(type).id
    )?.toDtoUse()

    fun createUser(newUser: DtoUser) = this.user.save(
        EntityUser().apply {
            this.username = newUser.username
            this.displayName = newUser.displayName
            this.email = newUser.email
            this.passwordHash = newUser.passwordHash
            this.profilePicUrl = newUser.profileUrl
            this.country = getCountryByCode(newUser.countryCode)
            this.userType = getUserTypeByType(newUser.type)
        }
    ).toDtoUse()

    fun updateRefreshToken(id: UserId, refreshToken: JWTToken) {
        val entry = jwt.findById(id).orElse(null)
        if (entry != null) {
            entry.refreshToken = refreshToken
            jwt.save(entry)
        } else {
            jwt.save(
                EntityJWTToken().apply {
                    this.id = id
                    this.refreshToken = refreshToken
                }
            )
        }
    }

    fun updatePassword(id: UserId, passwordHash: String) {
        val entry = user.findById(id).orElse(null)
        if (entry != null) {
            entry.passwordHash = passwordHash
            user.save(entry)
        }
    }

    private fun getUserTypeByType(type: UserType) = this.userType.findByTypeIgnoreCase(type.name)
    private fun getCountryByCode(code: String) = country.getEntityCountryByCodeIgnoreCase(code)


    private fun EntityUser.toDtoUse() = DtoUser(
        id = this.id,
        username = this.username,
        displayName = this.displayName,
        email = this.email,
        passwordHash = this.passwordHash,
        countryCode = this.country.code,
        type = UserType.valueOf(this.userType.type),
        profileUrl = this.profilePicUrl,
        birthDate = this.birthDate?.toLocalDate()
    )
}