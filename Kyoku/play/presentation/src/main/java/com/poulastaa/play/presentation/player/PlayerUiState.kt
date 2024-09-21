package com.poulastaa.play.presentation.player

import com.poulastaa.core.domain.RepeatState
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.song_artist.SongArtistUiArtist

data class PlayerUiState(
    val isData: Boolean = false,
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val isPlayerExtended: Boolean = false,

    val queue: List<PlayerUiSong> = emptyList(),
    val info: PlayerUiInfo = PlayerUiInfo(),
)

data class PlayerUiInfo(
    val currentPlayingIndex: Int = 0,
    val currentProgress: String = "0:0",
    val endTime: String = "0:0",
    val progress: Float = 0f,
    val isShuffledEnabled: Boolean = false,
    val repeatState: RepeatState = RepeatState.IDLE,
    val isPlaying: Boolean = false,
    val hasNext: Boolean = false,
    val hasPrev: Boolean = false,
    val more: MorePlayerInfo = MorePlayerInfo(),
    val artist: PlayerSongArtist = PlayerSongArtist(),
)

data class MorePlayerInfo(
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val id: Long = -1,
    val title: String = "",
    val coverImage: List<String> = emptyList(),
)

data class PlayerSongArtist(
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val songId: Long = -1,
    val artist: List<SongArtistUiArtist> = emptyList(),
)