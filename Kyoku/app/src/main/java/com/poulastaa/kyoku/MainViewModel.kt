package com.poulastaa.kyoku

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
                ScreenEnum.INTRO -> StartRoute(
                    route = Screens.AUTH_ROUTE,
                    startDestination = Screens.EmailLogIn.route
                )

                ScreenEnum.GET_SPOTIFY_PLAYLIST -> StartRoute(
                    route = Screens.START_UP_ROUTE,
                    startDestination = Screens.GetSpotifyPlaylist.route
                )


                ScreenEnum.SET_B_DATE -> StartRoute(
                    route = Screens.START_UP_ROUTE,
                    startDestination = Screens.SetBirthDate.route
                )


                ScreenEnum.PIC_GENRE -> StartRoute(
                    route = Screens.START_UP_ROUTE,
                    startDestination = Screens.PicGenre.route
                )


                ScreenEnum.PIC_ARTIST -> StartRoute(
                    route = Screens.START_UP_ROUTE,
                    startDestination = Screens.PicArtist.route
                )


                ScreenEnum.HOME -> StartRoute(
                    route = Screens.APP_ROUTE,
                    startDestination = Screens.Home.route
                )

                else -> return@launch
            }.let {
                _startRoute.value = it
                if (_startRoute.value.startDestination != null) _keepSplashOn.value = false
            }
        }
    }
}