package com.poulastaa.auth.presentation.intro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.ScreenEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(IntroUiState())
        private set

    private val _uiEvent = Channel<IntroUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: IntroUiEvent) {
        when (event) {
            IntroUiEvent.OnEmailLogInClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(IntroUiAction.OnSuccess(ScreenEnum.EMAIL_LOGIN))
                }
            }

            IntroUiEvent.OnGoogleLogInClick -> {

            }
        }
    }
}