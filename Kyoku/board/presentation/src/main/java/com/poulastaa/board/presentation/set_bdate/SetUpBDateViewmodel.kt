package com.poulastaa.board.presentation.set_bdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.presentation.SnackBarEventManager
import com.poulastaa.core.presentation.designsystem.TextProp
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
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
internal class SetUpBDateViewmodel @Inject constructor(
    val eventManager: SnackBarEventManager,
) : ViewModel() {
    private val _state = MutableStateFlow(SetUpBDateUiState())
    val state = _state.onStart { init() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.minutes.inWholeMilliseconds),
        initialValue = SetUpBDateUiState()
    )

    private val _uiEvent = Channel<SetUpBDateUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: SetUpBDateUiAction) {
        when (action) {
            is SetUpBDateUiAction.OnBDateChange -> _state.update {
                it.copy(bDate = TextProp(action.date))
            }

            SetUpBDateUiAction.OnSubmitClick -> {
                // todo validate
            }
        }
    }

    private fun init() {
        viewModelScope.launch {

        }
    }
}