package com.poulastaa.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.core.presentation.designsystem.model.TextHolder
import com.poulastaa.core.presentation.designsystem.toUiUser
import com.poulastaa.profile.domain.model.DtoItemType
import com.poulastaa.profile.domain.model.ProfileAllowedNavigationScreen
import com.poulastaa.profile.domain.repository.ProfileRepository
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
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@HiltViewModel
internal class ProfileViewmodel @Inject constructor(
    private val repo: ProfileRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileUiState())
    val state = _state.onStart {
        loadUser()
        loadData()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.toDuration(DurationUnit.SECONDS).inWholeMilliseconds),
        initialValue = ProfileUiState()
    )

    private val _uiEvent = Channel<ProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: ProfileUiAction) {
        when (action) {
            ProfileUiAction.OnEditProfileImage -> {}
            ProfileUiAction.OnEditUserNameToggle -> {
                if (_state.value.isMakingApiCall) return

                _state.update { state ->
                    state.copy(
                        username = if (state.isUpdateUsernameBottomSheetVisible.not()) state.username.copy(
                            value = state.user.username
                        ) else TextHolder(),
                        isUpdateUsernameBottomSheetVisible = state.isUpdateUsernameBottomSheetVisible.not(),
                    )
                }
            }

            is ProfileUiAction.OnNavigateSavedItem -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        ProfileUiEvent.Navigate(
                            when (action.type) {
                                UiItemType.PLAYLIST -> ProfileAllowedNavigationScreen.PLAYLIST
                                UiItemType.ALBUM -> ProfileAllowedNavigationScreen.ALBUM
                                UiItemType.ARTIST -> ProfileAllowedNavigationScreen.ARTIST
                                UiItemType.FAVOURITE -> ProfileAllowedNavigationScreen.FAVOURITE
                            }
                        )
                    )
                }
            }

            ProfileUiAction.OnNavigateToLibrary -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        ProfileUiEvent.Navigate(
                            ProfileAllowedNavigationScreen.LIBRARY
                        )
                    )
                }
            }

            is ProfileUiAction.OnUserNameChange -> {
                _state.update {
                    it.copy(
                        username = it.username.copy(
                            value = action.username
                        )
                    )
                }
            }

            ProfileUiAction.OnConformUserName -> {
                if (_state.value.isMakingApiCall) return
                viewModelScope.launch {
                    if (_state.value.username.value.trim() == _state.value.user.username) return@launch _uiEvent.send(
                        ProfileUiEvent.EmitToast(
                            UiText.StringResource(
                                R.string.error_same_username
                            )
                        )
                    )

                    if (isValidUserName().not()) return@launch

                    _state.update {
                        it.copy(
                            isMakingApiCall = true
                        )
                    }

                    when (val result = repo.updateUsername(_state.value.username.value)) {
                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    user = it.user.copy(
                                        username = it.username.value,
                                    ),
                                    isUpdateUsernameBottomSheetVisible = false
                                )
                            }
                        }

                        is Result.Error -> when (result.error) {
                            DataError.Network.NO_INTERNET -> _uiEvent.send(
                                ProfileUiEvent.EmitToast(
                                    UiText.StringResource(
                                        R.string.error_no_internet
                                    )
                                )
                            )

                            else -> _uiEvent.send(
                                ProfileUiEvent.EmitToast(
                                    UiText.StringResource(
                                        R.string.error_something_went_wrong
                                    )
                                )
                            )
                        }
                    }

                    _state.update {
                        it.copy(
                            isMakingApiCall = false
                        )
                    }
                }
            }
        }
    }

    private fun isValidUserName(): Boolean {
        val username = _state.value.username.value.trim()

        _state.update {
            it.copy(
                username = it.username.copy(
                    isErr = true,
                    isValid = false,
                    errText = UiText.StringResource(
                        when {
                            username.isEmpty() || username.isBlank() -> R.string.error_username_empty
                            username.length < 3 -> R.string.error_username_to_short
                            username.length > 20 -> R.string.error_username_to_long
                            username.startsWith("_") -> R.string.error_username_cant_start_with_underscore

                            else -> return true
                        }
                    )
                )
            )
        }

        return false
    }

    private fun loadUser() {
        viewModelScope.launch {
            val user = repo.getUser()
            _state.update {
                it.copy(
                    user = user.toUiUser(),
                )
            }

            val bDate = repo.getBData()

            _state.update {
                it.copy(
                    bDate = bDate
                )
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val data = repo.getSavedData()

            _state.update { state ->
                state.copy(
                    savedItems = data.map {
                        UiSavedItems(
                            itemCount = it.itemCount,
                            itemType = when (it.itemType) {
                                DtoItemType.PLAYLIST -> UiItemType.PLAYLIST
                                DtoItemType.ALBUM -> UiItemType.ALBUM
                                DtoItemType.ARTIST -> UiItemType.ARTIST
                                DtoItemType.FAVOURITE -> UiItemType.FAVOURITE
                            }
                        )
                    }
                )
            }
        }
    }
}