package com.poulastaa.kyoku

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.StartScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: DataStoreRepository,
) : ViewModel() {
    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    private val _keepSplashOn = MutableStateFlow(true)
    val keepSplashOn get() = _keepSplashOn.asStateFlow()

    init {
        viewModelScope.launch {
            val value = dataStore.readSignInState()

            when (value) {
                StartScreen.INTRO -> _startDestination.value = Screens.Intro.route
                StartScreen.GET_SPOTIFY_PLAYLIST -> _startDestination.value =
                    Screens.GetSpotifyPlaylist.route

                StartScreen.SET_B_DATE -> _startDestination.value = Screens.SetBirthDate.route
                StartScreen.PIC_GENRE -> _startDestination.value = Screens.PicGenre.route
                StartScreen.PIC_ARTIST -> _startDestination.value = Screens.PicArtist.route
                StartScreen.HOME -> _startDestination.value = Screens.Home.route
            }.let {
                if (_startDestination.value != null) _keepSplashOn.value = false
            }
        }
    }
}