package com.poulastaa.suggestion.domain.repository

import com.poulastaa.core.domain.model.ReqUserPayload
import com.poulastaa.suggestion.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.suggestion.domain.model.DtoHome
import com.poulastaa.suggestion.domain.model.DtoRefresh
import com.poulastaa.suggestion.domain.model.OldRefresh

interface SuggestionRepository {
    suspend fun getHomeData(payload: ReqUserPayload): DtoHome?
    suspend fun getRefreshData(payload: ReqUserPayload, oldData: OldRefresh): DtoRefresh?
    suspend fun getAddSongToPlaylistData(payload: ReqUserPayload): List<DtoAddSongToPlaylistPageItem>?
}