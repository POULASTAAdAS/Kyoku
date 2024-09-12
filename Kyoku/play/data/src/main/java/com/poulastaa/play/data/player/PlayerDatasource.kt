package com.poulastaa.play.data.player

//import androidx.media3.common.MediaItem
//import androidx.media3.common.MimeTypes
//import androidx.media3.common.Player
//import androidx.media3.exoplayer.ExoPlayer
//import com.poulastaa.play.domain.PlayerEvent
//import com.poulastaa.play.domain.PlayerState
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//class PlayerDatasource @Inject constructor(
//    private val player: ExoPlayer,
//    private val applicationScope: CoroutineScope
//) : Player.Listener {
//    init {
//        player.addListener(this)
//    }
//
//    private val _playerUiState: MutableStateFlow<PlayerState> =
//        MutableStateFlow(PlayerState.Initial)
//    val playerUiState = _playerUiState.asStateFlow()
//
//    private var playJob: Job? = null
//
//    fun onEvent(event: PlayerEvent) {
//        when (event) {
//            PlayerEvent.PlayPause -> {
//                if (player.isPlaying) {
//                    player.pause()
//                    stopProgress()
//                } else {
//                    player.play()
//                    playJob = startProgress()
//                    _playerUiState.value = PlayerState.Playing(isPlaying = true)
//                }
//            }
//        }
//    }
//
//    fun addMediaItem(url: String) {
//        val item = MediaItem.Builder()
//            .setMimeType(MimeTypes.APPLICATION_M3U8)
//            .setMimeType(MimeTypes.APPLICATION_ID3)
//            .setUri(url)
//            .setLiveConfiguration(
//                MediaItem
//                    .LiveConfiguration.Builder()
//                    .setMaxPlaybackSpeed(1.02f)
//                    .build()
//            )/*.setMediaMetadata(
//                MediaMetadata.Builder()
//                    .setTitle()
//            )*/
////            .setMediaId()
//            .build()
//
//        player.addMediaItem(item)
//    }
//
//    private fun startProgress() = applicationScope.launch {
//        while (true) {
//            delay(300)
////            player.currentLiveOffset
//
//        }
//    }
//
//    private fun stopProgress() {
//        playJob?.cancel()
//        _playerUiState.value = PlayerState.Playing(isPlaying = false)
//    }
//}