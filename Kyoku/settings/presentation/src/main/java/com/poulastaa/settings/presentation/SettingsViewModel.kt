package com.poulastaa.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.presentation.ui.toUiUser
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
class SettingsViewModel @Inject constructor(
    private val ds: DatastoreRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsUiState())
    val state = _state
        .onStart {
            loadUser()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SettingsUiState()
        )

    private val _uiEvent = Channel<SettingsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: SettingsUiAction) {
        when (action) {
            SettingsUiAction.OnLogOutCLick -> {
                viewModelScope.launch {
                    ds.logOut().also {
                        _uiEvent.send(SettingsUiEvent.OnLogOutSuccess)
                    }
                }
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            val user = ds.readLocalUser().toUiUser()

            _state.update {
                it.copy(
                    user = user
                )
            }
        }
    }
}