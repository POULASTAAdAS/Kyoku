package com.poulastaa.auth.presentation.intro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.auth.AuthRepository
import com.poulastaa.core.domain.model.UserAuthStatus
import com.poulastaa.core.domain.ConfigProviderRepository
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    config: ConfigProviderRepository,
    private val ds: DataStoreRepository,
    private val auth: AuthRepository,
) : ViewModel() {
    var state by mutableStateOf(IntroUiState())
        private set

    private val _uiEvent = Channel<IntroUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        state = state.copy(
            clientId = config.getClientId()
        )
    }

    fun onEvent(event: IntroUiEvent) {
        when (event) {
            IntroUiEvent.OnEmailLogInClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(IntroUiAction.OnSuccess(ScreenEnum.EMAIL_LOGIN))
                }
            }

            IntroUiEvent.OnGoogleLogInClick -> {
                if (!state.isGoogleLogging) {
                    state = state.copy(
                        isGoogleLogging = true
                    )
                }
            }

            IntroUiEvent.OnGoogleLogInCancel -> {
                state = state.copy(
                    isGoogleLogging = false
                )
            }

            is IntroUiEvent.OnTokenReceive -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val countryCode = event.activity.resources.configuration.locales[0].country

                    val result = auth.googleAuth(
                        token = event.token,
                        countryCode = countryCode
                    )

                    when (result) {
                        is Result.Error -> {
                            when (result.error) {
                                DataError.Network.NO_INTERNET -> {
                                    _uiEvent.send(
                                        IntroUiAction.EmitToast(
                                            message = UiText.StringResource(R.string.error_no_internet)
                                        )
                                    )
                                }

                                else -> {
                                    _uiEvent.send(
                                        IntroUiAction.EmitToast(
                                            message = UiText.StringResource(R.string.error_something_went_wrong)
                                        )
                                    )
                                }
                            }
                        }

                        is Result.Success -> {
                            when (result.data) {
                                UserAuthStatus.CREATED -> {
                                    ds.storeSignInState(ScreenEnum.GET_SPOTIFY_PLAYLIST)
                                    _uiEvent.send(IntroUiAction.OnSuccess(ScreenEnum.GET_SPOTIFY_PLAYLIST))
                                }

                                UserAuthStatus.USER_FOUND_HOME -> {
                                    ds.storeSignInState(ScreenEnum.HOME)
                                    _uiEvent.send(IntroUiAction.OnSuccess(ScreenEnum.HOME))
                                }

                                UserAuthStatus.USER_FOUND_STORE_B_DATE -> {
                                    ds.storeSignInState(ScreenEnum.SET_B_DATE)
                                    _uiEvent.send(IntroUiAction.OnSuccess(ScreenEnum.SET_B_DATE))
                                }

                                UserAuthStatus.USER_FOUND_SET_GENRE -> {
                                    ds.storeSignInState(ScreenEnum.PIC_GENRE)
                                    _uiEvent.send(IntroUiAction.OnSuccess(ScreenEnum.PIC_GENRE))
                                }

                                UserAuthStatus.USER_FOUND_SET_ARTIST -> {
                                    ds.storeSignInState(ScreenEnum.PIC_ARTIST)
                                    _uiEvent.send(IntroUiAction.OnSuccess(ScreenEnum.PIC_ARTIST))
                                }

                                else -> {
                                    _uiEvent.send(
                                        IntroUiAction.EmitToast(
                                            message = UiText.StringResource(R.string.error_something_went_wrong)
                                        )
                                    )
                                }
                            }
                        }
                    }

                    state = state.copy(
                        isGoogleLogging = false
                    )
                }
            }
        }
    }
}