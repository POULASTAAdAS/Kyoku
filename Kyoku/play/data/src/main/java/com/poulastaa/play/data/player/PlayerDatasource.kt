package com.poulastaa.play.data.player

import android.icu.text.DecimalFormat
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import com.poulastaa.core.domain.model.PlayerEvent
import com.poulastaa.core.domain.model.PlayerSong
import com.poulastaa.core.domain.model.PlayerState
import com.poulastaa.core.domain.repository.player.KyokuPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

class PlayerDatasource @Inject constructor(
    private val player: ExoPlayer,
) : Player.Listener, KyokuPlayer {
    init {
        player.addListener(this)
    }

    private val _playerUiState: MutableStateFlow<PlayerState> =
        MutableStateFlow(PlayerState.Initial)
    override val playerUiState = _playerUiState.asStateFlow()

    private var playJob: Job? = null

    override fun onEvent(event: PlayerEvent) {
        when (event) {
            PlayerEvent.PlayPause -> {
                if (player.isPlaying) pauseProgress()
                else playJob = startProgress()
            }

            PlayerEvent.PlayNext -> player.seekToNext()
            PlayerEvent.PlayPrev -> player.seekToPrevious()

            is PlayerEvent.SeekTo -> {
                val pos = (event.value * player.duration / 100f).toLong()
                player.seekTo(pos)
            }

            is PlayerEvent.SeekToSong -> player.seekTo(event.index, event.pos)

            PlayerEvent.Stop -> stopProgress()
        }
    }

    override fun addMediaItem(song: PlayerSong) {
        val item = MediaItem.Builder()
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .setMimeType(MimeTypes.APPLICATION_ID3)
            .setUri(song.masterPlaylistUrl)
            .setLiveConfiguration(
                MediaItem
                    .LiveConfiguration.Builder()
                    .setMaxPlaybackSpeed(1.02f)
                    .build()
            ).setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(song.title)
                    .setArtist(song.artist)
                    .setArtworkUri(Uri.parse(song.coverImage))
                    .build()
            )
            .setMediaId(song.id.toString())
            .build()

        player.addMediaItem(item)
        player.prepare()
    }

    override fun addMediaItem(list: List<PlayerSong>) {
        list.map { song ->
            MediaItem.Builder()
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .setMimeType(MimeTypes.APPLICATION_ID3)
                .setUri(song.masterPlaylistUrl)
                .setLiveConfiguration(
                    MediaItem
                        .LiveConfiguration.Builder()
                        .setMaxPlaybackSpeed(1.02f)
                        .build()
                ).setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        .setArtworkUri(Uri.parse(song.coverImage))
                        .build()
                )
                .setMediaId(song.songId.toString())
                .build()
        }.let {
            player.addMediaItems(it)
            player.prepare()
        }
    }

    private fun startProgress() = CoroutineScope(Dispatchers.Main).launch {
        player.play()

        while (true) {
            val progress = DecimalFormat("00.00")
                .format((player.currentPosition.toFloat() / player.duration * 100f))
                .toFloat()
            val second = millisecondsToMinutesAndSeconds(player.currentPosition)

            _playerUiState.value = PlayerState.ProgressBar(progress)
            _playerUiState.value = PlayerState.Progress(second)

            delay(300)
        }
    }

    private fun pauseProgress() {
        player.pause()
        playJob?.cancel()
    }

    private fun stopProgress() {
        playJob?.cancel()
        player.stop()
        player.clearMediaItems()
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_IDLE -> _playerUiState.value = PlayerState.Initial

            Player.STATE_READY -> _playerUiState.value =
                PlayerState.Ready(millisecondsToMinutesAndSeconds(player.duration))

            ExoPlayer.STATE_ENDED -> _playerUiState.value = PlayerState.Playing(isPlaying = false)

            else -> Unit
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _playerUiState.value = PlayerState.Playing(isPlaying = isPlaying)
    }

    override fun onTracksChanged(tracks: Tracks) {
        super.onTracksChanged(tracks)
        _playerUiState.value = PlayerState.CurrentlyPlaying(
            songId = player.currentMediaItem?.mediaId?.toLong() ?: -1L,
            hasPrev = player.hasPreviousMediaItem(),
            hasNext = player.hasNextMediaItem()
        )
    }

    private fun millisecondsToMinutesAndSeconds(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000.0
        val minutes = (totalSeconds / 60).toLong()
        val seconds = (totalSeconds % 60).toLong()

        return String.format(
            locale = Locale.Builder().build(),
            format = "%02d.%02d",
            minutes, seconds
        )
    }
}