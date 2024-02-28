package com.poulastaa.data.repository.artist

import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.UserTypeHelper
import com.poulastaa.data.model.db_table.ArtistTable
import com.poulastaa.data.model.db_table.user_artist.EmailUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_artist.GoogleUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_artist.PasskeyUserArtistRelationTable
import com.poulastaa.data.model.setup.artist.*
import com.poulastaa.domain.dao.Artist
import com.poulastaa.domain.dao.user_artist.EmailUserArtistRelation
import com.poulastaa.domain.dao.user_artist.GoogleUserArtistRelation
import com.poulastaa.domain.dao.user_artist.PasskeyUserArtistRelation
import com.poulastaa.domain.repository.aritst.ArtistRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.toResponseArtist
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and

class ArtistRepositoryImpl : ArtistRepository {
    override suspend fun suggestArtist(
        req: SuggestArtistReq,
        countryId: Int
    ): SuggestArtistResponse {
        val artistList = dbQuery {
            Artist.find {  // sort by points
                ArtistTable.country eq countryId
            }.orderBy(ArtistTable.points to SortOrder.DESC)
                .toResponseArtist()
                .removeDuplicate(req.alreadySendArtistList, req.isSelected)
        }

        return SuggestArtistResponse(
            status = ArtistResponseStatus.SUCCESS,
            artistList = artistList
        )
    }

    override suspend fun storeArtist(
        helper: UserTypeHelper,
        artistNameList: List<String>
    ): StoreArtistResponse {
        val artistIdList = dbQuery {
            Artist.find {
                ArtistTable.name inList artistNameList
            }.map {
                it.id.value
            }
        }

        when (helper.userType) {
            UserType.GOOGLE_USER -> artistIdList.storeArtistForGoogleUser(id = helper.id.toLong())

            UserType.EMAIL_USER -> artistIdList.storeArtistForEmailUser(id = helper.id.toLong())

            UserType.PASSKEY_USER -> artistIdList.storeArtistForPasskeyUser(id = helper.id.toLong())
        }

        return StoreArtistResponse(
            status = ArtistResponseStatus.SUCCESS
        )
    }

    private fun Iterable<ResponseArtist>.removeDuplicate(
        oldList: List<String>,
        isSelectedReq: Boolean
    ): List<ResponseArtist> {
        val filteredResult = this.filterNot {
            oldList.contains(it.name)
        }

        return filteredResult.take(if (isSelectedReq) 3 else 5)
    }


    private suspend fun Iterable<Int>.storeArtistForEmailUser(id: Long) {
        dbQuery {
            this.forEach {
                val found = EmailUserArtistRelation.find {
                    EmailUserArtistRelationTable.userId eq id and (EmailUserArtistRelationTable.artistId eq it)
                }.firstOrNull()

                if (found == null) {
                    EmailUserArtistRelation.new {
                        this.artistId = it
                        this.userId = id
                    }
                }
            }
        }
    }

    private suspend fun Iterable<Int>.storeArtistForGoogleUser(id: Long) {
        dbQuery {
            this.forEach {
                val found = GoogleUserArtistRelation.find {
                    GoogleUserArtistRelationTable.userId eq id and (GoogleUserArtistRelationTable.artistId eq it)
                }.firstOrNull()

                if (found == null) {
                    GoogleUserArtistRelation.new {
                        this.artistId = it
                        this.userId = id
                    }
                }
            }
        }
    }

    private suspend fun Iterable<Int>.storeArtistForPasskeyUser(id: Long) {
        dbQuery {
            this.forEach {
                val found = PasskeyUserArtistRelation.find {
                    PasskeyUserArtistRelationTable.userId eq id and (PasskeyUserArtistRelationTable.artistId eq it)
                }.firstOrNull()

                if (found == null) {
                    PasskeyUserArtistRelation.new {
                        this.artistId = it
                        this.userId = id
                    }
                }
            }
        }
    }
}