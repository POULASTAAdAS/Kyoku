package com.poulastaa.kyoku

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DatastoreRepository
import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.kyoku.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val ds: DatastoreRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(RootUiState())
    val state = _state
        .onStart {
            getSavedScreen()
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = RootUiState()
        )

    private val _keepSplashOn = MutableStateFlow(true)
    val keepSplashOn get() = _keepSplashOn.asStateFlow()

    private suspend fun getSavedScreen() {
        val screens = ds.readSignInState()
        val screen = when (screens) {
            SavedScreen.INTRO -> Screens.Auth.Intro
            SavedScreen.IMPORT_SPOTIFY_PLAYLIST -> Screens.SetUp.ImportSpotifyPlaylist
            SavedScreen.SET_B_DATE -> Screens.SetUp.SetBirthDate
            SavedScreen.PIC_GENRE -> Screens.SetUp.PickGenre
            SavedScreen.PIC_ARTIST -> Screens.SetUp.PickArtist
            SavedScreen.HOME -> Screens.Core.Home
            SavedScreen.LIBRARY -> Screens.Core.Library
        }
        _state.update {
            it.copy(
                screen = screen
            )
        }

        _keepSplashOn.value = false
    }
}