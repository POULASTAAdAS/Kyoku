package com.poulastaa.data.repository.playlist

import com.poulastaa.data.model.db_table.PlaylistTable
import com.poulastaa.data.model.db_table.user_pinned_playlist.EmailUserPinnedPlaylistTable
import com.poulastaa.data.model.db_table.user_pinned_playlist.GoogleUserPinnedPlaylistTable
import com.poulastaa.data.model.db_table.user_pinned_playlist.PasskeyUserPinnedPlaylistTable
import com.poulastaa.data.model.db_table.user_playlist.EmailUserPlaylistTable
import com.poulastaa.data.model.db_table.user_playlist.GoogleUserPlaylistTable
import com.poulastaa.data.model.db_table.user_playlist.PasskeyUserPlaylistTable
import com.poulastaa.data.model.item.ItemOperation
import com.poulastaa.data.model.utils.PlaylistRow
import com.poulastaa.data.model.utils.UserType
import com.poulastaa.domain.dao.playlist.EmailUserPlaylist
import com.poulastaa.domain.dao.playlist.GoogleUserPlaylist
import com.poulastaa.domain.dao.playlist.PasskeyUserPlaylist
import com.poulastaa.domain.dao.playlist.Playlist
import com.poulastaa.domain.repository.playlist.PlaylistRepository
import com.poulastaa.plugins.dbQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select

class PlaylistRepositoryImpl : PlaylistRepository {
    override suspend fun cretePlaylistForEmailUser(list: List<PlaylistRow>, playlist: Playlist) {
        list.forEach {
            dbQuery {
                EmailUserPlaylist.new {
                    this.playlistId = playlist.id.value
                    this.songId = it.songId
                    this.userId = it.userId
                }
            }
        }
    }

    override suspend fun cretePlaylistForGoogleUser(list: List<PlaylistRow>, playlist: Playlist) {
        list.forEach {
            dbQuery {
                GoogleUserPlaylist.new {
                    this.playlistId = playlist.id.value
                    this.songId = it.songId
                    this.userId = it.userId
                }
            }
        }
    }

    override suspend fun cretePlaylistForPasskeyUser(list: List<PlaylistRow>, playlist: Playlist) {
        list.forEach {
            dbQuery {
                PasskeyUserPlaylist.new {
                    this.playlistId = playlist.id.value
                    this.songId = it.songId
                    this.userId = it.userId
                }
            }
        }
    }

    override suspend fun handlePlaylist(
        userId: Long,
        userType: UserType,
        playlistId: Long,
        operation: ItemOperation
    ): Boolean {
        return when (userType) {
            UserType.GOOGLE_USER -> {
                when (operation) {
                    ItemOperation.ADD -> {
                        false
                    }

                    ItemOperation.DELETE -> {
                        try {
                            dbQuery {
                                findPlaylistForGoogleUser(
                                    playlistId = playlistId,
                                    userId = userId
                                ).forEach {
                                    it.delete()
                                }
                            }

                            deletePinnedPlaylistForGoogleUser(
                                playlistId = playlistId,
                                userId = userId
                            )

                            CoroutineScope(Dispatchers.IO).launch {
                                if (
                                    dbQuery {
                                        findPlaylistForPasskeyUser(
                                            playlistId = playlistId,
                                            userId = userId
                                        ).empty() && findPlaylistForEmailUser(
                                            playlistId = playlistId,
                                            userId = userId
                                        ).empty() && findPinnedPlaylistForPasskeyUser(
                                            playlistId = playlistId,
                                            userId = userId
                                        ).empty() && findPinnedPlaylistForEmailUser(
                                            playlistId = playlistId,
                                            userId = userId
                                        ).empty()
                                    }
                                ) deletePlaylist(playlistId)
                            }


                            true
                        } catch (e: Exception) {
                            false
                        }
                    }

                    ItemOperation.ERR -> return false
                }
            }

            UserType.EMAIL_USER -> {
                when (operation) {
                    ItemOperation.ADD -> {
                        false
                    }

                    ItemOperation.DELETE -> {
                        try {
                            dbQuery {
                                findPlaylistForEmailUser(
                                    playlistId = playlistId,
                                    userId = userId
                                ).forEach {
                                    it.delete()
                                }
                            }

                            deletePinnedPlaylistForEmailUser(
                                playlistId = playlistId,
                                userId = userId
                            )

                            CoroutineScope(Dispatchers.IO).launch {
                                if (
                                    dbQuery {
                                        findPlaylistForPasskeyUser(
                                            playlistId = playlistId,
                                            userId = userId
                                        ).empty() && findPlaylistForGoogleUser(
                                            playlistId = playlistId,
                                            userId = userId
                                        ).empty() && findPinnedPlaylistForPasskeyUser(
                                            playlistId = playlistId,
                                            userId = userId
                                        ).empty() && findPinnedPlaylistForGoogleUser(
                                            playlistId = playlistId,
                                            userId = userId
                                        ).empty()
                                    }
                                ) deletePlaylist(playlistId)
                            }


                            true
                        } catch (e: Exception) {
                            false
                        }
                    }

                    ItemOperation.ERR -> return false
                }
            }

            UserType.PASSKEY_USER -> {
                when (operation) {
                    ItemOperation.ADD -> {
                        false
                    }

                    ItemOperation.DELETE -> {
                        try {
                            dbQuery {
                                findPlaylistForPasskeyUser(
                                    playlistId = playlistId,
                                    userId = userId
                                ).forEach {
                                    it.delete()
                                }
                            }

                            deletePinnedPlaylistForPasskeyUser(
                                playlistId = playlistId,
                                userId = userId
                            )

                            CoroutineScope(Dispatchers.IO).launch {
                                if (
                                    dbQuery {
                                        findPlaylistForGoogleUser(
                                            playlistId = playlistId,
                                            userId = userId
                                        ).empty() && findPlaylistForEmailUser(
                                            playlistId = playlistId,
                                            userId = userId
                                        ).empty() && findPinnedPlaylistForGoogleUser(
                                            playlistId = playlistId,
                                            userId = userId
                                        ).empty() && findPinnedPlaylistForEmailUser(
                                            playlistId = playlistId,
                                            userId = userId
                                        ).empty()
                                    }
                                ) deletePlaylist(playlistId)
                            }


                            true
                        } catch (e: Exception) {
                            false
                        }
                    }

                    ItemOperation.ERR -> return false
                }
            }
        }
    }


