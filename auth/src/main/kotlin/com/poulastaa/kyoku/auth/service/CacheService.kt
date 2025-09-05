package com.poulastaa.kyoku.auth.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poulastaa.kyoku.auth.model.CacheKeys
import com.poulastaa.kyoku.auth.model.dto.DtoUser
import com.poulastaa.kyoku.auth.model.dto.UserType
import com.poulastaa.kyoku.auth.utils.Email
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class CacheService(
    private val redis: RedisTemplate<String, Any>,
    private val gson: Gson,
) : CacheKeys() {
    fun cacheUserByEmail(email: Email, type: UserType) = Group.USER.cache<DtoUser>("$type:$email")
    fun setUserByEmail(user: DtoUser) = Group.USER.set("${user.type}:${user.email}", user).let { user }

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
            gson.toJson(data),
            expTime ?: this.expTime,
            unit ?: this.unit
        )
    }

    private fun Group.delete(key: String) = redis.delete(this.buildDynamicKey(key))

    private fun Group.buildDynamicKey(key: String? = null) = this.prefix?.let {
        key?.let { "${this.prefix}:${this.name}:$key" } // ----> PREFIX:NAME:KEY
            ?: "${this.prefix}:${this.name}" // key is null ----> PREFIX:NAME
    } ?: "${this.name}:$key" // prefix is null ----> NAME:kEY
}