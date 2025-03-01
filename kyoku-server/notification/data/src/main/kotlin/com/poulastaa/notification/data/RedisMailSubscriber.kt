package com.poulastaa.notification.data

import com.poulastaa.core.domain.model.MailType
import com.poulastaa.core.domain.repository.auth.LocalAuthCacheDatasource
import com.poulastaa.notification.domain.repository.MailServiceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RedisMailSubscriber(
    private val cache: LocalAuthCacheDatasource,
    private val mail: MailServiceRepository,
) {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            cache.consumeMail { (type, email) ->
                when (type) {
                    MailType.EMAIL_VERIFICATION -> mail.sendEmailVerificationMail(email)
                    MailType.PASSWORD_RESET -> mail.sendPasswordResetMail(email)
                    MailType.WELCOME -> mail.sendWelcomeMail(
                        email = email.split(',')[0],
                        username = email.split(',')[1]
                    )

                    MailType.WELCOME_BACK -> mail.sendWelcomeBackMail(
                        email = email.split(',')[0],
                        username = email.split(',')[1]
                    )
                }
            }
        }
    }
}