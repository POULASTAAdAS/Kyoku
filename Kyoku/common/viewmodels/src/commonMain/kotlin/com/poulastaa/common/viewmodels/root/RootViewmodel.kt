package com.poulastaa.common.viewmodels.root

import androidx.lifecycle.viewModelScope
import com.poulastaa.common.viewmodels.BaseViewmodel
import com.poulastaa.common.viewmodels.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class RootViewmodel : BaseViewmodel<RootUiState, Nothing, Nothing>(
    initialSate = RootUiState(),
) {
    init {
        viewModelScope.launch {
            delay(1600.milliseconds) // keep splash on-screen until the animated icon finishes (~1580ms), matching windowSplashScreenAnimationDuration
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
