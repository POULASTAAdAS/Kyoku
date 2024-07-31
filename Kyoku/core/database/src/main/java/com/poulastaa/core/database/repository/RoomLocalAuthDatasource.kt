package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.HomeDao
import com.poulastaa.core.database.entity.relation.PopularArtistSongRelationEntity
import com.poulastaa.core.database.entity.relation.SongPlaylistRelationEntity
import com.poulastaa.core.database.mapper.toAlbumEntity
import com.poulastaa.core.database.mapper.toArtistEntity
import com.poulastaa.core.database.mapper.toArtistSongEntity
import com.poulastaa.core.database.mapper.toDayTypeSongPrevEntity
import com.poulastaa.core.database.mapper.toFavouriteArtistMixPrevEntity
import com.poulastaa.core.database.mapper.toPlaylistEntity
import com.poulastaa.core.database.mapper.toPopularAlbumPrevEntity
import com.poulastaa.core.database.mapper.toPopularSongArtistEntity
import com.poulastaa.core.database.mapper.toPopularSongFromYourTimePrevEntity
import com.poulastaa.core.database.mapper.toPopularSongMixPrevEntity
import com.poulastaa.core.database.mapper.toPopularSuggestArtistEntity
import com.poulastaa.core.database.mapper.toSongEntity
import com.poulastaa.core.domain.auth.LocalAuthDatasource
import com.poulastaa.core.domain.model.DayType
import com.poulastaa.core.domain.model.LogInData
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class RoomLocalAuthDatasource @Inject constructor(
    private val commonDao: CommonDao,
    private val homeDao: HomeDao,
) : LocalAuthDatasource {
    override suspend fun storeData(
        dayType: DayType,
        data: LogInData,
    ): Unit = coroutineScope {
        val popularSongMixPrevDef = async {
            homeDao.insertPopularSongMixPrevs(
                entrys = data.popularSongMixPrev.map {
                    it.toPopularSongMixPrevEntity()
                }
            )
        }
        val popularSongFromYourTimePrevDef = async {
            homeDao.insertPopularSongFromYourTimePrevs(
                entrys = data.popularSongFromYourTimePrev.map {
                    it.toPopularSongFromYourTimePrevEntity()
                }
            )
        }
        val favouriteArtistMixPrevDef = async {
            homeDao.insertFavouriteArtistMixPrevs(
                entrys = data.favouriteArtistMixPrev.map {
                    it.toFavouriteArtistMixPrevEntity()
                }
            )
        }
        val dayTypeSongDef = async {
            homeDao.insertDayTypeSongPrevs(
                entrys = data.dayTypeSong.map {
                    it.toDayTypeSongPrevEntity(dayType)
                }
            )
        }
        val popularAlbumDef = async {
            homeDao.insertPopularAlbumPrevs(
                entrys = data.popularAlbum.map {
                    it.toPopularAlbumPrevEntity()
                }
            )
        }
        val popularArtistDef = async {
            homeDao.insertPopularSuggestArtists(
                entrys = data.popularArtist.map {
                    it.toPopularSuggestArtistEntity()
                }
            )
        }
        val popularArtistSongDef = async {
            data.popularArtistSong.map { prevArtistSong ->
                async {
                    val artistSongsDef = async {
                        homeDao.insertPopularSongArtist(prevArtistSong.artist.toPopularSongArtistEntity())
                    }

                    val popularSongArtistsDef = async {
                        homeDao.insertArtistSongs(
                            prevArtistSong.songs.map {
                                it.toArtistSongEntity()
                            }
                        )
                    }

                    artistSongsDef.await()
                    popularSongArtistsDef.await()

                    prevArtistSong.songs.map {
                        PopularArtistSongRelationEntity(
                            artistId = prevArtistSong.artist.id,
                            songId = it.songId
                        )
                    }.let {
                        homeDao.insertPopularArtistSongRelations(it)
                    }
                }
            }.awaitAll()
        }
        val savedPlaylistDef = async {
            data.savedPlaylist.map { playlistData ->
                async {
                    val playlist = async {
                        val entry = playlistData.toPlaylistEntity()

                        commonDao.insertPlaylist(playlist = entry)

                        entry.id
                    }

                    val songsDef = async {
                        val songs = playlistData.listOfSong.map { it.toSongEntity() }

                        commonDao.insertSongs(songs = songs)
                        songs.map { it.id }
                    }

                    val playlistId = playlist.await()
                    val songIdList = songsDef.await()

                    val relation = songIdList.map {
                        SongPlaylistRelationEntity(
                            songId = it,
                            playlistId = playlistId
                        )
                    }

                    commonDao.insertSongPlaylistRelation(relation)
                }
            }.awaitAll()
        }
        val savedAlbumDef = async {
            val entrys = data.savedAlbum.map { album ->
                album.toAlbumEntity()
            }

            commonDao.insertAlbums(entrys)
        }
        val savedArtistDef = async {
            val entrys = data.savedArtist.map { artist ->
                artist.toArtistEntity()
            }

            commonDao.insertArtists(entrys)
        }


        listOf(
            popularSongMixPrevDef,
            popularSongFromYourTimePrevDef,
            favouriteArtistMixPrevDef,
            dayTypeSongDef,
            popularAlbumDef,
            popularArtistDef,
            popularArtistSongDef,
            savedPlaylistDef,
            savedAlbumDef,
            savedArtistDef
        ).awaitAll()
    }
}