package com.poulastaa.auth.presentation.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.domain.model.AuthStatus
import com.poulastaa.auth.presentation.BuildConfig
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewmodel @Inject constructor(
    private val repo: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(IntroUiState())
    val state = _state
        .onStart {
            loadClientId()
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = IntroUiState()
        )

    private val _uiEvent = Channel<IntroUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: IntroUiAction) {
        when (action) {
            IntroUiAction.OnGoogleSignInCancel -> {
                _state.update {
                    it.copy(
                        isGoogleAuthLoading = false
                    )
                }
            }

            IntroUiAction.OnGoogleSignInClick -> {
                if (_state.value.isGoogleAuthLoading) return

                _state.update {
                    it.copy(
                        isGoogleAuthLoading = true
                    )
                }
            }

            is IntroUiAction.OnTokenReceive -> {
                viewModelScope.launch {
                    val response = repo.googleAuth(
                        token = action.token,
                        countryCode = action.countryCode
                    )

                    when (response) {
                        is Result.Error -> {
                            when (response.error) {
                                DataError.Network.NO_INTERNET -> _uiEvent.send(
                                    IntroUiEvent.EmitToast(UiText.StringResource(R.string.error_no_internet))
                                )

                                else -> _uiEvent.send(
                                    IntroUiEvent.EmitToast(UiText.StringResource(R.string.error_something_went_wrong))
                                )
                            }
                        }

                        is Result.Success -> {
                            when (response.data) {
                                AuthStatus.CREATED -> _uiEvent.send(
                                    IntroUiEvent.OnSuccess(
                                        SavedScreen.IMPORT_SPOTIFY_PLAYLIST
                                    )
                                )

                                AuthStatus.USER_FOUND,
                                AuthStatus.USER_FOUND_HOME,
                                    -> _uiEvent.send(
                                    IntroUiEvent.OnSuccess(
                                        SavedScreen.MAIN
                                    )
                                )

                                AuthStatus.USER_FOUND_STORE_B_DATE -> _uiEvent.send(
                                    IntroUiEvent.OnSuccess(
                                        SavedScreen.SET_B_DATE
                                    )
                                )

                                AuthStatus.USER_FOUND_SET_GENRE -> _uiEvent.send(
                                    IntroUiEvent.OnSuccess(
                                        SavedScreen.PIC_GENRE
                                    )
                                )

                                AuthStatus.USER_FOUND_SET_ARTIST -> _uiEvent.send(
                                    IntroUiEvent.OnSuccess(
                                        SavedScreen.PIC_ARTIST
                                    )
                                )

                                else -> _uiEvent.send(
                                    IntroUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.error_something_went_wrong
                                        )
                                    )
                                )
                            }
                        }
                    }

                    _state.update {
                        it.copy(
                            isGoogleAuthLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun loadClientId() {
        _state.update {
            it.copy(
                clientId = BuildConfig.CLIENT_ID
            )
        }
    }
}