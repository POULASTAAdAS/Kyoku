package com.poulastaa.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@HiltViewModel
internal class ProfileViewmodel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val state = _uiState.onStart {
        loadUser()
        loadData()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.toDuration(DurationUnit.SECONDS).inWholeMilliseconds),
        initialValue = ProfileUiState()
    )

    fun onAction(action: ProfileUiAction) {
        when (action) {
            ProfileUiAction.OnEditProfileImage -> {}
            ProfileUiAction.OnEditUserName -> {}
            is ProfileUiAction.OnNavigateSavedItem -> {}
            ProfileUiAction.OnNavigateToGallery -> {}

            is ProfileUiAction.OnUserNameChange -> {}
        }
    }

    private fun loadUser() {
        viewModelScope.launch {

        }
    }

    private fun loadData() {
        viewModelScope.launch {

        }
    }
}