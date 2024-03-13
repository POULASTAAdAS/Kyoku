package com.poulastaa.kyoku.presentation.screen.home_root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.utils.storeSignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeRootViewModel @Inject constructor(
    private val ds: DataStoreOperation
) : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: HomeRootUiEvent) {
        when (event) {
            is HomeRootUiEvent.Navigate -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.Navigate(event.route))
                }
            }
            HomeRootUiEvent.LogOut -> {
                storeSignInState(SignInStatus.AUTH, ds)
            }
        }
    }
}