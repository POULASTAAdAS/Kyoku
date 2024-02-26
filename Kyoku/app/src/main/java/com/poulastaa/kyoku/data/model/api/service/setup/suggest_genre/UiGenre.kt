package com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre

import androidx.compose.ui.graphics.Color
import com.poulastaa.kyoku.ui.theme.listOfSuggestGenreCardColors
import kotlin.random.Random

data class UiGenre(
    val name: String = "",
    val isSelected: Boolean = false,
    val color: Color = listOfSuggestGenreCardColors[Random.nextInt(listOfSuggestGenreCardColors.size)]
)
