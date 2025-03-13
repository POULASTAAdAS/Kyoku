package com.poulastaa.view.presentation.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.view.domain.repository.ViewArtistRepository
import com.poulastaa.view.presentation.mapper.toUiViewPrevSong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@HiltViewModel
internal class ViewArtistViewmodel @Inject constructor(
    private val repo: ViewArtistRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ViewArtistUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.toDuration(DurationUnit.SECONDS)),
        initialValue = ViewArtistUiState()
    )

    private val _uiEvent = Channel<ViewArtistUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onAction(action: ViewArtistUiAction) {
        when (action) {
            is ViewArtistUiAction.OnPlayAll -> {}
            is ViewArtistUiAction.OnSongClick -> {}

            is ViewArtistUiAction.OnSongOptionsToggle -> {}

            ViewArtistUiAction.OnExploreArtist -> {}
            ViewArtistUiAction.OnFollowToggle -> {}
        }
    }

    fun init(artistId: ArtistId) {
        viewModelScope.launch {
            when (val result = repo.loadArtist(artistId)) {
                is Result.Error -> when (result.error) {
                    DataError.Network.NO_INTERNET -> _state.update {
                        it.copy(
                            loadingType = LoadingType.Error(
                                type = LoadingType.ERROR_TYPE.NO_INTERNET,
                                lottieId = listOf(
                                    R.raw.lottie_error_1,
                                    R.raw.lottie_error_2,
                                    R.raw.lottie_error_3,
                                    R.raw.lottie_error_4,
                                    R.raw.lottie_error_5,
                                    R.raw.lottie_error_6,
                                ).random(Random)
                            )
                        )
                    }

                    else -> _state.update {
                        it.copy(
                            loadingType = LoadingType.Error(
                                type = LoadingType.ERROR_TYPE.UNKNOWN,
                                lottieId = listOf(
                                    R.raw.lottie_error_1,
                                    R.raw.lottie_error_2,
                                    R.raw.lottie_error_3,
                                    R.raw.lottie_error_4,
                                    R.raw.lottie_error_5,
                                    R.raw.lottie_error_6,
                                ).random(Random)
                            )
                        )
                    }
                }

                is Result.Success -> {
                    _state.update { state ->
                        state.copy(
                            artist = result.data.artist.toUiViewArtist(),
                            mostPopularSongs = result.data.mostPopularSongs.map { it.toUiViewPrevSong() },
                            loadingType = LoadingType.Content
                        )
                    }
                }
            }
        }
    }
}