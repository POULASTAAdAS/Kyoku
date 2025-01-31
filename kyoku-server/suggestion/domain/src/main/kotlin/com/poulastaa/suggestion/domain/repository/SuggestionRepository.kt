package com.poulastaa.suggestion.domain.repository

import com.poulastaa.core.domain.model.ReqUserPayload
import com.poulastaa.suggestion.domain.model.DtoHome

interface SuggestionRepository {
    suspend fun getHomeData(payload: ReqUserPayload): DtoHome?
}