package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.HomeDao
import com.poulastaa.core.database.entity.FavouriteEntity
import com.poulastaa.core.database.entity.relation.PopularArtistSongRelationEntity
import com.poulastaa.core.database.entity.relation.SongAlbumRelationEntity
import com.poulastaa.core.database.mapper.toAlbumEntity
import com.poulastaa.core.database.mapper.toArtistEntity
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
import com.poulastaa.core.database.mapper.toSongEntity
import com.poulastaa.core.database.mapper.toSuggestArtist
import com.poulastaa.core.domain.home.LocalHomeDatasource
import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.DayType
import com.poulastaa.core.domain.model.HomeData
import com.poulastaa.core.domain.model.NewHome
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.utils.SavedAlbum
import com.poulastaa.core.domain.utils.SavedPlaylist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomLocalHomeDatasource @Inject constructor(
    private val commonDao: CommonDao,
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

        if (response.dayTypeSong.isNotEmpty()) dayTypeSongDef.await()
        listOf(
            popularSongMixPrevDef,
            popularSongFromYourTimePrevDef,
            favouriteArtistMixPrevDef,
            popularAlbumDef,
            popularArtistDef,
            popularArtistSongDef
        ).awaitAll()
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

    override fun loadSavedPlaylist(): Flow<SavedPlaylist> = commonDao.getAllSavedPlaylist().map {
        it.groupBy { result -> result.id }
            .map { mapEntry -> mapEntry.toSavedPlaylist() }.take(3)
    }

    override fun loadSavedAlbum(): Flow<SavedAlbum> =
        commonDao.getAllSavedAlbum().map { it.take(3) }

    override suspend fun isArtistIsInLibrary(artistId: Long): Boolean =
        homeDao.isArtistIsInLibrary(artistId) != null

    override suspend fun isAlbumInLibrary(albumId: Long): Boolean =
        homeDao.isAlbumInLibrary(albumId) != null

    override suspend fun isSongInFavourite(songId: Long): Boolean =
        homeDao.isSongInFavourite(songId) != null

    override suspend fun isSongInDatabase(id: Long): Boolean = homeDao.getSong(id) != null

    override suspend fun addSong(song: Song) =
        commonDao.insertSong(song = song.toSongEntity())

    override suspend fun insertIntoFavourite(id: Long) =
        commonDao.insertOneIntoFavourite(entry = FavouriteEntity(id))

    override suspend fun removeSongFromFavourite(id: Long) =
        commonDao.deleteSongFromFavourite(FavouriteEntity(id))

    override suspend fun followArtist(artist: Artist) =
        commonDao.insertArtist(artist.toArtistEntity())

    override suspend fun unFollowArtist(id: Long) =
        commonDao.deleteArtist(id)

    override suspend fun saveAlbum(data: AlbumWithSong): Unit = coroutineScope {
        val albumEntry = data.album.toAlbumEntity()
        val songEntry = data.listOfSong.map { it.toSongEntity() }

        val albumDef = async { commonDao.insertAlbum(albumEntry) }
        val songDef = async { commonDao.insertSongs(songEntry) }

        albumDef.await()
        songDef.await()

        val relation = songEntry.map { it.id }.map {
            SongAlbumRelationEntity(
                albumId = albumEntry.id,
                songId = it
            )
        }

        commonDao.insertSongAlbumRelation(relation)
    }

    override suspend fun removeAlbum(id: Long) =
        commonDao.deleteAlbum(id)
}