package com.poulastaa.kyoku.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val dataStore: DataStoreOperation,
) : ViewModel() {
    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination get() = _startDestination.asStateFlow()

    private val _keepSplashOn = MutableStateFlow(true)
    val keepSplashOn get() = _keepSplashOn.asStateFlow()

    init { // read sign in state
        viewModelScope.launch {
            dataStore.readSignedInState().collect {
                _startDestination.value = if (it) Screens.Home.route else Screens.Auth.route
                _keepSplashOn.value = false
            }
        }
    }
}