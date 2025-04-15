package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.database.entity.EntityExplore
import com.poulastaa.core.database.entity.EntityFavourite
import com.poulastaa.core.database.entity.EntityRelationSongPlaylist
import com.poulastaa.core.database.mapper.toDtoExploreType
import com.poulastaa.core.database.mapper.toDtoHeading
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoExploreType
import com.poulastaa.core.domain.model.DtoHeading
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.DtoViewPayload
import com.poulastaa.core.domain.model.PlaylistId
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
    private val common: RoomCommonInsertDatasource,
) : LocalViewOtherDatasource {
    override suspend fun getSavedPrevViewSongIds(type: ViewType): List<SongId> = when (type) {
        ViewType.POPULAR_SONG_MIX,
        ViewType.POPULAR_YEAR_MIX,
        ViewType.SAVED_ARTIST_SONG_MIX,
        ViewType.DAY_TYPE_MIX,
            -> type.toDtoExploreType()?.id?.let { viewDao.getExploreTypePrevData(it) }
            ?: emptyList()

        else -> emptyList()
    }

    override suspend fun getViewTypeData(
        type: ViewType,
        otherId: Long?,
    ): DtoViewPayload<DtoDetailedPrevSong>? = when (type) {
        ViewType.PLAYLIST -> otherId?.let {
            coroutineScope {
                val playlistDef = async { viewDao.getPlaylistOnId(it) }
                val songsDef = async { viewDao.getPlaylistPrevSongs(it) }

                val playlist = playlistDef.await() ?: return@coroutineScope null
                val dbSongs = songsDef.await()

                val songs = dbSongs.map {
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
                val dbSongs = songsDef.await()

                val songs = dbSongs.map {
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

        ViewType.FAVOURITE -> DtoViewPayload(
            heading = DtoHeading(
                type = ViewType.FAVOURITE,
                name = "Favourite",
            ),
            listOfSongs = viewDao.getFavoriteSongs()
        )

        ViewType.POPULAR_SONG_MIX,
        ViewType.POPULAR_YEAR_MIX,
        ViewType.SAVED_ARTIST_SONG_MIX,
        ViewType.DAY_TYPE_MIX,
            -> type.toDtoExploreType()?.id?.let {
            DtoViewPayload(
                heading = DtoHeading(
                    type = type,
                    name = type.name,
                ),
                listOfSongs = viewDao.getExploreTypeSongs(
                    typeId = it
                )
            )
        }

        else -> null
    }

    override suspend fun saveViewTypeData(list: List<DtoSong>, playlistId: PlaylistId?) {
        savedSong(list)

        if (playlistId != null) root.insertRelationSongPlaylist(
            list.map {
                EntityRelationSongPlaylist(
                    songId = it.id,
                    playlistId = playlistId
                )
            }
        ) else root.insertFavourite(list.map { EntityFavourite(it.id) })
    }

    override suspend fun saveViewTypeData(type: DtoExploreType, list: List<DtoSong>) {
        savedSong(list)

        viewDao.insertExploreTypeSong(
            list.map {
                EntityExplore(
                    typeId = type.id,
                    dataId = it.id
                )
            }
        )
    }

    private suspend fun savedSong(list: List<DtoSong>) {
        common.insertSong(list)
    }
}