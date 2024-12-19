package com.poulastaa.auth.presentation.intro

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewmodel @Inject constructor() : ViewModel() {
    var state by mutableStateOf(IntroUiState())
        private set

    fun onAction(action: IntroUiAction) {
        when (action) {
            IntroUiAction.OnEmailSingInClick -> {

            }

            IntroUiAction.OnGoogleSignInCancel -> {
                state = state.copy(
                    isGoogleAuthLoading = true
                )
            }

            IntroUiAction.OnGoogleSignInClick -> {
                state = state.copy(
                    isGoogleAuthLoading = true
                )
            }

            is IntroUiAction.OnTokenReceive -> {
                Log.d("token", action.token)
            }
        }
    }
}