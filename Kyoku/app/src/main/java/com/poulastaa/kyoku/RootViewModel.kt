package com.poulastaa.kyoku

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(RootUiState())
    val state = _state
        .onStart {
            getSavedScreen()
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = RootUiState()
        )

    private val _keepSplashOn = MutableStateFlow(true)
    val keepSplashOn get() = _keepSplashOn.asStateFlow()

    private suspend fun getSavedScreen() {
        // todo get saved screen

        delay(2000)
        val screen = Screens.Auth.Intro

        _state.update {
            it.copy(
                screen = screen
            )
        }

        _keepSplashOn.value = false
    }
}