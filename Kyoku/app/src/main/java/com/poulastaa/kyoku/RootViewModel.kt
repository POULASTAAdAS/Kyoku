package com.poulastaa.kyoku

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.SavedScreen
import com.poulastaa.core.presentation.ThemeManager
import com.poulastaa.kyoku.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val themeManager: ThemeManager,
) : ViewModel() {
    private val _state = MutableStateFlow(RootUiState())
    val state = _state.onStart {
        getSavedScreen()
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = RootUiState()
    )

    private val _keepSplashOn = MutableStateFlow(true)
    val keepSplashOn get() = _keepSplashOn.asStateFlow()

    fun loadThem(isSystemThemDark: Boolean) {
        if (themeManager.isModeDark.value != isSystemThemDark)
            themeManager.loadOrChangeTheme(isSystemThemDark)
    }

    private suspend fun getSavedScreen() {
//        val screens = ds.readSignInState()
        delay(1000)

        val screens = SavedScreen.IMPORT_SPOTIFY_PLAYLIST
        val screen = when (screens) {
            SavedScreen.INTRO -> Screens.Auth
            SavedScreen.IMPORT_SPOTIFY_PLAYLIST -> Screens.SetUp.ImportSpotifyPlaylist
            SavedScreen.SET_B_DATE -> Screens.SetUp.SetBirthDate
            SavedScreen.PIC_GENRE -> Screens.SetUp.PickGenre
            SavedScreen.PIC_ARTIST -> Screens.SetUp.PickArtist
            SavedScreen.MAIN -> Screens.Main
        }

        _state.update {
            it.copy(screen)
        }

        _keepSplashOn.value = false
    }
}