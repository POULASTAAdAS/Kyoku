package com.poulastaa.settings.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.presentation.designsystem.ThemChanger
import com.poulastaa.core.presentation.designsystem.toUiUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

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
            SettingsUiAction.ResetRevelAnimation -> {
                _state.update {
                    it.copy(
                        offset = IntOffset(0, 0).toOffset(),
                    )
                }
            }

            SettingsUiAction.OpenLogoutDialog -> {
                _state.update {
                    it.copy(
                        isLogoutDialogVisible = true
                    )
                }
            }

            SettingsUiAction.CancelLogoutDialog -> {
                _state.update {
                    it.copy(
                        isLogoutDialogVisible = false
                    )
                }
            }

            SettingsUiAction.OnLogoutDialog -> {}

            SettingsUiAction.OpenDeleteAccountDialog -> {
                _state.update {
                    it.copy(
                        isDeleteAccountDialogVisible = true
                    )
                }
            }

            SettingsUiAction.CancelDeleteAccountDialog -> {
                _state.update {
                    it.copy(
                        isDeleteAccountDialogVisible = false
                    )
                }
            }

            SettingsUiAction.OnDeleteAccountDialog -> {}
            SettingsUiAction.OnHistoryClick -> {}
            SettingsUiAction.OnProfileClick -> {}

            is SettingsUiAction.OnStartThemChange -> {
                viewModelScope.launch {
                    startThemChangeTransition(action.offset)
                }
            }

            is SettingsUiAction.DragOffset -> {
                drag(action.x)
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

    private suspend fun startThemChangeTransition(offset: Offset?) {
        offset?.let {
            _state.update {
                it.copy(
                    offset = offset
                )
            }
        }
        // Delay for the animation to complete
        delay((_state.value.themChangeAnimationTime / 1.3).toDuration(DurationUnit.MILLISECONDS))
        ThemChanger.toggleTheme()
    }

    fun drag(x: Int) {
        _state.update {
            it.copy(
                dragOffset = IntOffset(x, 0)
            )
        }
    }
}