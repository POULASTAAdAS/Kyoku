package com.poulastaa.common.ui.root

import androidx.lifecycle.viewModelScope
import com.poulastaa.common.ui.Screens
import com.poulastaa.common.ui.viewmodel.BaseViewmodel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RootViewmodel : BaseViewmodel<RootUiState, Nothing, Nothing>(RootUiState()) {
    init {
        viewModelScope.launch {
            delay(800)
            updateState {
                copy(startDestination = Screens.AuthGraph)
            }
        }
    }

    override fun handleAction(action: Nothing) = Unit
}
