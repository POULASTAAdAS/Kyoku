package com.poulastaa.setup.presentation.pic_genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

@HiltViewModel
class PicGenreViewmodel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(PicGenreUiState())
    val state = _state
        .onStart {

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PicGenreUiState()
        )

    private val _uiEvent = Channel<PicGenreUiEvent>()
    val event = _uiEvent.receiveAsFlow()

    fun onAction(action: PicGenreUiAction) {
        when (action) {
            is PicGenreUiAction.OnGenreChange -> {
                val oldValue = _state.value.searchGenre.value

                _state.update {
                    it.copy(
                        searchGenre = it.searchGenre.copy(
                            value = action.genre.trim()
                        )
                    )
                }

                if (oldValue == action.genre.trim()) return

                viewModelScope.launch {
                    // todo search
                }
            }

            is PicGenreUiAction.OnGenreSelect -> {
                _state.update {
                    it.copy(
                        data = it.data.map { genre ->
                            if (genre.id == action.id) genre.copy(
                                isSelected = !genre.isSelected
                            ) else genre
                        }
                    )
                }
            }

            PicGenreUiAction.OnContinueClick -> {
                viewModelScope.launch {
                    // todo save genre
                }
            }
        }
    }
}