package com.poulastaa.core.presentation.ui.components.crate_playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.repository.CreatePlaylistRepository
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.core.presentation.designsystem.UiText.StringResource
import com.poulastaa.core.presentation.designsystem.model.TextHolder
import com.poulastaa.core.presentation.ui.components.crate_playlist.CreatePlaylistUiEvent.Created
import com.poulastaa.core.presentation.ui.components.crate_playlist.CreatePlaylistUiEvent.EmitToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MIN_LENGTH = 3
private const val MAX_LENGTH = 20
private val VALID_NAME_REGEX = "^[a-zA-Z0-9_]+(?: [a-zA-Z0-9_]+)*$".toRegex()

@HiltViewModel
internal class CreatePlaylistBottomBarViewmodel @Inject constructor(
    private val repo: CreatePlaylistRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CreatePlaylistUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = CreatePlaylistUiState()
    )

    private val _uiEvent = Channel<CreatePlaylistUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: CreatePlaylistUiAction) {
        when (action) {
            is CreatePlaylistUiAction.OnPlaylistNameChange -> _state.update {
                it.copy(
                    name = it.name.copy(
                        value = action.message,
                        isErr = false,
                        errText = UiText.DynamicString("")
                    )
                )
            }

            CreatePlaylistUiAction.OnCancelClick -> viewModelScope.launch {
                _uiEvent.send(CreatePlaylistUiEvent.Canceled)
            }

            CreatePlaylistUiAction.OnSaveClick -> viewModelScope.launch {
                _state.update {
                    it.copy(
                        isLoading = true
                    )
                }

                when (_state.value.name.validate()) {
                    PlaylistNameValidationState.VALID -> {
                        val result = repo.createPlaylist(_state.value.name.value.trim())

                        when (result) {
                            is Result.Error -> when (result.error) {
                                DataError.Network.NO_INTERNET -> _uiEvent.send(
                                    EmitToast(
                                        StringResource(R.string.error_no_internet)
                                    )
                                )

                                else -> _uiEvent.send(
                                    EmitToast(
                                        StringResource(
                                            R.string.error_something_went_wrong
                                        )
                                    )
                                )
                            }

                            is Result.Success -> {
                                _uiEvent.send(
                                    EmitToast(
                                        StringResource(
                                            R.string.playlist_created
                                        )
                                    )
                                ).also {
                                    _uiEvent.send(Created(result.data))
                                }
                            }
                        }
                    }

                    PlaylistNameValidationState.TOO_SHORT -> _state.update {
                        it.copy(
                            name = it.name.copy(
                                isErr = true,
                                errText = StringResource(R.string.error_playlist_name_to_short)
                            )
                        )
                    }

                    PlaylistNameValidationState.TOO_LONG -> _state.update {
                        it.copy(
                            name = it.name.copy(
                                isErr = true,
                                errText = StringResource(R.string.error_Playlist_name_to_long)
                            )
                        )
                    }

                    PlaylistNameValidationState.STARTS_WITH_UNDERSCORE -> _state.update {
                        it.copy(
                            name = it.name.copy(
                                isErr = true,
                                errText = StringResource(R.string.error_playlist_name_cant_start_with_underscore)
                            )
                        )
                    }

                    PlaylistNameValidationState.INVALID_CHARACTERS -> _state.update {
                        it.copy(
                            name = it.name.copy(
                                isErr = true,
                                errText = StringResource(R.string.error_playlist_name_cant_contain_special_characters)
                            )
                        )
                    }
                }

                _state.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    private enum class PlaylistNameValidationState {
        VALID,
        TOO_SHORT,
        TOO_LONG,
        STARTS_WITH_UNDERSCORE,
        INVALID_CHARACTERS,
    }

    private fun TextHolder.validate(): PlaylistNameValidationState {
        val name = this.value.trim()

        return when {
            name.length < MIN_LENGTH -> PlaylistNameValidationState.TOO_SHORT
            name.length > MAX_LENGTH -> PlaylistNameValidationState.TOO_LONG
            name.startsWith("_") -> PlaylistNameValidationState.STARTS_WITH_UNDERSCORE
            !name.matches(VALID_NAME_REGEX) -> PlaylistNameValidationState.INVALID_CHARACTERS
            else -> PlaylistNameValidationState.VALID
        }
    }
}
