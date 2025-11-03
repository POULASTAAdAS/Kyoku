package com.poulastaa.board.presentation.set_bdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
internal class SetUpBDateViewmodel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(SetUpBDateUiState())
    val state = _state.onStart { init() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.minutes.inWholeMilliseconds),
        initialValue = SetUpBDateUiState()
    )

    private fun init() {
        viewModelScope.launch {

        }
    }
}