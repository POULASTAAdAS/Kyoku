package com.poulastaa.data.repository.faourite

import com.poulastaa.data.model.db_table.user_fev.EmailUserFavouriteTable
import com.poulastaa.data.model.db_table.user_fev.GoogleUserFavouriteTable
import com.poulastaa.data.model.db_table.user_fev.PasskeyUserFavouriteTable
import com.poulastaa.data.model.utils.UserType
import com.poulastaa.data.model.utils.UserTypeHelper
import com.poulastaa.domain.repository.favourite.FavouriteRepository
import com.poulastaa.plugins.dbQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore

class FavouriteRepositoryImpl : FavouriteRepository {
    override fun handelFavourite(songId: Long, helper: UserTypeHelper, operation: Boolean) {
        if (operation) insertIntoFavourite(songId, helper)
        else removeFromFavourite(songId, helper)
    }


    override fun insertIntoFavourite(songId: Long, helper: UserTypeHelper) {
        CoroutineScope(Dispatchers.IO).launch {
            dbQuery {
                when (helper.userType) {
                    UserType.GOOGLE_USER -> {
                        GoogleUserFavouriteTable.insertIgnore {
                            it[this.userId] = helper.id
                            it[this.songId] = songId
                        }
                    }

                    UserType.EMAIL_USER -> {
                        EmailUserFavouriteTable.insertIgnore {
                            it[this.userId] = helper.id
                            it[this.songId] = songId
                        }
                    }

                    UserType.PASSKEY_USER -> {
                        PasskeyUserFavouriteTable.insertIgnore {
                            it[this.userId] = helper.id
                            it[this.songId] = songId
                        }
                    }
                }
            }
        }
    }


    override fun removeFromFavourite(songId: Long, helper: UserTypeHelper) {
        CoroutineScope(Dispatchers.IO).launch {
            dbQuery {
                when (helper.userType) {
                    UserType.GOOGLE_USER -> {
                        GoogleUserFavouriteTable.deleteWhere {
                            this.userId eq helper.id and (this.songId eq songId)
                        }
                    }

                    UserType.EMAIL_USER -> {
                        EmailUserFavouriteTable.deleteWhere {
                            this.userId eq helper.id and (this.songId eq songId)
                        }
                    }

                    UserType.PASSKEY_USER -> {
                        PasskeyUserFavouriteTable.deleteWhere {
                            this.userId eq helper.id and (this.songId eq songId)
                        }
                    }
                }
            }
        }
    }
}