package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.HomeDao
import com.poulastaa.core.database.entity.relation.PopularArtistSongRelationEntity
import com.poulastaa.core.database.mapper.toArtistSongEntity
import com.poulastaa.core.database.mapper.toDayTypeSongPrev
import com.poulastaa.core.database.mapper.toDayTypeSongPrevEntity
import com.poulastaa.core.database.mapper.toFavouriteArtistMixPrev
import com.poulastaa.core.database.mapper.toFavouriteArtistMixPrevEntity
import com.poulastaa.core.database.mapper.toPopularAlbumPrev
import com.poulastaa.core.database.mapper.toPopularAlbumPrevEntity
import com.poulastaa.core.database.mapper.toPopularSongArtistEntity
import com.poulastaa.core.database.mapper.toPopularSongFromYourTimePrev
import com.poulastaa.core.database.mapper.toPopularSongFromYourTimePrevEntity
import com.poulastaa.core.database.mapper.toPopularSongMixPrev
import com.poulastaa.core.database.mapper.toPopularSongMixPrevEntity
import com.poulastaa.core.database.mapper.toPopularSuggestArtistEntity
import com.poulastaa.core.database.mapper.toPrevArtistSong
import com.poulastaa.core.database.mapper.toSavedPlaylist
import com.poulastaa.core.database.mapper.toSuggestArtist
import com.poulastaa.core.domain.home.LocalHomeDatasource
import com.poulastaa.core.domain.model.DayType
import com.poulastaa.core.domain.model.HomeData
import com.poulastaa.core.domain.model.NewHome
import com.poulastaa.core.domain.utils.SavedPlaylist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomLocalHomeDatasource @Inject constructor(
    private val homeDao: HomeDao,
) : LocalHomeDatasource {
    override suspend fun storeNewHomeResponse(
        dayType: DayType,
        response: NewHome,
    ): Unit = withContext(Dispatchers.IO) {
        val popularSongMixPrevDef = async {
            homeDao.insertPopularSongMixPrevs(
                entrys = response.popularSongMixPrev.map {
                    it.toPopularSongMixPrevEntity()
                }
            )
        }
        val popularSongFromYourTimePrevDef = async {
            homeDao.insertPopularSongFromYourTimePrevs(
                entrys = response.popularSongFromYourTimePrev.map {
                    it.toPopularSongFromYourTimePrevEntity()
                }
            )
        }
        val favouriteArtistMixPrevDef = async {
            homeDao.insertFavouriteArtistMixPrevs(
                entrys = response.favouriteArtistMixPrev.map {
                    it.toFavouriteArtistMixPrevEntity()
                }
            )
        }
        val dayTypeSongDef = async {
            homeDao.insertDayTypeSongPrevs(
                entrys = response.dayTypeSong.map {
                    it.toDayTypeSongPrevEntity(dayType)
                }
            )
        }
        val popularAlbumDef = async {
            homeDao.insertPopularAlbumPrevs(
                entrys = response.popularAlbum.map {
                    it.toPopularAlbumPrevEntity()
                }
            )
        }
        val popularArtistDef = async {
            homeDao.insertPopularSuggestArtists(
                entrys = response.popularArtist.map {
                    it.toPopularSuggestArtistEntity()
                }
            )
        }

        val popularArtistSongDef = async {
            response.popularArtistSong.map { prevArtistSong ->
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

        popularSongMixPrevDef.await()
        popularSongFromYourTimePrevDef.await()
        favouriteArtistMixPrevDef.await()
        if (response.dayTypeSong.isNotEmpty()) dayTypeSongDef.await()
        popularAlbumDef.await()
        popularArtistDef.await()
        popularArtistSongDef.await()
    }

    override suspend fun isNewUser(): Boolean = homeDao.isNewUser().isEmpty()

    override suspend fun loadHomeStaticData(): HomeData = withContext(Dispatchers.IO) {
        val readPopularSongMixPrevDef = async {
            homeDao.readPopularSongMixPrev().map {
                it.toPopularSongMixPrev()
            }
        }
        val readPopularSongFromYourTimePrevDef = async {
            homeDao.readPopularSongFromYourTimePrev().map {
                it.toPopularSongFromYourTimePrev()
            }
        }

        val readFavouriteArtistMixPrevDef = async {
            homeDao.readFavouriteArtistMixPrev().map {
                it.toFavouriteArtistMixPrev()
            }
        }
        val readDayTypeSongDef = async {
            homeDao.readDayTypeSong().map {
                it.toDayTypeSongPrev()
            }
        }
        val readPopularAlbumDef = async {
            homeDao.readPopularAlbum().map {
                it.toPopularAlbumPrev()
            }
        }
        val readSuggestArtistDef = async {
            homeDao.readPopularSuggestArtist().map {
                it.toSuggestArtist()
            }
        }

        val readPopularArtistSongDef = async {
            homeDao.readPopularArtistSong().groupBy {
                it.artistId
            }.map { it.toPrevArtistSong() }
        }


        HomeData(
            popularSongMixPrev = readPopularSongMixPrevDef.await(),
            popularSongFromYourTimePrev = readPopularSongFromYourTimePrevDef.await(),
            favouriteArtistMixPrev = readFavouriteArtistMixPrevDef.await(),
            dayTypeSong = readDayTypeSongDef.await(),
            popularAlbum = readPopularAlbumDef.await(),
            suggestedArtist = readSuggestArtistDef.await(),
            popularArtistSong = readPopularArtistSongDef.await()
        )
    }

    override fun loadSavedPlaylist(): Flow<SavedPlaylist> = homeDao.getSavedPlaylists().map {
        it.groupBy { result -> result.id }
            .map { mapEntry -> mapEntry.toSavedPlaylist() }
    }.take(3)

    override suspend fun isArtistIsInLibrary(artistId: Long): Boolean =
        homeDao.isArtistIsInLibrary(artistId) != null

    override suspend fun isAlbumInLibrary(albumId: Long): Boolean =
        homeDao.isAlbumInLibrary(albumId) != null

    override suspend fun isSongInFavourite(songId: Long): Boolean =
        homeDao.isSongInFavourite(songId) != null
}