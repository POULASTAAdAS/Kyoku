package com.poulastaa.kyoku.auth.service

import com.poulastaa.kyoku.auth.model.Endpoints
import com.poulastaa.kyoku.auth.model.Notification
import com.poulastaa.kyoku.auth.model.response.ResponseStatus
import com.poulastaa.kyoku.auth.model.response.ResponseWrapper
import com.poulastaa.kyoku.auth.utils.Email
import com.poulastaa.kyoku.auth.utils.Password
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
        db.getUserByEmail(EMAIL)?.let { user ->
            user.takeIf { isSamePassword(password, user.passwordHash) }?.let {
                notification.publishMail(
                    Notification.Email(
                        email = email,
                        username = user.username,
                        endpoint = Endpoints.EMAIL_SING_IN,
                        type = Notification.Type.AUTHENTICATE
                    )
                )

                ResponseWrapper(
                    status = ResponseStatus.USER_FOUND,
                    payload = it
                )
            } ?: ResponseWrapper( // password does not match
                status = ResponseStatus.PASSWORD_DOES_NOT_MATCH
            )
        } ?: ResponseWrapper( // user not found
            status = ResponseStatus.USER_NOT_FOUND
        )
    } ?: ResponseWrapper( // invalid email
        status = ResponseStatus.EMAIL_NOT_VALID
    )

    private fun validateEmail(email: Email) = email.isValidDomain() && email.isValidEmail()

    private fun String.encryptPassword(): String? = BCrypt.hashpw(this, BCrypt.gensalt(15))
    private fun isSamePassword(password: String, passwordHash: Password) = BCrypt.checkpw(password, passwordHash)


    private fun String.isValidEmail() = this.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex())
    private fun String.isValidDomain() = try {
        InetAddress.getByName(this.substringAfter("@")) != null
    } catch (_: UnknownHostException) {
        false
    }
}