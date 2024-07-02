package com.poulastaa.setup.presentation.set_genre

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.poulastaa.core.domain.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class SetGenreViewModel @Inject constructor(
    private val ds: DataStoreRepository,
) : ViewModel() {
    var state by mutableStateOf(GenreUiState())
        private set

    private val selectedIdList: ArrayList<Int> = ArrayList()

    private val _uiEvent = Channel<GenreUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: GenreUiEvent) {
        when (event) {
            is GenreUiEvent.OnGenreClick -> {
                state = state.copy(
                    data = state.data.map {
                        if (it.id == event.id) {
                            if (selectedIdList.contains(it.id)) selectedIdList.remove(it.id)
                            else selectedIdList.add(it.id)

                            it.copy(isSelected = !it.isSelected)
                        } else it
                    }
                )

                state = if (selectedIdList.size > 4) state.copy(
                    isToastVisible = false,
                    canMakeApiCall = true
                )
                else state.copy(
                    isToastVisible = true,
                    canMakeApiCall = false
                )
            }

            GenreUiEvent.OnContinueClick -> {
                if (selectedIdList.size < 4) {
                    state = state.copy(
                        isToastVisible = true,
                        canMakeApiCall = false
                    )

                    return
                }

                state = state.copy(
                    isMakingApiCall = true
                )
            }
        }
    }
}