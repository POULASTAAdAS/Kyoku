package com.poulastaa.core.database.repository.auth

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poulastaa.core.domain.model.DtoDBUser
import com.poulastaa.core.domain.model.MailType
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.LocalCoreCacheDatasource
import com.poulastaa.core.domain.repository.RedisKeys
import com.poulastaa.core.domain.repository.auth.Email
import com.poulastaa.core.domain.repository.auth.LocalAuthCacheDatasource
import kotlinx.coroutines.delay
import redis.clients.jedis.JedisPool
import redis.clients.jedis.params.SetParams

class RedisLocalAuthDataSource(
    private val redisPool: JedisPool,
    private val gson: Gson,
    private val coreCache: LocalCoreCacheDatasource,
) : LocalAuthCacheDatasource, RedisKeys() {
    override fun setUserByEmail(
        key: Email,
        type: UserType,
        value: DtoDBUser,
    ) = coreCache.setUserByEmail(key, type, value)

    override fun cachedCountryId(key: String): Int? = redisPool.resource.use { jedis ->
        jedis.get("${Group.COUNTRY_ID}:$key")?.toInt()
    }

    override fun setCountryId(key: String, value: String) {
        redisPool.resource.use { jedis ->
            jedis.set(
                "${Group.COUNTRY_ID}:$key",
                value,
                SetParams.setParams().nx().ex(Group.COUNTRY_ID.expTime)
            )
        }
    }

    override fun isVerificationTokenUsed(token: String): Boolean {
        val cache = redisPool.resource.use { jedis ->
            jedis.get("${Group.EMAIL_VERIFICATION_TOKEN}:$token")
        } ?: return false

        return cache == token
    }

    override fun storeUsedVerificationToken(token: String) {
        redisPool.resource.use { jedis ->
            jedis.set(
                "${Group.EMAIL_VERIFICATION_TOKEN}:${token}",
                token,
                SetParams.setParams().nx().ex(Group.EMAIL_VERIFICATION_TOKEN.expTime)
            )
        }
    }

    override fun cacheEmailVerificationStatus(key: Email): Boolean? = redisPool.resource.use { jedis ->
        jedis.get("${Group.EMAIL_VERIFICATION_STATUS}:$key")
    }?.toBoolean()

    override fun setEmailVerificationStatus(key: Email) {
        redisPool.resource.use { jedis ->
            jedis.set(
                "${Group.EMAIL_VERIFICATION_STATUS}:$key",
                false.toString(),
                SetParams.setParams().nx().ex(Group.EMAIL_VERIFICATION_STATUS.expTime)
            )
        }
    }

    override fun deleteEmailVerificationStatus(key: Email) {
        redisPool.resource.use { jedis ->
            jedis.del("${Group.EMAIL_VERIFICATION_STATUS}:$key")
        }
    }

    override fun produceMail(message: Pair<MailType, Email>) {
        redisPool.resource.use { jedis ->
            jedis.lpush(Channel.NOTIFICATION.name, gson.toJson(message))
        }
    }

    override suspend fun consumeMail(block: (Pair<MailType, Email>) -> Unit) {
        while (true) {
            redisPool.resource.use { jedis ->
                jedis.rpop(Channel.NOTIFICATION.name)?.let { message ->
                    val map: Pair<MailType, Email> = gson.fromJson(
                        message,
                        object : TypeToken<Pair<MailType, Email>>() {}.type
                    )

                    block(map)
                }
            }

            delay(Channel.NOTIFICATION.delay)
        }
    }

    override fun cacheJWTTokenState(email: String): Boolean {
        val state = redisPool.resource.use { jedis ->
            jedis.get("${Group.JWT_TOKEN_STATUS}:$email")
        }?.toBoolean()

        return if (state == null) false
        else if (!state) {
            redisPool.resource.use { jedis ->
                jedis.del("${Group.JWT_TOKEN_STATUS}:$email")
            }
            true
        } else false
    }

    override fun storeJWTTokenState(email: Email) {
        redisPool.resource.use { jedis ->
            jedis.set(
                "${Group.JWT_TOKEN_STATUS}:$email",
                false.toString(),
                SetParams.setParams().nx().ex(Group.JWT_TOKEN_STATUS.expTime)
            )
        }
    }

    override fun isResetPasswordTokenUsed(token: String): Boolean {
        val cache = redisPool.resource.use { jedis ->
            jedis.get("${Group.RESET_PASSWORD_TOKEN}:$token")
        } ?: return false

        return cache == token
    }
}