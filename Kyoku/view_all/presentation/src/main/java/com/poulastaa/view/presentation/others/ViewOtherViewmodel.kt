package com.poulastaa.view.presentation.others

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.ViewType
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.ERROR_LOTTIE_ID
import com.poulastaa.view.domain.repository.ViewOtherRepository
import com.poulastaa.view.presentation.mapper.toUiViewPrevSong
import com.poulastaa.view.presentation.others.mapper.toUiRoot
import com.poulastaa.view.presentation.others.mapper.toUiViewType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@HiltViewModel
internal class ViewOtherViewmodel @Inject constructor(
    private val repo: ViewOtherRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ViewOtherUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(8.toDuration(DurationUnit.SECONDS)),
        initialValue = ViewOtherUiState()
    )

    private val _uiState = Channel<ViewOtherUiEvent>()
    val uiState = _uiState.receiveAsFlow()

    fun onAction(action: ViewOtherUiAction) {

    }

    fun init(otherId: Long, type: ViewType) {
        viewModelScope.launch {
            when (val result = repo.getViewData(type, otherId)) {
                is Result.Error -> when (result.error) {
                    DataError.Network.CONFLICT -> {
                        _state.update { state ->
                            state.copy(
                                loadingType = LoadingType.Error(
                                    type = LoadingType.ERROR_TYPE.NO_INTERNET,
                                    lottieId = ERROR_LOTTIE_ID
                                )
                            )
                        }

                        _uiState.send(
                            ViewOtherUiEvent.EmitToast(
                                UiText.StringResource(
                                    R.string.error_no_internet
                                )
                            )
                        )
                    }

                    else -> {
                        _state.update { state ->
                            state.copy(
                                loadingType = LoadingType.Error(
                                    type = LoadingType.ERROR_TYPE.UNKNOWN,
                                    lottieId = ERROR_LOTTIE_ID
                                )
                            )
                        }

                        _uiState.send(
                            ViewOtherUiEvent.EmitToast(
                                UiText.StringResource(
                                    R.string.error_something_went_wrong
                                )
                            )
                        )
                    }
                }

                is Result.Success -> {
                    val data = result.data

                    _state.update { state ->
                        state.copy(
                            loadingType = LoadingType.Content,
                            type = type.toUiViewType(),
                            root = data.heading.toUiRoot(),
                            listOfSongs = data.listOfSongs.map { it.toUiViewPrevSong() }
                        )
                    }
                }
            }
        }
    }
}