package com.poulastaa.data.model

import com.poulastaa.data.model.db_table.user.EmailAuthUserTable
import com.poulastaa.data.model.db_table.user.GoogleAuthUserTable
import com.poulastaa.data.model.db_table.user.PasskeyAuthUserTable
import com.poulastaa.domain.dao.user.EmailAuthUser
import com.poulastaa.domain.dao.user.GoogleAuthUser
import com.poulastaa.domain.dao.user.PasskeyAuthUser
import com.poulastaa.domain.repository.users.EmailAuthUserRepository
import com.poulastaa.domain.repository.users.GoogleAuthUserRepository
import com.poulastaa.domain.repository.users.PasskeyAuthUserRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.toUser

data class DbUsers(
    val emailUser: EmailAuthUserRepository,
    val googleUser: GoogleAuthUserRepository,
    val passekyUser: PasskeyAuthUserRepository
) {
    suspend fun gerDbUser(userTypeHelper: UserTypeHelper) = when (userTypeHelper.userType) {
        UserType.GOOGLE_USER -> gerGoogleUser(userTypeHelper.id)

        UserType.EMAIL_USER -> getEmailUser(userTypeHelper.id)

        UserType.PASSKEY_USER -> gerPasskeyUser(userTypeHelper.id)
    }


    private suspend fun getEmailUser(email: String) = dbQuery {
        EmailAuthUser.find {
            EmailAuthUserTable.email eq email
        }.firstOrNull()?.toUser(UserType.EMAIL_USER)
    }

    private suspend fun gerGoogleUser(sub: String) = dbQuery {
        GoogleAuthUser.find {
            GoogleAuthUserTable.sub eq sub
        }.firstOrNull()?.toUser(UserType.GOOGLE_USER)
    }

    private suspend fun gerPasskeyUser(email: String) = dbQuery {
        PasskeyAuthUser.find {
            PasskeyAuthUserTable.email eq email
        }.firstOrNull()?.toUser(UserType.PASSKEY_USER)
    }
}
