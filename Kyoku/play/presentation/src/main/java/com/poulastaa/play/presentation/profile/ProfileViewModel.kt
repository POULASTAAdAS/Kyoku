package com.poulastaa.play.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val ds: DataStoreRepository,
) : ViewModel() {
    var state by mutableStateOf(ProfileUiState())
        private set

    private val _uiState = Channel<ProfileUiAction>()
    val uiState = _uiState.receiveAsFlow()

    init {
        readData()
        readHeader()
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            ProfileUiEvent.EditClick -> {

            }

            is ProfileUiEvent.OnItemClick -> {

            }
        }
    }

    private fun readData() {
        viewModelScope.launch {
            val user = ds.readLocalUser()

            state = state.copy(
                name = user.name,
                imageUrl = user.profilePic
            )
        }
    }

    private fun readHeader() = viewModelScope.launch {
        ds.readTokenOrCookie().collectLatest {
            state = state.copy(
                header = it
            )
        }
    }
}