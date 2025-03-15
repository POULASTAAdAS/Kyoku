package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.database.mapper.toDtoHeading
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoExploreType
import com.poulastaa.core.domain.model.DtoHeading
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.DtoViewPayload
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.domain.model.ViewType
import com.poulastaa.core.domain.repository.LocalViewOtherDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class RoomLocalViewOtherDatasource @Inject constructor(
    private val viewDao: ViewDao,
    private val root: RootDao,
) : LocalViewOtherDatasource {
    override suspend fun getSavedPrevViewSongIds(type: ViewType): List<SongId> = when (type) {
        ViewType.POPULAR_SONG_MIX -> viewDao.getExploreTypePrevData(DtoExploreType.POPULAR_SONG_MIX.id)
        ViewType.POPULAR_YEAR_MIX -> viewDao.getExploreTypePrevData(DtoExploreType.POPULAR_YEAR_MIX.id)
        ViewType.SAVED_ARTIST_SONG_MIX -> viewDao.getExploreTypePrevData(DtoExploreType.POPULAR_ARTIST_SONG_MIX.id)
        ViewType.DAY_TYPE_MIX -> viewDao.getExploreTypePrevData(DtoExploreType.DAY_TYPE_MIX.id)
        else -> emptyList()
    }

    override suspend fun getViewTypeData(
        type: ViewType,
        otherId: Long?,
    ): DtoViewPayload<DtoDetailedPrevSong>? {
        when (type) {
            ViewType.PLAYLIST -> otherId?.let {
                coroutineScope {
                    val playlistDef = async { viewDao.getPlaylistOnId(it) }
                    val songsDef = async { viewDao.getPlaylistPrevSongs(it) }

                    val playlist = playlistDef.await() ?: return@coroutineScope null
                    val songs = songsDef.await()

                    songs.map {
                        async {
                            val artist = root.getSongArtists(it.id).joinToString { it.name }

                            DtoDetailedPrevSong(
                                id = it.id,
                                title = it.title,
                                poster = it.poster,
                                artists = artist,
                                releaseYear = it.releaseYear
                            )
                        }
                    }.awaitAll()

                    DtoViewPayload(
                        heading = playlist.toDtoHeading(),
                        listOfSongs = songs
                    )
                }
            }
            ViewType.ALBUM -> otherId?.let {
                coroutineScope {
                    val albumDef = async { viewDao.getAlbumOnId(it) }
                    val songsDef = async { viewDao.getAlbumPrevSongs(it) }

                    val album = albumDef.await() ?: return@coroutineScope null
                    val songs = songsDef.await()

                    songs.map {
                        async {
                            val artist = root.getSongArtists(it.id).joinToString { it.name }

                            DtoDetailedPrevSong(
                                id = it.id,
                                title = it.title,
                                poster = it.poster,
                                artists = artist,
                                releaseYear = it.releaseYear
                            )
                        }
                    }.awaitAll()

                    DtoViewPayload(
                        heading = album.toDtoHeading(),
                        listOfSongs = songs
                    )
                }
            }
            ViewType.FAVOURITE -> {
                DtoViewPayload(
                    heading = DtoHeading(
                        type = ViewType.FAVOURITE,
                        name = "Favourite",
                    ),
                    listOfSongs = viewDao.getFavoriteSongs()
                )
            }

            ViewType.POPULAR_SONG_MIX -> TODO()
            ViewType.POPULAR_YEAR_MIX -> TODO()
            ViewType.SAVED_ARTIST_SONG_MIX -> TODO()
            ViewType.DAY_TYPE_MIX -> TODO()
            else -> return null
        }
    }

    override suspend fun saveViewTypeData(type: DtoExploreType, list: List<DtoSong>) {
        when (type) {
            DtoExploreType.POPULAR_SONG_MIX -> TODO()
            DtoExploreType.POPULAR_ARTIST_SONG_MIX -> TODO()
            DtoExploreType.POPULAR_YEAR_MIX -> TODO()
            DtoExploreType.DAY_TYPE_MIX -> TODO()
        }
    }
}