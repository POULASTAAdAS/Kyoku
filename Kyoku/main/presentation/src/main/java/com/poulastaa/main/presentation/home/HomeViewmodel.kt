package com.poulastaa.main.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.main.domain.repository.HomeRepository
import com.poulastaa.main.presentation.home.mapper.toUiHomeState
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
import kotlin.random.Random

@HiltViewModel
internal class HomeViewmodel @Inject constructor(
    private val repo: HomeRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(prevData)
    val state = _state.onStart {

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // 5 seconds
        initialValue = HomeUiState()
    )

    private val _uiEvent = Channel<HomeUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun init(isInitial: Boolean) {
        viewModelScope.launch {
            if (isInitial) repo.getHome()
            loadData()
        }
    }

    fun onAction(action: HomeUiAction) {
        Log.d("Action", action.toString())

        when (action) {
            is HomeUiAction.OnSavedItemCLick -> {}
            is HomeUiAction.OnExploreTypeItemClick -> {}
            is HomeUiAction.OnViewMore -> {}
            is HomeUiAction.OnSuggestedAlbumLongClick -> {}
            is HomeUiAction.OnSuggestedArtistLongClick -> {}
            is HomeUiAction.OnSuggestArtistSongClick -> {}
        }
    }

    private suspend fun loadData() {
        val home = repo.loadData()

        _state.update {
            home.toUiHomeState(home.savedItems.random(Random))
        }
    }
}