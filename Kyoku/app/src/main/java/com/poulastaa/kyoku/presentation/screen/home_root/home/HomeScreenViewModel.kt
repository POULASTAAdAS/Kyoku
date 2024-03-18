package com.poulastaa.kyoku.presentation.screen.home_root.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.api.service.home.HomeReq
import com.poulastaa.kyoku.data.model.api.service.home.HomeResponseStatus
import com.poulastaa.kyoku.data.model.api.service.home.HomeType
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.home.HomeUiData
import com.poulastaa.kyoku.data.model.screens.home.HomeUiState
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.utils.getHomeReqTimeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val ds: DataStoreOperation,
    private val db: DatabaseRepositoryImpl,
    private val api: ServiceRepository
) : ViewModel() {
    private val network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)

    init {
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
                state = state.copy(
                    isInternetAvailable = checkInternetConnection()
                )
            }
        }
    }

    private fun checkInternetConnection(): Boolean {
        return network.value == NetworkObserver.STATUS.AVAILABLE
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(HomeUiState())
        private set

    private suspend fun isFirstReq() = db.checkIfNewUser()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(800)
            if (state.isInternetAvailable) {
                loadStartupData()
            } else {
                // todo show network error
                Log.d("called", "internet error")
            }
        }
    }

    private fun loadStartupData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (isFirstReq()) {
                // make api call
                val response = api.homeReq(
                    req = HomeReq(
                        type = HomeType.NEW_USER_REQ,
                        time = getHomeReqTimeType(),
                        isOldEnough = false
                    )
                )

                // store response and read response
                when (response.status) {
                    HomeResponseStatus.SUCCESS -> {
                        val storeFevArtistMixJob = async {
                            db.insertIntoFevArtistMixPrev(list = response.fevArtistsMixPreview)
                        }

                        val storeAlbumJob = async {
                            db.insertIntoAlbum(list = response.albumPreview.listOfPreviewAlbum)
                        }

                        val storeArtistPrevJob = async {
                            db.insertResponseArtistPrev(list = response.artistsPreview)
                        }

                        val storeDailyMixJob = async {
                            db.insertDailyMixPrev(response.dailyMixPreview)
                        }

                        storeFevArtistMixJob.await()
                        storeAlbumJob.await()
                        storeArtistPrevJob.await()
                        storeDailyMixJob.await()

                        // load from db
                        loadFromDb()
                    }

                    HomeResponseStatus.FAILURE -> {
                        // todo handle error
                    }
                }
            } else {
                loadFromDb()
            }
        }
    }

    private fun loadFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val fevArtistMixPrev = async {
                db.readFevArtistMixPrev()
            }

            val albumPrev = async {
                db.readAllAlbumPrev()
            }

            val artistPrev = async {
                db.readAllArtistPrev()
            }

            val dailyMixPrev = async {

            }
            val playlist = {

            }

            val favourites = async {

            }


            state = state.copy(
                isLoading = false,
                data = HomeUiData(
                    fevArtistMixPrev = fevArtistMixPrev.await(),
                    albumPrev = albumPrev.await(),
                    artistPrev = artistPrev.await()
                )
            )
        }
    }
}