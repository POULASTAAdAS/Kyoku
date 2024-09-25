package com.poulastaa.play.presentation.root_drawer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.PlayType
import com.poulastaa.core.domain.model.PlayerEvent
import com.poulastaa.core.domain.model.PlayerState
import com.poulastaa.core.domain.repository.player.KyokuPlayer
import com.poulastaa.core.domain.repository.player.PlayerRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.player.PlayerUiEvent
import com.poulastaa.play.presentation.player.PlayerUiState
import com.poulastaa.play.presentation.song_artist.toSongArtistUiArtist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repo: PlayerRepository,
    private val player: KyokuPlayer,
) : ViewModel() {
    var state by mutableStateOf(PlayerUiState())
        private set

    private val _uiEvent = Channel<RootDrawerUiAction.EmitToast>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var loadArtistJob: Job? = null
    private var loadInfoJob: Job? = null

    init {
        init()
        loadPlayingData(true)
    }

    private fun init() {
        viewModelScope.launch {
            player.playerUiState.collectLatest { playerState ->
                when (playerState) {
                    is PlayerState.ProgressBar -> {
                        state = state.copy(
                            info = state.info.copy(
                                progress = playerState.value
                            )
                        )
                    }

                    is PlayerState.Progress -> {
                        state = state.copy(
                            info = state.info.copy(
                                currentProgress = playerState.value
                            )
                        )
                    }

                    is PlayerState.Ready -> {
                        state = state.copy(
                            info = state.info.copy(
                                endTime = playerState.totalTime
                            )
                        )
                    }

                    is PlayerState.Playing -> {
                        state = state.copy(
                            info = state.info.copy(
                                isPlaying = playerState.isPlaying
                            )
                        )
                    }

                    is PlayerState.CurrentlyPlaying -> {
                        state = state.copy(
                            info = state.info.copy(
                                hasPrev = playerState.hasPrev,
                                hasNext = playerState.hasNext
                            )
                        )

                        state = state.copy(
                            queue = state.queue.map { uiSong ->
                                if (uiSong.songId == playerState.songId) uiSong.copy(isPlaying = true)
                                else uiSong.copy(isPlaying = false)
                            }
                        )

                        state.queue
                            .firstOrNull { it.songId == playerState.songId }
                            ?.let { song ->
                                state = state.copy(
                                    info = state.info.copy(
                                        currentPlayingIndex = song.index.dec(),
                                    )
                                )
                            }
                    }

                    else -> Unit
                }
            }
        }
    }

    fun onPlayerEvent(event: PlayerUiEvent) {
        when (event) {
            is PlayerUiEvent.PlayOperation -> {
                when (event) {
                    is PlayerUiEvent.PlayOperation.PlayAll -> {
                        when (event.type) {
                            PlayType.PLAYLIST,
                            PlayType.ALBUM,
                            -> {
                                if (state.info.more.id == event.id) return player.onEvent(
                                    PlayerEvent.SeekToSong(0, 0)
                                )
                            }

                            PlayType.FEV,
                            PlayType.ARTIST_MIX,
                            PlayType.POPULAR_MIX,
                            PlayType.OLD_MIX,
                            -> {
                                if (event.type == state.info.other.playType)
                                    return player.onEvent(PlayerEvent.SeekToSong(0, 0))
                            }

                            PlayType.IDLE -> return
                        }

                        state = state.copy(
                            isData = false,
                            loadingState = DataLoadingState.LOADING
                        )

                        viewModelScope.launch {
                            player.onEvent(PlayerEvent.Stop)

                            when (val result = repo.loadData(event.id, event.type)) {
                                is Result.Error -> {
                                    when (result.error) {
                                        DataError.Network.NO_INTERNET -> _uiEvent.send(
                                            RootDrawerUiAction.EmitToast(
                                                UiText.StringResource(
                                                    R.string.error_no_internet
                                                )
                                            )
                                        )

                                        else -> _uiEvent.send(
                                            RootDrawerUiAction.EmitToast(
                                                UiText.StringResource(
                                                    R.string.error_something_went_wrong
                                                )
                                            )
                                        )
                                    }
                                }

                                is Result.Success -> loadPlayingData()
                            }
                        }
                    }

                    is PlayerUiEvent.PlayOperation.ShuffleAll -> {

                    }

                    is PlayerUiEvent.PlayOperation.PlayOne -> {
                        when (event.type) {
                            PlayType.PLAYLIST,
                            PlayType.ALBUM,
                            -> {
                                if (state.info.other.otherId == event.otherId) return onPlayerEvent(
                                    PlayerUiEvent.PlayBackController.OnQueueSongClick(
                                        event.songId
                                    )
                                )

                                state = state.copy(
                                    isData = false,
                                    loadingState = DataLoadingState.LOADING,
                                )

                                viewModelScope.launch {
                                    player.onEvent(PlayerEvent.Stop)

                                    when (val result = repo.loadData(event.otherId, event.type)) {
                                        is Result.Error -> {
                                            when (result.error) {
                                                DataError.Network.NO_INTERNET -> _uiEvent.send(
                                                    RootDrawerUiAction.EmitToast(
                                                        UiText.StringResource(
                                                            R.string.error_no_internet
                                                        )
                                                    )
                                                )

                                                else -> _uiEvent.send(
                                                    RootDrawerUiAction.EmitToast(
                                                        UiText.StringResource(
                                                            R.string.error_something_went_wrong
                                                        )
                                                    )
                                                )
                                            }
                                        }

                                        is Result.Success -> {
                                            val dataDef = async { repo.getSongs().first() }
                                            val infoDef = async { repo.getInfo().first() }

                                            val data = dataDef.await()

                                            val index = data.indexOfFirst {
                                                it.songId == event.songId
                                            }

                                            player.addMediaItem(data)
                                            player.onEvent(PlayerEvent.SeekToSong(index))

                                            state = state.copy(
                                                queue = data.map { it.toPlayerUiSong() }
                                            )

                                            val info = infoDef.await()

                                            state = state.copy(
                                                info = info.toPlayerUiInfo()
                                            )
                                        }
                                    }

                                    state = state.copy(
                                        isData = state.queue.isNotEmpty(),
                                        loadingState = DataLoadingState.LOADED,
                                    )
                                }
                            }

                            PlayType.FEV -> {

                            }

                            PlayType.ARTIST_MIX -> {

                            }

                            PlayType.POPULAR_MIX -> {

                            }

                            PlayType.OLD_MIX -> {

                            }

                            PlayType.IDLE -> Unit
                        }
                    }
                }
            }

            PlayerUiEvent.OnPlayerExtendClick -> {
                state = state.copy(
                    isPlayerExtended = true
                )
            }

            PlayerUiEvent.OnPlayerShrinkClick -> {
                state = state.copy(
                    isPlayerExtended = false
                )
            }

            is PlayerUiEvent.PlayBackController -> {
                when (event) {
                    PlayerUiEvent.PlayBackController.OnPlayPrevClick -> player.onEvent(PlayerEvent.PlayPrev)
                    PlayerUiEvent.PlayBackController.OnPlayNextClick -> player.onEvent(PlayerEvent.PlayNext)
                    is PlayerUiEvent.PlayBackController.OnPlayPause -> player.onEvent(PlayerEvent.PlayPause)
                    is PlayerUiEvent.PlayBackController.SeekTo -> player.onEvent(
                        PlayerEvent.SeekTo(
                            event.pos
                        )
                    )

                    is PlayerUiEvent.PlayBackController.OnQueueSongClick -> {
                        val songIndex =
                            state.queue.indexOfFirst { it.songId == event.songId }
                        player.onEvent(PlayerEvent.SeekToSong(songIndex))
                    }
                }
            }

            is PlayerUiEvent.GetSongInfo -> {
                if (state.info.artist.songId == event.songId) return

                state = state.copy(
                    info = state.info.copy(
                        artist = state.info.artist.copy(
                            songId = event.songId,
                            loadingState = DataLoadingState.LOADING,
                        )
                    )
                )

                loadArtistJob?.cancel()
                loadArtistJob = loadArtist(event.songId)
            }

            PlayerUiEvent.ClosePlayer -> {
                state = PlayerUiState()
                repo.close()
                player.onEvent(PlayerEvent.Stop)
            }

            else -> Unit
        }
    }

    private fun loadPlayingData(isInitial: Boolean = false) {
        loadSongs(isInitial)
        loadInfo()
    }

    private fun loadSongs(isInitial: Boolean = false) = viewModelScope.launch {
        val data = repo.getSongs().first()

        player.addMediaItem(data)
        if (!isInitial) player.onEvent(PlayerEvent.PlayPause)

        state = state.copy(
            isData = data.isNotEmpty(),
            loadingState = DataLoadingState.LOADED,
            queue = data.map { it.toPlayerUiSong() }
        )
    }

    private fun loadInfo() = viewModelScope.launch {
        val info = repo.getInfo().first()

        state = state.copy(
            info = info.toPlayerUiInfo()
        )
    }

    private fun loadArtist(songId: Long) = viewModelScope.launch {
        when (val result = repo.getArtistOnSongId(songId)) {
            is Result.Error -> {
                when (result.error) {
                    DataError.Network.NO_INTERNET -> {
                        _uiEvent.send(
                            RootDrawerUiAction.EmitToast(
                                UiText.StringResource(
                                    R.string.error_no_internet
                                )
                            )
                        )

                        return@launch
                    }

                    else -> _uiEvent.send(
                        RootDrawerUiAction.EmitToast(
                            UiText.StringResource(
                                R.string.error_something_went_wrong
                            )
                        )
                    )
                }

                state = state.copy(
                    info = state.info.copy(
                        artist = state.info.artist.copy(
                            loadingState = DataLoadingState.ERROR
                        )
                    )
                )
            }

            is Result.Success -> {
                state = state.copy(
                    info = state.info.copy(
                        artist = state.info.artist.copy(
                            songId = if (result.data.isEmpty()) -1 else songId,
                            loadingState = DataLoadingState.LOADED,
                            artist = result.data.map { it.toSongArtistUiArtist() }
                        )
                    )
                )
            }
        }

        if (state.info.more.id != -1L &&
            state.info.more.id == state.info.more.id
        ) return@launch

        loadInfoJob?.cancel()
        loadInfoJob = loadOtherInfo(songId)
    }

    private fun loadOtherInfo(songId: Long) = viewModelScope.launch {
        when (val result = repo.getOtherInfo(songId)) {
            is Result.Error -> {
                when (result.error) {
                    DataError.Network.NO_INTERNET -> {
                        _uiEvent.send(
                            RootDrawerUiAction.EmitToast(
                                UiText.StringResource(
                                    R.string.error_no_internet
                                )
                            )
                        )
                    }

                    else -> _uiEvent.send(
                        RootDrawerUiAction.EmitToast(
                            UiText.StringResource(
                                R.string.error_something_went_wrong
                            )
                        )
                    )
                }
            }

            is Result.Success -> {
                state = state.copy(
                    info = state.info.copy(
                        more = result.data.toMorePlayerInfo()
                    )
                )
            }
        }
    }
}