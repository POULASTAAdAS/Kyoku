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
    private val _startRoute = MutableStateFlow(
        StartRoute()
    )
    val startRoute = this._startRoute.asStateFlow()

    private val _keepSplashOn = MutableStateFlow(true)
    val keepSplashOn get() = _keepSplashOn.asStateFlow()

    init {
        viewModelScope.launch {
            val value = dataStore.readSignInState()

            when (value) {
                StartScreen.INTRO -> StartRoute(
                    route = Screens.AUTH_ROUTE,
                    startDestination = Screens.Intro.route
                )

                StartScreen.GET_SPOTIFY_PLAYLIST -> StartRoute(
                    route = Screens.START_UP_ROUTE,
                    startDestination = Screens.GetSpotifyPlaylist.route
                )


                StartScreen.SET_B_DATE -> StartRoute(
                    route = Screens.START_UP_ROUTE,
                    startDestination = Screens.SetBirthDate.route
                )


                StartScreen.PIC_GENRE -> StartRoute(
                    route = Screens.START_UP_ROUTE,
                    startDestination = Screens.PicGenre.route
                )


                StartScreen.PIC_ARTIST -> StartRoute(
                    route = Screens.START_UP_ROUTE,
                    startDestination = Screens.PicArtist.route
                )


                StartScreen.HOME -> StartRoute(
                    route = Screens.APP_ROUTE,
                    startDestination = Screens.Home.route
                )

            }.let {
                _startRoute.value = it
                if (_startRoute.value.startDestination != null) _keepSplashOn.value = false
            }
        }
    }
}