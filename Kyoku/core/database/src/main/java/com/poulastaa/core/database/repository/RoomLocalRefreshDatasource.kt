package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.RefreshDao
import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.entity.EntityPrevExplore
import com.poulastaa.core.database.entity.EntityRelationSuggestedSongByArtist
import com.poulastaa.core.database.mapper.toDtoSuggestedArtistSongRelation
import com.poulastaa.core.database.mapper.toEntityPrevAlbum
import com.poulastaa.core.database.mapper.toEntityPrevArtist
import com.poulastaa.core.database.mapper.toEntityPrevSong
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoExploreType
import com.poulastaa.core.domain.model.DtoPrevAlbum
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.model.DtoRelationSuggestedArtistSong
import com.poulastaa.core.domain.model.DtoSuggestedArtistSongRelation
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.domain.repository.LocalRefreshDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class RoomLocalRefreshDatasource @Inject constructor(
    private val root: RootDao,
    private val refresh: RefreshDao,
    private val scope: CoroutineScope,
) : LocalRefreshDatasource {
    override suspend fun clearSuggestedData() {
        scope.launch {
            listOf(
                async { refresh.clearPrevExploreType() },
                async { refresh.clearSuggestedArtist() },
                async { refresh.clearSuggestedAlbum() },
                async { refresh.clearSuggestedArtistSong() },
            ).awaitAll()
        }
    }

    override suspend fun updatePopularSongMix(list: List<DtoPrevSong>) {
        storePrevSong(list).also {
            root.insertPrevExploreType(list.map {
                EntityPrevExplore(
                    typeId = DtoExploreType.POPULAR_SONG_MIX.id,
                    dataId = it.id
                )
            })
        }
    }

    override suspend fun updatePopularArtistMix(list: List<DtoPrevSong>) {
        storePrevSong(list).also {
            root.insertPrevExploreType(list.map {
                EntityPrevExplore(
                    typeId = DtoExploreType.POPULAR_ARTIST_SONG_MIX.id,
                    dataId = it.id
                )
            })
        }
    }

    override suspend fun updateOldGem(list: List<DtoPrevSong>) {
        storePrevSong(list).also {
            root.insertPrevExploreType(list.map {
                EntityPrevExplore(
                    typeId = DtoExploreType.POPULAR_YEAR_MIX.id,
                    dataId = it.id
                )
            })
        }
    }

    override suspend fun updateSuggestedArtist(list: List<DtoPrevArtist>) {
        root.insertPrevArtist(list.map { it.toEntityPrevArtist() })
    }

    override suspend fun updateSuggestedAlbum(list: List<DtoPrevAlbum>) {
        root.insertPrevAlbum(list.map { it.toEntityPrevAlbum() })
    }

    override suspend fun storePrevSong(list: List<DtoPrevSong>) {
        root.insertPrevSong(list.map { it.toEntityPrevSong() })
    }

    override suspend fun storeSuggestedArtist(list: List<DtoPrevArtist>) {
        root.insertPrevArtist(list.map { it.toEntityPrevArtist() })
    }

    override suspend fun updateRelationSuggestedArtistSong(list: List<DtoRelationSuggestedArtistSong>) {
        root.insertRelationSuggestedSongByArtist(list.map { dto ->
            dto.list.map {
                EntityRelationSuggestedSongByArtist(
                    artistId = dto.artistId,
                    songId = it
                )
            }
        }.flatten())
    }

    override suspend fun getSavedMostPopularSongs(): List<SongId> =
        refresh.getExploreTypeIdList(DtoExploreType.POPULAR_SONG_MIX.id)

    override suspend fun getSavedPopularArtistMix(): List<SongId> =
        refresh.getExploreTypeIdList(DtoExploreType.POPULAR_ARTIST_SONG_MIX.id)

    override suspend fun getSavedOldGem(): List<SongId> =
        refresh.getExploreTypeIdList(DtoExploreType.POPULAR_YEAR_MIX.id)

    override suspend fun getSavedSuggestedArtist(): List<ArtistId> =
        refresh.getSavedSuggestedArtist()

    override suspend fun getSavedSuggestedAlbum(): List<ArtistId> =
        refresh.getSavedSuggestedAlbum()

    override suspend fun getOldSuggestedArtistSongRelation(): List<DtoSuggestedArtistSongRelation> =
        refresh.getSuggestedArtistSong().map { it.toDtoSuggestedArtistSongRelation() }
}