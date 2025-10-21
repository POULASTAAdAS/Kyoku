package com.poulastaa.kyoku.auth.service

import com.poulastaa.kyoku.auth.controller.toResponse
import com.poulastaa.kyoku.auth.model.Notification
import com.poulastaa.kyoku.auth.model.dto.*
import com.poulastaa.kyoku.auth.model.response.*
import com.poulastaa.kyoku.auth.utils.*
import org.springframework.context.ApplicationContext
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.time.Duration

@Service
class AuthService(
    private val notification: NotificationService,
    private val jwt: JWTService,
    private val cache: CacheService,
    private val db: DatabaseService,
    private val context: ApplicationContext,
) {
    fun processEmailSingIn(
        email: Email,
        password: Password,
    ) = email.takeIf { validateEmail(it) }?.let { _ ->
        getUser(email, UserType.EMAIL)?.let { user ->
            // if user first tries to sing-up before validating email tries to log-in
            if (user.id == -1L) return@let ResponseWrapper(
                status = ResponseStatus.USER_NOT_FOUND
            )

            user.takeIf { isSamePassword(password, user.passwordHash) }?.let { dtoUser ->
                // TODO get and send type of login from user-service ---------> THOUGHT: use gRPC
                //  Types:
                //  new user -> opted out after creating account thus no initial data is collected to proceed
                //  no playlist | artist | genre -> does not have enough collected data to proceed
                //  change response accordingly
                val data = dtoUser
                val status = ResponseStatus.USER_FOUND

                ResponseWrapper(
                    payload = data,
                    status = status
                ).also { _ ->
                    notification.publishMail(
                        Notification.Email(
                            email = email,
                            username = user.username,
                            type = Notification.Type.AUTHENTICATE
                        )
                    )
                }
            } ?: ResponseWrapper( // password does not match
                status = ResponseStatus.PASSWORD_DOES_NOT_MATCH
            )
        } ?: ResponseWrapper( // user not found
            status = ResponseStatus.UNAUTHORIZED
        )
    } ?: ResponseWrapper( // invalid email
        status = ResponseStatus.EMAIL_NOT_VALID
    )

    /**
     *  [DtoUser] is first saved in cache with null id see: [CacheService.setUserByEmail]....and after verifying email,
     *  it will be moved to the database and the cache id will be replaced by db generated id. see: [TODO]
     */
    fun processEmailSingUp(
        username: Username,
        email: Email,
        password: Password,
        countryCode: String,
    ) = email.takeIf { validateEmail(it) }?.let { _ ->
        val user = getUser(email, UserType.EMAIL)

        // no user found in database or cache
        // user found in cache but id == -1 ----> user may have tried to sign-up multiple time within 15 minute (within rate limit)
        if (user == null || user.id == -1L) {
            password.encryptPassword()?.let { passwordHash ->
                ResponseWrapper(
                    payload = cache.setUserByEmail(
                        DtoUser(
                            username = username,
                            displayName = username,
                            email = email,
                            passwordHash = passwordHash,
                            countryCode = countryCode,
                            type = UserType.EMAIL,
                        )
                    ),
                    status = ResponseStatus.USER_CREATED,
                ).also {
                    notification.publishMail(
                        Notification.Email(
                            email = email,
                            username = username,
                            type = Notification.Type.AUTHENTICATE
                        )
                    )
                }
            } ?: ResponseWrapper( // encrypting password failed
                status = ResponseStatus.INTERNAL_SERVER_ERROR
            )
        } else ResponseWrapper( // user not found
            status = ResponseStatus.EMAIL_ALREADY_IN_USE
        )
    } ?: ResponseWrapper( // invalid email
        status = ResponseStatus.EMAIL_NOT_VALID
    )

    fun processGoogleAuth(
        payload: GoogleAuthPayload,
        countryCode: String,
    ) = getUser(payload.email, UserType.GOOGLE)?.let { user -> // user in database --> login user
        // TODO: get user from user-service
        //  Types:
        //  new user -> opted out after creating account thus no initial data is collected to proceed
        //  no playlist | artist | genre -> does not have enough collected data to proceed
        //  change response accordingly

        val accessToken = jwt.generateToken(
            payload = DtoAuthenticationTokenClaim(
                email = user.email,
                userType = user.type,
            ),
            type = JWTTokenType.TOKEN_ACCESS
        )
        val refreshToken = jwt.generateToken(
            payload = DtoAuthenticationTokenClaim(
                email = user.email,
                userType = user.type,
            ),
            type = JWTTokenType.TOKEN_REFRESH
        )

        //  save refresh token to db
        db.updateRefreshToken(user.id, refreshToken)

        ResponseWrapper(
            status = ResponseStatus.USER_FOUND,
            payload = ResponseGoogleAuth(
                user = user.toResponse(ResponseStatus.USER_FOUND),
                token = ResponseToken(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            )
        ).also { _ ->
            notification.publishMail(
                Notification.Email(
                    email = payload.email,
                    username = user.username,
                    type = Notification.Type.WELCOME_BACK
                )
            )
        }
    } ?: payload.sub.encryptPassword()?.let { passwordHash -> // user not found create user
        db.createUser(
            DtoUser(
                username = payload.name,
                displayName = payload.displayName,
                email = payload.email,
                passwordHash = passwordHash,
                countryCode = countryCode,
                type = UserType.GOOGLE,
            )
        ).let { user ->
            cache.setUserByEmail(user)

            val accessToken = jwt.generateToken(
                payload = DtoAuthenticationTokenClaim(
                    email = user.email,
                    userType = user.type,
                ),
                type = JWTTokenType.TOKEN_ACCESS
            )
            val refreshToken = jwt.generateToken(
                payload = DtoAuthenticationTokenClaim(
                    email = user.email,
                    userType = user.type,
                ),
                type = JWTTokenType.TOKEN_REFRESH
            )

            //  save refresh token to db
            db.updateRefreshToken(user.id, refreshToken)

            ResponseWrapper(
                status = ResponseStatus.USER_CREATED,
                payload = ResponseGoogleAuth(
                    user = user.toResponse(ResponseStatus.USER_CREATED),
                    token = ResponseToken(
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                ),
            )
        }.also {
            notification.publishMail(
                Notification.Email(
                    email = payload.email,
                    username = payload.name,
                    type = Notification.Type.WELCOME
                )
            )
        }
    } ?: ResponseWrapper( // encrypting password failed
        status = ResponseStatus.INTERNAL_SERVER_ERROR
    )

    fun validateAuthenticationMailPayload(token: JWTToken): EmailVerificationStatus {
        if (cache.isJWTTokenUsed(token)) return EmailVerificationStatus.TOKEN_ALREADY_USED

        val email = jwt.verifyAndExtractClaim<Email>(
            token,
            type = JWTTokenType.TOKEN_VERIFICATION_MAIL
        ) ?: return EmailVerificationStatus.TOKEN_EXPIRED

        // the user must be in cache as user trying to authenticate
        if (cache.cacheUserByEmail(email, UserType.EMAIL) == null) return EmailVerificationStatus.USER_NOT_FOUND

        return EmailVerificationStatus.VALID.also {
            cache.storeUsedJWTToken(token)
            // this status is important...when getting access and refresh token for the first time
            // after authentication for email user....this status is checked first then jwt token is generated
            cache.setEmailVerificationState(email, UserType.EMAIL, true)
        }
    }

    fun generateAuthenticationTokens(
        email: Email,
        type: UserType,
    ) = email.takeIf { email -> cache.cacheAndDeleteEmailVerificationState(email, UserType.EMAIL) }?.let { _ ->
        var user = cache.cacheUserByEmail(email, type) ?: return ResponseToken()

        val accessToken = jwt.generateToken(
            payload = DtoAuthenticationTokenClaim(
                email = user.email,
                userType = user.type,
            ),
            type = JWTTokenType.TOKEN_ACCESS
        )
        val refreshToken = jwt.generateToken(
            payload = DtoAuthenticationTokenClaim(
                email = user.email,
                userType = user.type,
            ),
            type = JWTTokenType.TOKEN_REFRESH
        )

        // is userId == -1 new user ---> first create entry
        if (user.id == -1L) user = db.createUser(
            DtoUser(
                username = user.username,
                displayName = user.displayName,
                email = user.email,
                passwordHash = user.passwordHash,
                countryCode = user.countryCode,
                type = type
            )
        ).also {
            cache.setUserByEmail(it) // update -1 ID with new generated ID
            notification.publishMail(
                Notification.Email(
                    email = user.email,
                    username = user.username,
                    type = Notification.Type.WELCOME
                )
            )
        } else notification.publishMail(
            Notification.Email(
                email = user.email,
                username = user.username,
                type = Notification.Type.WELCOME_BACK
            )
        )

        db.updateRefreshToken(user.id, refreshToken)

        ResponseToken(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    } ?: ResponseToken() // invalid request

    fun refreshToken(
        payload: DtoAuthenticationTokenClaim,
        token: JWTToken,
    ): RefreshTokenResponse {
        if (cache.isJWTTokenUsed(token)) return RefreshTokenResponse()
        val claims = jwt.verifyAndExtractClaim<DtoAuthenticationTokenClaim>(
            token = token,
            type = JWTTokenType.TOKEN_REFRESH
        ) ?: return RefreshTokenResponse(RefreshTokenResponseStatus.TOKEN_EXPIRED)

        if (claims.email != payload.email) return RefreshTokenResponse(RefreshTokenResponseStatus.INVALID_TOKEN)
        if (claims.userType != payload.userType) return RefreshTokenResponse(RefreshTokenResponseStatus.INVALID_TOKEN)

        val user = getUser(payload.email, payload.userType)
            ?: return RefreshTokenResponse(RefreshTokenResponseStatus.INVALID_TOKEN)
        val accessToken = jwt.generateToken(
            payload = DtoAuthenticationTokenClaim(
                email = user.email,
                userType = user.type,
            ),
            type = JWTTokenType.TOKEN_ACCESS
        )
        val refreshToken = jwt.generateToken(
            payload = DtoAuthenticationTokenClaim(
                email = user.email,
                userType = user.type,
            ),
            type = JWTTokenType.TOKEN_REFRESH
        )

        db.updateRefreshToken(user.id, refreshToken).also {
            val (time, unit) = context.getBean(
                "provideRefreshTokenConfigurationsClass",
                DtoJWTConfigInfo::class.java
            ).expDuration.toBestFitUnit()

            cache.storeUsedJWTToken(token, time, unit)
        }

        return RefreshTokenResponse(
            status = RefreshTokenResponseStatus.SUCCESS,
            payload = ResponseToken(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        )
    }

    fun sendForgotPasswordMail(
        email: Email,
    ): ResponseWrapper<ResponseForgoPasswordMailStatus> = email.takeIf { validateEmail(it) }?.let { _ ->
        getUser(email, UserType.EMAIL)?.let { user ->
            if (user.id == -1L) return@let ResponseWrapper(
                status = ResponseStatus.USER_NOT_FOUND,
                payload = ResponseForgoPasswordMailStatus.USER_NOT_FOUND
            )

            // generate 5 digit code sent to user through mail
            val code = (10000..99999).random(Random)
            notification.publishMail(
                Notification.Email(
                    email = email,
                    username = user.username,
                    data = code,
                    type = Notification.Type.FORGOT_PASSWORD_CODE
                )
            )

            // set same in redis with 10 minute expiry
            cache.setForgotPasswordCode(
                email = email,
                code = code.toString()
            )

            ResponseWrapper(
                status = ResponseStatus.USER_FOUND, // ignore this for now -> bad design
                payload = ResponseForgoPasswordMailStatus.SENT
            )
        } ?: ResponseWrapper(
            status = ResponseStatus.USER_NOT_FOUND,
            payload = ResponseForgoPasswordMailStatus.USER_NOT_FOUND
        )
    } ?: ResponseWrapper(
        status = ResponseStatus.EMAIL_NOT_VALID,
        payload = ResponseForgoPasswordMailStatus.INVALID_EMAIL
    )

    fun validateForgotPasswordCode(code: String, email: Email) = email.takeIf { validateEmail(it) }?.let { _ ->
        getUser(email, UserType.EMAIL)?.let { user ->
            if (user.id == -1L) return@let CodeValidationResponse(CodeValidationResponseStatus.USER_NOT_FOUND)

            val cacheCode = cache.cacheForgotPasswordCode(email)
                ?: return@let CodeValidationResponse(CodeValidationResponseStatus.EXPIRED)

            if (code == cacheCode) {
                val token = jwt.generateToken(
                    payload = DtoAuthenticationTokenClaim(
                        email = user.email,
                        userType = user.type,
                    ),
                    type = JWTTokenType.TOKEN_UPDATE_PASSWORD
                ).also { cache.deleteForgotPasswordCode(email) }

                CodeValidationResponse(CodeValidationResponseStatus.VALID, token)
            } else CodeValidationResponse(CodeValidationResponseStatus.INVALID_CODE)
        } ?: CodeValidationResponse(CodeValidationResponseStatus.USER_NOT_FOUND)
    } ?: CodeValidationResponse(CodeValidationResponseStatus.INVALID_EMAIL)

    fun updatePassword(
        password: Password,
        token: JWTToken,
    ): ResponseWrapper<UpdatePasswordResponse> {
        if (cache.isJWTTokenUsed(token)) return ResponseWrapper(
            status = ResponseStatus.UNAUTHORIZED,
            payload = UpdatePasswordResponse(
                UpdatePasswordStatus.EXPIRED_TOKEN
            )
        )

        if (password.length < 6 || password.isBlank()) return ResponseWrapper(
            status = ResponseStatus.UNAUTHORIZED,
            payload = UpdatePasswordResponse(
                UpdatePasswordStatus.INVALID_PASSWORD
            )
        )

        val payload = jwt.verifyAndExtractClaim<DtoAuthenticationTokenClaim>(
            token = token,
            type = JWTTokenType.TOKEN_UPDATE_PASSWORD
        ) ?: return ResponseWrapper(
            status = ResponseStatus.UNAUTHORIZED,
            payload = UpdatePasswordResponse(
                UpdatePasswordStatus.INVALID_TOKEN
            )
        )

        val user = getUser(payload.email, payload.userType) ?: return ResponseWrapper(
            status = ResponseStatus.USER_NOT_FOUND,
            payload = UpdatePasswordResponse(
                UpdatePasswordStatus.USER_NOT_FOUND
            )
        )

        if (isSamePassword(password, user.passwordHash)) return ResponseWrapper(
            status = ResponseStatus.USER_FOUND,
            payload = UpdatePasswordResponse(
                UpdatePasswordStatus.SAME_PASSWORD
            )
        )

        val passwordHash = password.encryptPassword() ?: return ResponseWrapper(
            status = ResponseStatus.INTERNAL_SERVER_ERROR,
            payload = UpdatePasswordResponse(UpdatePasswordStatus.ERROR)
        )
        db.updatePassword(
            id = user.id,
            passwordHash = passwordHash
        ).also {
            cache.setUserByEmail(user.copy(passwordHash = passwordHash))
        }

        val (time, unit) = context.getBean(
            "provideUpdatePasswordTokenConfigurationsClass",
            DtoJWTConfigInfo::class.java
        ).expDuration.toBestFitUnit()
        cache.storeUsedJWTToken(token, time, unit)

        return ResponseWrapper(
            status = ResponseStatus.USER_FOUND,
            payload = UpdatePasswordResponse(
                UpdatePasswordStatus.UPDATED
            )
        )
    }

    private fun getUser(
        email: Email,
        type: UserType,
    ) = cache.cacheUserByEmail(email, type) ?: db.getUserByEmailAndType(email, type)?.also {
        cache.setUserByEmail(it)
    }

    private fun validateEmail(email: Email) = email.isValidDomain() && email.isValidEmail()

    private fun String.encryptPassword(): PasswordHash? = BCrypt.hashpw(this, BCrypt.gensalt(15))
    private fun isSamePassword(password: String, passwordHash: Password) = BCrypt.checkpw(password, passwordHash)

    private fun String.isValidEmail() = this.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex())
    private fun String.isValidDomain() = try {
        InetAddress.getByName(this.substringAfter("@")) != null
    } catch (_: UnknownHostException) {
        false
    }

    fun Duration.toBestFitUnit() = when {
        this.inWholeDays > 0 -> this.inWholeDays to TimeUnit.DAYS
        this.inWholeHours > 0 -> this.inWholeHours to TimeUnit.HOURS
        this.inWholeMinutes > 0 -> this.inWholeMinutes to TimeUnit.MINUTES
        this.inWholeSeconds > 0 -> this.inWholeSeconds to TimeUnit.SECONDS
        this.inWholeMilliseconds > 0 -> this.inWholeMilliseconds to TimeUnit.MILLISECONDS
        else -> this.inWholeNanoseconds to TimeUnit.NANOSECONDS
    }
}