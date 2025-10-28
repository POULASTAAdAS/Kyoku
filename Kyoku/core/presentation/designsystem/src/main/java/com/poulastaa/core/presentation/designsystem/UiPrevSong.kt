package com.poulastaa.core.presentation.designsystem

import androidx.compose.runtime.Stable
import com.poulastaa.core.domain.utils.InternalId
import com.poulastaa.core.domain.utils.SongId

@Stable
data class UiPrevSong(
    val internalId: InternalId,
    val id: SongId,
    val title: String,
    val artist: String?,
    val coverImage: String? = null,
)
