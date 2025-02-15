package com.poulastaa.setup.presentation.pic_genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.setup.domain.repository.set_genre.SetGenreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PicGenreViewmodel @Inject constructor(
    private val repo: SetGenreRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(PicGenreUiState())
    val state = _state
        .onStart {
            getGenreJob?.cancel()
            getGenreJob = suggestGenre()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PicGenreUiState()
        )

    private val _uiEvent = Channel<PicGenreUiEvent>()
    val event = _uiEvent.receiveAsFlow()

    private val _genre: MutableStateFlow<PagingData<UiGenre>> =
        MutableStateFlow(PagingData.empty())
    val genre = _genre.asStateFlow()

    private var getGenreJob: Job? = null

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
                    getGenreJob?.cancel()
                    getGenreJob = suggestGenre()
                }
            }

            is PicGenreUiAction.OnGenreSelect -> {
                _genre.value = _genre.value.map { genre ->
                    if (genre.id == action.id) genre.copy(isSelected = !action.isSelected)
                    else genre
                }

                val savedList = _state.value.data.toMutableList()

                if (action.isSelected) savedList.remove(action.id)
                else if (!savedList.contains(action.id)) savedList.add(action.id)

                _state.update {
                    it.copy(
                        data = savedList.toList()
                    )
                }
            }

            PicGenreUiAction.OnContinueClick -> {
                viewModelScope.launch {
                    when (val result = repo.saveGenre(_state.value.data)) {
                        is Result.Error -> {
                            when (result.error) {
                                DataError.Network.NO_INTERNET -> _uiEvent.send(
                                    PicGenreUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.error_no_internet
                                        )
                                    )
                                )

                                else -> _uiEvent.send(
                                    PicGenreUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.error_something_went_wrong
                                        )
                                    )
                                )
                            }
                        }

                        is Result.Success -> _uiEvent.send(PicGenreUiEvent.OnSuccess)
                    }
                }

                _state.update {
                    it.copy(
                        isMakingApiCall = false
                    )
                }
            }
        }
    }

    private fun suggestGenre() = viewModelScope.launch {
        _genre.value = PagingData.empty()

        repo.getPagingGenre(
            query = _state.value.searchGenre.value.trim()
        ).cachedIn(viewModelScope)
            .distinctUntilChanged()
            .collectLatest { pagingData ->
            _genre.value = pagingData.map { it.toUiGenre(_state.value.data) }
        }
    }
}