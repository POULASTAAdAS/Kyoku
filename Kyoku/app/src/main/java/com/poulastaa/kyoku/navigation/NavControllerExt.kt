package com.poulastaa.kyoku.navigation

import androidx.navigation.NavHostController
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent

fun NavHostController.navigate(event: UiEvent.Navigate) = this.navigate(event.route)
fun NavHostController.navigateWithData(event: UiEvent.NavigateWithData) =
    this.navigate("/app/songView/${event.type.title}/${event.id}/${event.name}")
