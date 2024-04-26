package com.poulastaa.kyoku.data.model.screens.song_view

import com.poulastaa.kyoku.data.model.UiEvent

sealed class SongViewUiEvent {
    data class EmitToast(val message: String) : SongViewUiEvent()
    data object SomethingWentWrong : SongViewUiEvent()

    sealed class PlayControlClick : SongViewUiEvent() {
        data class DownloadClick(
            val listId: Long = -1,
            val name: String = "",
            val type: UiEvent.PlayType
        ) : PlayControlClick()

        data class PlayClick(
            val listId: Long = -1,
            val name: String = "",
            val type: UiEvent.PlayType
        ) : PlayControlClick()

        data class ShuffleClick(
            val listId: Long = 1,
            val name: String = "",
            val type: UiEvent.PlayType
        ) : PlayControlClick()

        data class SongPlayClick(
            val listId: Long = -1,
            val songId: Long = -1,
            val name: String = "",
            val type: UiEvent.PlayType
        ) : PlayControlClick()
    }

    sealed class ItemClick : SongViewUiEvent() {
        data class ViewAllFromArtist(val name: String) : ItemClick()
    }
}