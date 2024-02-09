package com.poulastaa.kyoku.navigation

import androidx.navigation.NavHostController
import com.poulastaa.kyoku.data.model.auth.UiEvent

fun NavHostController.navigate(event: UiEvent.Navigate) = this.navigate(event.route)