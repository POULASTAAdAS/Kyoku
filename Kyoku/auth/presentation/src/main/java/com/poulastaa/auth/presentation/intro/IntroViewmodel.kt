package com.poulastaa.auth.presentation.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.presentation.BuildConfig
import com.poulastaa.core.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
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

                        }

                        is Result.Success -> {

                        }
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