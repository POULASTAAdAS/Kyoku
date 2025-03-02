package com.poulastaa.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.presentation.designsystem.toUiUser
import com.poulastaa.profile.domain.model.DtoItemType
import com.poulastaa.profile.domain.repository.ProfileRepository
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
internal class ProfileViewmodel @Inject constructor(
    private val repo: ProfileRepository,
) : ViewModel() {
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
            val user = repo.getUser()
            val bDate = repo.getBData()

            _uiState.value = _uiState.value.copy(
                user = user.toUiUser(),
                bDate = bDate
            )
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val data = repo.getSavedData()
            _uiState.value = _uiState.value.copy(
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