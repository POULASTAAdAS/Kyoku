package com.poulastaa.kyoku.domain.player.service

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import com.poulastaa.kyoku.data.model.screens.player.PlayerUiEvent
import com.poulastaa.kyoku.data.model.screens.player.PlayerUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AudioServiceHandler @Inject constructor(
    private val player: ExoPlayer
) : Player.Listener {
    init {
        player.addListener(this)
    }

    private val _playerUiState: MutableStateFlow<PlayerUiState> =
        MutableStateFlow(PlayerUiState.Initial)
    val playerUiState: StateFlow<PlayerUiState> = _playerUiState.asStateFlow()

    private var job: Job? = null

    fun addOneMediaItem(mediaItem: MediaItem) {
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    fun addMultipleMediaItem(list: List<MediaItem>) {
        player.setMediaItems(list.toMutableList())
        player.prepare()
    }

    suspend fun onEvent(event: PlayerUiEvent) {
        when (event) {
            PlayerUiEvent.Backward -> player.seekBack()

            PlayerUiEvent.Forward -> player.seekForward()

            PlayerUiEvent.SeekToPrev -> {
                player.seekToPrevious()

                if (!player.isPlaying) CoroutineScope(Dispatchers.Main).launch {
                    player.prepare()
                    playPause()
                }
            }

            PlayerUiEvent.SeekToNext -> {
                player.seekToNext()

                if (!player.isPlaying) CoroutineScope(Dispatchers.Main).launch {
                    player.prepare()
                    playPause()
                }
            }

            PlayerUiEvent.PlayPause -> playPause()

            is PlayerUiEvent.SeekTo -> player.seekTo(event.index)

            is PlayerUiEvent.SeekToSong -> {
                player.seekTo(event.index, event.pos)

                if (!player.isPlaying) CoroutineScope(Dispatchers.Main).launch {
                    playPause()
                }
            }

            is PlayerUiEvent.SelectedSongChange -> {
                when (event.index) {
                    player.currentMediaItemIndex -> playPause()
                    else -> {
                        player.seekToDefaultPosition(event.index)
                        player.playWhenReady = true

                        _playerUiState.value = PlayerUiState.Playing(isPlaying = true)

                        startProgress()
                    }
                }
            }

            PlayerUiEvent.Stop -> {
                stopProgress()
                player.stop()
            }

            is PlayerUiEvent.UpdateProgress -> {
                player.seekTo((player.duration * event.value).toLong())
            }
        }
    }

    private suspend fun playPause() {
        if (player.isPlaying) {
            player.pause()
            stopProgress()
        } else {
            player.play()
            _playerUiState.value = PlayerUiState.Playing(isPlaying = true)
            startProgress()
        }
    }

    private suspend fun startProgress() = job.run {
        while (true) {
            delay(300)
            if (player.contentPosition >= 0)
                _playerUiState.value = PlayerUiState.Progress(value = player.currentPosition)
        }
    }

    private fun stopProgress() {
        job?.cancel()
        _playerUiState.value = PlayerUiState.Playing(isPlaying = false)
    }


    override fun onPlaybackStateChanged(playbackState: Int) {
        Log.d("playbackState", playbackState.toString())

        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _playerUiState.value =
                PlayerUiState.Buffering(player.contentPosition)

            ExoPlayer.STATE_READY -> _playerUiState.value = PlayerUiState.Ready(player.duration)

            ExoPlayer.STATE_ENDED -> _playerUiState.value = PlayerUiState.Playing(isPlaying = false)
            ExoPlayer.STATE_IDLE -> _playerUiState.value = PlayerUiState.Playing(isPlaying = false)
            ExoPlayer.COMMAND_SEEK_TO_NEXT -> _playerUiState.value =
                PlayerUiState.Playing(isPlaying = true)

            ExoPlayer.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM -> _playerUiState.value =
                PlayerUiState.Playing(isPlaying = true)

            ExoPlayer.COMMAND_SEEK_TO_MEDIA_ITEM -> _playerUiState.value =
                PlayerUiState.Playing(isPlaying = true)

            else -> Unit
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _playerUiState.value = PlayerUiState.Playing(isPlaying)

        if (isPlaying) CoroutineScope(Dispatchers.Main).launch {
            startProgress()
        }
        else stopProgress()
    }

    override fun onTracksChanged(tracks: Tracks) {
        super.onTracksChanged(tracks)

        _playerUiState.value =
            PlayerUiState.CurrentPlayingSongId(player.currentMediaItem?.mediaId?.toLong() ?: -1L)
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        if (error.errorCode == 2004) {
            if (player.hasNextMediaItem()) {
                player.seekToNext()
                player.prepare()
                CoroutineScope(Dispatchers.Main).launch {
                    playPause()
                }
            }
            else {
                player.seekToDefaultPosition(0)
                player.prepare()
                CoroutineScope(Dispatchers.Main).launch {
                    playPause()
                }
            }
        }
    }
}