    private fun findPlaylistForGoogleUser(
        playlistId: Long,
        userId: Long
    ) = GoogleUserPlaylist.find {
        GoogleUserPlaylistTable.playlistId eq playlistId and
                (GoogleUserPlaylistTable.userId eq userId)
    }


    private fun findPlaylistForEmailUser(
        playlistId: Long,
        userId: Long
    ) = EmailUserPlaylist.find {
        EmailUserPlaylistTable.playlistId eq playlistId and
                (EmailUserPlaylistTable.userId eq userId)
    }

    private fun findPlaylistForPasskeyUser(
        playlistId: Long,
        userId: Long
    ) = PasskeyUserPlaylist.find {
        PasskeyUserPlaylistTable.playlistId eq playlistId and
                (PasskeyUserPlaylistTable.userId eq userId)
    }

    private fun findPinnedPlaylistForGoogleUser(
        playlistId: Long,
        userId: Long
    ) = GoogleUserPinnedPlaylistTable.select {
        GoogleUserPinnedPlaylistTable.playlistId eq playlistId and
                (GoogleUserPinnedPlaylistTable.userId eq userId)
    }

    private fun findPinnedPlaylistForEmailUser(
        playlistId: Long,
        userId: Long
    ) = EmailUserPinnedPlaylistTable.select {
        EmailUserPinnedPlaylistTable.playlistId eq playlistId and
                (EmailUserPinnedPlaylistTable.userId eq userId)
    }

    private fun findPinnedPlaylistForPasskeyUser(
        playlistId: Long,
        userId: Long
    ) = PasskeyUserPinnedPlaylistTable.select {
        PasskeyUserPinnedPlaylistTable.playlistId eq playlistId and
                (PasskeyUserPinnedPlaylistTable.userId eq userId)
    }

    private suspend fun deletePinnedPlaylistForGoogleUser(
        playlistId: Long,
        userId: Long
    ) {
        dbQuery {
            GoogleUserPinnedPlaylistTable.deleteWhere {
                GoogleUserPinnedPlaylistTable.playlistId eq playlistId and
                        (GoogleUserPinnedPlaylistTable.userId eq userId)
            }
        }
    }

    private suspend fun deletePinnedPlaylistForPasskeyUser(
        playlistId: Long,
        userId: Long
    ) {
        dbQuery {
            PasskeyUserPinnedPlaylistTable.deleteWhere {
                PasskeyUserPinnedPlaylistTable.playlistId eq playlistId and
                        (PasskeyUserPinnedPlaylistTable.userId eq userId)
            }
        }
    }

    private suspend fun deletePinnedPlaylistForEmailUser(
        playlistId: Long,
        userId: Long
    ) {
        dbQuery {
            EmailUserPinnedPlaylistTable.deleteWhere {
                EmailUserPinnedPlaylistTable.playlistId eq playlistId and
                        (EmailUserPinnedPlaylistTable.userId eq userId)
            }
        }
    }

    private fun deletePlaylist(
        playlistId: Long
    ) {
        Playlist.find {
            PlaylistTable.id eq playlistId
        }.firstOrNull()?.delete()
    }
}

