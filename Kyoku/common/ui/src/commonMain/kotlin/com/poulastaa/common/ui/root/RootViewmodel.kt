package com.poulastaa.common.ui.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.common.ui.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RootViewmodel : ViewModel() {
    val state = MutableStateFlow(RootUiState())

    init {
        viewModelScope.launch {
            delay(800)
            state.update {
                it.copy(startDestination = Screens.AuthScreens.SignIn)
            }
        }
    }
}