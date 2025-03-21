package com.poulastaa.settings.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemChanger
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.core.presentation.designsystem.toUiUser
import com.poulastaa.main.domain.repository.work.SyncLibraryScheduler
import com.poulastaa.settings.domain.model.SettingsAllowedNavigationScreens
import com.poulastaa.settings.domain.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    private val repo: SettingRepository,
    private val work: SyncLibraryScheduler,
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

    private var isThemChangeCoolDown = false

    fun onAction(action: SettingsUiAction) {
        if (_state.value.isLoading) return

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
                        isLogoutBottomSheetVisible = true
                    )
                }
            }

            SettingsUiAction.CancelLogoutDialog -> {
                _state.update {
                    it.copy(
                        isLogoutBottomSheetVisible = false
                    )
                }
            }

            SettingsUiAction.OnLogoutDialog -> {
                _state.update {
                    it.copy(
                        isLoading = true
                    )
                }

                viewModelScope.launch {
                    val db = async { repo.logOut() }
                    val sync = async { work.cancelAllSyncs() }
                    listOf(db, sync).awaitAll()

                    _uiEvent.send(SettingsUiEvent.OnLogOutSuccess)
                }
            }

            SettingsUiAction.OpenDeleteAccountDialog -> {
                _state.update {
                    it.copy(
                        isDeleteAccountVBottomSheetVisible = true
                    )
                }
            }

            SettingsUiAction.CancelDeleteAccountDialog -> {
                _state.update {
                    it.copy(
                        isDeleteAccountVBottomSheetVisible = false
                    )
                }
            }

            SettingsUiAction.OnDeleteAccountDialog -> {
                _state.update {
                    it.copy(
                        isLoading = true
                    )
                }

                viewModelScope.launch {
                    when (val result = repo.deleteAccount()) {
                        is Result.Error -> {
                            when (result.error) {
                                DataError.Network.NO_INTERNET -> _uiEvent.send(
                                    SettingsUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.error_no_internet
                                        )
                                    )
                                )

                                else -> _uiEvent.send(
                                    SettingsUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.error_something_went_wrong
                                        )
                                    )
                                )
                            }
                        }

                        is Result.Success -> {
                            _uiEvent.send(
                                SettingsUiEvent.EmitToast(
                                    UiText.StringResource(
                                        R.string.account_deleted
                                    )
                                )
                            )
                            _uiEvent.send(SettingsUiEvent.OnLogOutSuccess)
                        }
                    }

                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }

            SettingsUiAction.OnHistoryClick -> {
                viewModelScope.launch {
                    _uiEvent.send(SettingsUiEvent.Navigate(SettingsAllowedNavigationScreens.HISTORY))
                }
            }

            SettingsUiAction.OnProfileClick -> {
                viewModelScope.launch {
                    _uiEvent.send(SettingsUiEvent.Navigate(SettingsAllowedNavigationScreens.PROFILE))
                }
            }

            is SettingsUiAction.OnStartThemChange -> {
                viewModelScope.launch {
                    startThemChangeTransition(action.offset)
                }
            }

            SettingsUiAction.OnThemPicToggle -> {
                _state.update {
                    it.copy(
                        isThemChangeScreenVisible = it.isThemChangeScreenVisible.not()
                    )
                }
            }

            is SettingsUiAction.OnThemChange -> {
                if (isThemChangeCoolDown) return
                if (action.them == ThemChanger.themColor) return

                ThemChanger.changeThem(action.them)
                viewModelScope.launch {
                    themChangeCoolDown()
                }
            }

            is SettingsUiAction.OnDragOffset -> {
                _state.update {
                    it.copy(
                        dragOffset = IntOffset(action.x, 0)
                    )
                }
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            val user = repo.getUser().toUiUser()

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
        ThemModeChanger.toggleThemeMode()
    }

    private suspend fun themChangeCoolDown() {
        isThemChangeCoolDown = true
        delay(800)
        isThemChangeCoolDown = false
    }
}