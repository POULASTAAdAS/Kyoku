package com.poulastaa.kyoku

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.ScreenEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: DataStoreRepository,
) : ViewModel() {
    private val _routeExt = MutableStateFlow(RouteExt())
    val startRoute = _routeExt.asStateFlow()

    private val _keepSplashOn = MutableStateFlow(true)
    val keepSplashOn get() = _keepSplashOn.asStateFlow()

    init {
        viewModelScope.launch {
            val value = dataStore.readSignInState()

            when (value) {
                ScreenEnum.INTRO -> RouteExt(
                    route = Screens.AUTH_ROUTE,
                    startDestination = Screens.Intro.route
                )

                ScreenEnum.GET_SPOTIFY_PLAYLIST -> RouteExt(
                    route = Screens.START_UP_ROUTE,
                    startDestination = Screens.GetSpotifyPlaylist.route
                )


                ScreenEnum.SET_B_DATE -> RouteExt(
                    route = Screens.START_UP_ROUTE,
                    startDestination = Screens.GetSpotifyPlaylist.route
                )


                ScreenEnum.PIC_GENRE -> RouteExt(
                    route = Screens.START_UP_ROUTE,
                    startDestination = Screens.PicGenre.route
                )


                ScreenEnum.PIC_ARTIST -> RouteExt(
                    route = Screens.START_UP_ROUTE,
                    startDestination = Screens.PicArtist.route
                )


                ScreenEnum.HOME -> RouteExt(
                    route = Screens.APP_ROUTE,
                    startDestination = Screens.Home.route
                )

                else -> return@launch
            }.let {
                _routeExt.value = it
                if (_routeExt.value.startDestination != null) _keepSplashOn.value = false
            }
        }
    }
}