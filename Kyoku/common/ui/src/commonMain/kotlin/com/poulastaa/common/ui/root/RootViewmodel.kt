package com.poulastaa.common.ui.root

import androidx.lifecycle.viewModelScope
import com.poulastaa.common.ui.Screens
import com.poulastaa.common.ui.viewmodel.BaseViewmodel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RootViewmodel : BaseViewmodel<RootUiState, Nothing, Nothing>(RootUiState()) {
    init {
        viewModelScope.launch {
            delay(1200)
            updateState {
                copy(
                    isLoading = false,
                    startDestination = Screens.AuthGraph,
                )
            }
        }
    }

    override fun handleAction(action: Nothing) = Unit
}
