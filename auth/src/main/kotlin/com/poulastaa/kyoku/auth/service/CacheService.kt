package com.poulastaa.kyoku.auth.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poulastaa.kyoku.auth.model.CacheKeys
import com.poulastaa.kyoku.auth.model.dto.DtoUser
import com.poulastaa.kyoku.auth.model.dto.SerializedUser
import com.poulastaa.kyoku.auth.model.dto.UserType
import com.poulastaa.kyoku.auth.utils.Email
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@Service
class CacheService(
    private val redis: RedisTemplate<String, Any>,
    private val gson: Gson,
) : CacheKeys() {
    fun cacheUserByEmail(email: Email, type: UserType) = Group.USER.cache<SerializedUser>("$type:$email")?.toDtoUser()
    fun setUserByEmail(
        user: DtoUser,
    ) = Group.USER.set<SerializedUser>("${user.type}:${user.email}", user.toSerializedUser()).let { user }

    fun setEmailVerificationState(email: Email, status: Boolean) = Group.EMAIL_VERIFICATION_STATUS.set(email, status)
    fun cacheAndDeleteEmailVerificationState(
        email: Email,
    ) = (Group.EMAIL_VERIFICATION_STATUS.cache<Boolean>(email) ?: false).also {
        it.takeIf { it }?.let { Group.EMAIL_VERIFICATION_STATUS.delete(email) }
    }

    private inline fun <reified T> Group.cache(
        key: String? = null,
    ) = redis.opsForValue().get(this.buildDynamicKey(key))?.let { data ->
        gson.fromJson<T>(gson.toJson(data), object : TypeToken<T>() {}.type)
    }

    private inline fun <reified T : Any> Group.set(
        key: String,
        data: T,
        expTime: Long? = null,
        unit: TimeUnit? = null,
    ) {
        redis.opsForValue().set(
            this.buildDynamicKey(key),
            data,
            expTime ?: this.expTime,
            unit ?: this.unit
        )
    }

    private fun Group.delete(key: String) = redis.delete(this.buildDynamicKey(key))

    private fun Group.buildDynamicKey(key: String? = null) = this.prefix?.let {
        key?.let { "${this.prefix}:${this.name}:$key" } // ----> PREFIX:NAME:KEY
            ?: "${this.prefix}:${this.name}" // key is null ----> PREFIX:NAME
    } ?: "${this.name}:$key" // prefix is null ----> NAME:kEY


    private fun DtoUser.toSerializedUser() = SerializedUser(
        id = this.id,
        username = this.username,
        email = this.email,
        displayName = this.displayName,
        profileUrl = this.profileUrl,
        passwordHash = this.passwordHash,
        countryCode = this.countryCode,
        type = this.type,
        birthDate = this.birthDate?.toString()
    )

    private fun SerializedUser.toDtoUser() = DtoUser(
        id = this.id,
        username = this.username,
        displayName = this.displayName,
        email = this.email,
        passwordHash = this.passwordHash,
        countryCode = this.countryCode,
        type = this.type,
        birthDate = this.birthDate?.let { LocalDate.parse(it) }
    )
}