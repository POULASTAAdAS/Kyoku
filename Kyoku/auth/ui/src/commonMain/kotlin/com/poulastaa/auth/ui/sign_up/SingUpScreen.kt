package com.poulastaa.auth.ui.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.components.BackButton
import com.poulastaa.common.ui.design_system.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingUpScreen() {
    val navController = LocalNavController.current
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    // TODO: will be moved to viewmodel
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isError by remember { mutableStateOf(false) }
    val isPasswordVisible = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    BackButton(
                        onClick = navController::navigateUp
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(MaterialTheme.dimens.layout.contentPadding)
        ) {

        }
    }
}