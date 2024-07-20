package com.poulastaa.play.presentation.root_drawer.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.home.HomeRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.play.presentation.root_drawer.home.mapper.getCurrentTime
import com.poulastaa.play.presentation.root_drawer.home.mapper.getDayType
import com.poulastaa.play.presentation.root_drawer.home.mapper.toUiHomeData
import com.poulastaa.play.presentation.root_drawer.home.mapper.toUiPlaylist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val homeRepo: HomeRepository,
) : ViewModel() {
    var state by mutableStateOf(HomeUiState())
        private set

    init {
        setHomeTopBarTitle()
        readHeader()
        loadData()
    }

    fun onEvent(event: HomeUiEvent) {

    }


    private fun setHomeTopBarTitle() {
        viewModelScope.launch {
            state = state.copy(
                heading = getCurrentTime(),
            )
        }
    }

    private fun readHeader() = viewModelScope.launch {
        ds.readTokenOrCookie().collectLatest {
            state = state.copy(
                header = it
            )
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val newUserDef = async { homeRepo.isNewUser() }

            val populateDef = async { populate() }

            if (newUserDef.await()) return@launch loadNewUserData()
            populateDef.await()
            loadSavedPlaylist()
        }
    }

    private fun loadNewUserData() {
        viewModelScope.launch {
            val result = homeRepo.storeNewHomeResponse(
                dayType = getDayType()
            )

            when (result) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Network.NO_INTERNET -> {

                        }

                        else -> {

                        }
                    }
                }

                is Result.Success -> populate()
            }
        }
    }

    private suspend fun populate(): Unit = withContext(Dispatchers.IO) {
        val data = homeRepo.loadHomeData()

        withContext(Dispatchers.Main) { // canShowUi is not updating without thread change
            state = state.copy(
                isNewUser = false,
                isDataLoading = false,
                staticData = data.toUiHomeData()
            )
        }
    }

    private fun loadSavedPlaylist() {
        viewModelScope.launch {
            homeRepo.loadSavedPlaylist().map {
                it.map { result ->
                    result.toUiPlaylist()
                }
            }.map {
                state = state.copy(
                    savedPlaylists = it
                )
            }
        }
    }
}