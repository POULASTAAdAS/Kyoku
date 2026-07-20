package com.poulastaa.common.ui.root

import androidx.lifecycle.viewModelScope
import com.poulastaa.common.ui.Screens
import com.poulastaa.common.ui.viewmodel.BaseViewmodel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RootViewmodel : BaseViewmodel<RootUiState, Nothing, Nothing>(
    initialSate = RootUiState(),
) {
    init {
        viewModelScope.launch {
            delay(1600) // keep splash on-screen until the animated icon finishes (~1580ms), matching windowSplashScreenAnimationDuration
            updateState {
                copy(
                    isLoading = false,
                    startDestination = Screens.AuthGraph,
                )
            }
        }
    }

    override suspend fun handleAction(action: Nothing) = Unit
}
