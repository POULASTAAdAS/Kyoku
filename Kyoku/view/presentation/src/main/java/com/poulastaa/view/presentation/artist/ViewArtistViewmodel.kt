package com.poulastaa.view.presentation.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.toUiPrevSong
import com.poulastaa.view.domain.repository.ViewArtistRepository
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

    }

    fun init(artistId: ArtistId) {
        viewModelScope.launch {
            when (val result = repo.loadArtist(artistId)) {
                is Result.Error -> when (result.error) {
                    DataError.Network.NO_INTERNET -> {
                        _uiEvent.send(
                            ViewArtistUiEvent.EmitToast(
                                UiText.StringResource(
                                    R.string.error_no_internet
                                )
                            )
                        )
                    }

                    else -> _uiEvent.send(
                        ViewArtistUiEvent.EmitToast(
                            UiText.StringResource(
                                R.string.error_something_went_wrong
                            )
                        )
                    )
                }

                is Result.Success -> {
                    _state.update { state ->
                        state.copy(
                            artist = result.data.artist.toUiViewArtist(),
                            mostPopularSongs = result.data.mostPopularSongs.map { it.toUiPrevSong() }
                        )
                    }
                }
            }

//            _state.update {
//                it.copy(
//                    loadingType = if (it.artist.name.isEmpty()) LoadingType.ERROR
//                    else LoadingType.CONTENT
//                )
//            }
        }
    }
}