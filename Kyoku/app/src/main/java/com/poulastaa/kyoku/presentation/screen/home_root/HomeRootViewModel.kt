package com.poulastaa.kyoku.presentation.screen.home_root

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiState
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.utils.storeSignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeRootViewModel @Inject constructor(
    private val ds: DataStoreOperation
) : ViewModel() {
    private fun readAccessToken() {
        viewModelScope.launch {
            ds.readTokenOrCookie().collect {
                state = state.copy(
                    headerValue = it
                )
            }
        }
    }

    private fun readAuthType() {
        viewModelScope.launch {
            val authType = ds.readAuthType().first()

            if (authType == AuthType.UN_AUTH.name) {
                // TODO: handle quick auth
            }

            state = state.copy(
                isCookie = authType == AuthType.SESSION_AUTH.name
            )
        }
    }

    private fun readUserName() {
        viewModelScope.launch {
            ds.readUsername().collect {
                state = state.copy(
                    userName = it
                )
            }
        }
    }

    private fun readProfilePicUrl() {
        viewModelScope.launch {
            ds.readProfilePic().collect {
                state = state.copy(
                    profilePicUrl = it
                )
            }
        }
    }

    private fun setHomeTopBarTitle() {
        viewModelScope.launch {
            val localTime = LocalDateTime.now().toLocalTime()

            val currentTime = localTime.format(DateTimeFormatter.ofPattern("hh")).toInt()
            val status = localTime.format(DateTimeFormatter.ofPattern("a"))

            if (status.uppercase() == "AM") {
                state = if (currentTime >= 4) {
                    state.copy(
                        homeTopBarTitle = "Good Morning"
                    )
                } else {
                    state.copy(
                        homeTopBarTitle = "Night Owl"
                    )
                }
            } else {
                state = if (currentTime <= 10) {
                    state.copy(
                        homeTopBarTitle = "Good Evening"
                    )
                } else if (currentTime in 10..11) {
                    state.copy(
                        homeTopBarTitle = "Good Night"
                    )
                } else {
                    state.copy(
                        homeTopBarTitle = "Night Owl"
                    )
                }
            }
        }
    }


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    var state by mutableStateOf(HomeRootUiState())


    init {
        readAccessToken()
        readAuthType()
        readUserName()
        readProfilePicUrl()
        setHomeTopBarTitle()
    }


    fun onEvent(event: HomeRootUiEvent) {
        when (event) {
            is HomeRootUiEvent.Navigate -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.Navigate(event.route))
                }
            }

            HomeRootUiEvent.LogOut -> {
                storeSignInState(SignInStatus.AUTH, ds)
            }
        }
    }
}