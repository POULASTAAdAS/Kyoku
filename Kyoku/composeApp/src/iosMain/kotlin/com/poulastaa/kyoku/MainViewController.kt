package com.poulastaa.kyoku

import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.auth.ui.components.GoogleAuthWrapper
import com.poulastaa.auth.ui.components.registerGoogleAuthWrapper
import com.poulastaa.common.ui.design_system.AppTheme
import com.poulastaa.common.ui.root.RootViewmodel
import com.poulastaa.kyoku.di.initKoin
import org.koin.compose.viewmodel.koinViewModel

fun MainViewController(
    googleAuthWrapper: GoogleAuthWrapper,
) = ComposeUIViewController(
    configure = {
        initKoinIos()
        registerGoogleAuthWrapper(googleAuthWrapper)
    },
) {
    AppTheme {
        val viewmodel = koinViewModel<RootViewmodel>()
        val rootState by viewmodel.uiState.collectAsStateWithLifecycle()
        RootNavigation(state = rootState)
    }
}

fun initKoinIos() {
    initKoin()
}
