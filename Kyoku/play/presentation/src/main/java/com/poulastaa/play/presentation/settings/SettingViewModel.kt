package com.poulastaa.play.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.domain.repository.setting.SettingRepository
import com.poulastaa.play.domain.SyncLibraryScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: SettingRepository,
    private val sync: SyncLibraryScheduler,
) : ViewModel() {
    var state by mutableStateOf(SettingUiState())
        private set

    private val _uiEvent = Channel<SettingUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        readHeader()
        readProfilePic()
    }

    fun onEvent(event: SettingUiEvent) {
        when (event) {
            SettingUiEvent.OnLogOutClick -> {
                state = state.copy(
                    isLogoutDialogVisible = true
                )
            }

            SettingUiEvent.OnLogOutConform -> {
                state = state.copy(
                    isLoggingOut = true
                )

                viewModelScope.launch {
                    val db = async { repo.logOut() }
                    val sync = async { sync.cancelAllSyncs() }

                    db.await()
                    sync.await()

                    state = state.copy(
                        isLoggingOut = false,
                        isLogoutDialogVisible = false
                    )

                    _uiEvent.send(
                        SettingUiAction.Navigate(ScreenEnum.INTRO)
                    )
                }
            }

            SettingUiEvent.OnLogOutCancel -> {
                state = state.copy(
                    isLogoutDialogVisible = false
                )
            }
        }
    }

    private fun readHeader() {
        viewModelScope.launch {
            ds.readTokenOrCookie().collectLatest {
                state = state.copy(
                    header = it
                )
            }
        }
    }

    private fun readProfilePic() {
        viewModelScope.launch {
            state = state.copy(
                profilePicUrl = ds.readLocalUser().profilePic
            )
        }
    }
}