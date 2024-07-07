package com.poulastaa.play.presentation.root_drawer.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.play.domain.getCurrentTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ds: DataStoreRepository,
) : ViewModel() {
    var state by mutableStateOf(HomeUiState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(
                heading = getCurrentTime(),
            )
        }
    }


    fun onEvent(event: HomeUiEvent) {

    }
}