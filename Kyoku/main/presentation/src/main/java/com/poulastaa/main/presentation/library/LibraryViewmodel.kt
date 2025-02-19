package com.poulastaa.main.presentation.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@HiltViewModel
internal class LibraryViewmodel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(LibraryUiState())
    val state = _state.onStart {
//        stopLoadingAfterFiveSecIfNoData()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = LibraryUiState()
    )

    fun onAction(action: LibraryUiAction) {
        when (action) {
            is LibraryUiAction.OnFilterTypeClick -> {

            }
        }
    }

    private fun stopLoadingAfterFiveSecIfNoData() {
        viewModelScope.launch {
            delay(6.0.toDuration(DurationUnit.SECONDS))

            if (_state.value.canShowUi.not()) {
                _state.update {
                    it.copy(
                        noSavedData = true
                    )
                }
            }
        }
    }
}