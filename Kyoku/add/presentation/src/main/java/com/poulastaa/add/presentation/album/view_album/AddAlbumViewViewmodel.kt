package com.poulastaa.add.presentation.album.view_album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.add.domain.repository.AddAlbumRepository
import com.poulastaa.add.presentation.album.UiAlbum
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.toUiDetailedPrevSong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddAlbumViewViewmodel @Inject constructor(
    private val repo: AddAlbumRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AddAlbumViewUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = AddAlbumViewUiState()
    )

    private val _uiEvent = Channel<AddAlbumViewUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun init(album: UiAlbum) {
        if (album.id == -1L) return

        viewModelScope.launch {
            _state.update {
                it.copy(
                    loadingType = LoadingType.Loading
                )
            }

            when (val result = repo.getAlbum(album.id)) {
                is Result.Error -> when (result.error) {
                    DataError.Network.NO_INTERNET -> _uiEvent.send(
                        AddAlbumViewUiEvent.EmitToast(
                            UiText.StringResource(
                                R.string.error_no_internet
                            )
                        )
                    ).also {
                        _state.update {
                            it.copy(
                                loadingType = LoadingType.Error(
                                    type = LoadingType.ERROR_TYPE.NO_INTERNET
                                )
                            )
                        }
                    }

                    else -> _uiEvent.send(
                        AddAlbumViewUiEvent.EmitToast(
                            UiText.StringResource(
                                R.string.error_something_went_wrong
                            )
                        )
                    ).also {
                        _state.update {
                            it.copy(
                                loadingType = LoadingType.Error(
                                    type = LoadingType.ERROR_TYPE.UNKNOWN
                                )
                            )
                        }
                    }
                }

                is Result.Success -> _state.update {
                    it.copy(
                        album = album,
                        songs = result.data.map { it.toUiDetailedPrevSong() },
                        loadingType = LoadingType.Content,
                    )
                }
            }
        }
    }
}