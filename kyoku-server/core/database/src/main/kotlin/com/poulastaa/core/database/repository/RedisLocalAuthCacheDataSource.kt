package com.poulastaa.core.database.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.MailType
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.Email
import com.poulastaa.core.domain.repository.LocalAuthCacheDatasource
import kotlinx.coroutines.delay
import redis.clients.jedis.JedisPool
import redis.clients.jedis.params.SetParams

class RedisLocalAuthCacheDataSource(
    private val redisPool: JedisPool,
    private val gson: Gson,
) : LocalAuthCacheDatasource {
    private object Group {
        const val COUNTRY_ID = "COUNTRY_ID"
        const val USER = "USER"
        const val EMAIL_VERIFICATION_STATUS = "EMAIL_VERIFICATION_STATUS"
        const val JWT_TOKEN_STATUS = "JWT_TOKEN_STATUS"
        const val EMAIL_VERIFICATION_TOKEN = "EMAIL_VERIFICATION_TOKEN"
        const val RESET_PASSWORD_TOKEN = "RESET_PASSWORD_TOKEN"
    }

    private object Channel {
        const val NOTIFICATION = "NOTIFICATION"
    }

    override fun cachedCountryId(key: String): Int? = redisPool.resource.use { jedis ->
        jedis.get("${Group.COUNTRY_ID}:$key")?.toInt()
    }

    override fun setCountryId(key: String, value: String) {
        redisPool.resource.use { jedis ->
            jedis.set(
                "${Group.COUNTRY_ID}:$key",
                value,
                SetParams.setParams().nx().ex(3600) // 1 hour
            )
        }
    }

    override fun cachedUserByEmail(
        key: Email,
        type: UserType,
    ): DBUserDto? = redisPool.resource.use { jedis ->
        jedis.get("${Group.USER}:${type.name}:$key")
    }?.let { string ->
        gson.fromJson(string, DBUserDto::class.java)
    }

    override fun setUserByEmail(
        key: Email,
        type: UserType,
        value: DBUserDto,
    ) {
        redisPool.resource.use { jedis ->
            jedis.set(
                "${Group.USER}:${type.name}:$key",
                gson.toJson(value),
                SetParams.setParams().ex(15 * 60) // 15 minute
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
                SetParams.setParams().nx().ex(15 * 60) // 10 minute
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
                SetParams.setParams().nx().ex(10 * 60) // 10 minute
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
            jedis.lpush(Channel.NOTIFICATION, gson.toJson(message))
        }
    }

    override suspend fun consumeMail(block: (Pair<MailType, Email>) -> Unit) {
        while (true) {
            redisPool.resource.use { jedis ->
                jedis.rpop(Channel.NOTIFICATION)?.let { message ->
                    val map: Pair<MailType, Email> = gson.fromJson(
                        message,
                        object : TypeToken<Pair<MailType, Email>>() {}.type
                    )

                    block(map)
                }
            }

            delay(1000 * 6) // 6's
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
                SetParams.setParams().nx().ex(15 * 60) // 10 minute
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