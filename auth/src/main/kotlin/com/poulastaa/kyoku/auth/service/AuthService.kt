package com.poulastaa.kyoku.auth.service

import com.poulastaa.kyoku.auth.model.Notification
import com.poulastaa.kyoku.auth.model.dto.*
import com.poulastaa.kyoku.auth.model.response.ResponseStatus
import com.poulastaa.kyoku.auth.model.response.ResponseWrapper
import com.poulastaa.kyoku.auth.utils.*
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import java.net.InetAddress
import java.net.UnknownHostException

@Service
class AuthService(
    private val notification: NotificationService,
    private val jwt: JWTService,
    private val cache: CacheService,
    private val db: DatabaseService,
) {
    fun processEmailSingIn(
        email: Email,
        password: Password,
    ) = email.takeIf { validateEmail(it) }?.let { EMAIL ->
        getUser(EMAIL, UserType.EMAIL)?.let { user ->
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
            status = ResponseStatus.USER_NOT_FOUND
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
    ) = email.takeIf { validateEmail(it) }?.let { EMAIL ->
        if (getUser(EMAIL, UserType.EMAIL) == null) {
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
        val data = user
        val status = ResponseStatus.USER_FOUND

        ResponseWrapper(
            payload = data,
            status = status
        ).also { _ ->
            cache.setEmailVerificationState(user.email, true)

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
        ).let {
            cache.setUserByEmail(it)

            ResponseWrapper(
                payload = it,
                status = ResponseStatus.USER_CREATED,
            )
        }.also {
            cache.setEmailVerificationState(it.payload!!.email, true)

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
        if (cache.isVerificationTokenUsed(token)) return EmailVerificationStatus.TOKEN_ALREADY_USED

        val email = jwt.verifyAndExtractClaim<Email>(
            token,
            type = JWTTokenType.TOKEN_VERIFICATION_MAIL
        ) ?: return EmailVerificationStatus.TOKEN_EXPIRED

        // the user must be in cache as user trying to authenticate
        if (cache.cacheUserByEmail(email, UserType.EMAIL) == null) return EmailVerificationStatus.USER_NOT_FOUND

        return EmailVerificationStatus.VALID.also {
            cache.storeUsedVerificationToken(token)
            // this status is important...when getting access and refresh token for the first time
            // after authentication for email user....this status is checked first then jwt token is generated
            cache.setEmailVerificationState(email, true)
        }
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
}