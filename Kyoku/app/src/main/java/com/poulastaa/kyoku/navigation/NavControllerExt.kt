package com.poulastaa.kyoku.navigation

import androidx.navigation.NavHostController
import com.poulastaa.kyoku.data.model.auth.AuthUiEvent

fun NavHostController.navigate(event: AuthUiEvent.Navigate) = this.navigate(event.route)