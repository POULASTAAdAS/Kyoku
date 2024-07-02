package com.poulastaa.setup.presentation.set_genre.model

import androidx.compose.ui.graphics.Color
import com.poulastaa.core.presentation.designsystem.listOfSuggestGenreCardColors
import kotlin.random.Random

data class UiGenre(
    val id: Int = -1,
    val name: String = "",
    val isSelected: Boolean = false,
    val color: Color = listOfSuggestGenreCardColors[Random.nextInt(listOfSuggestGenreCardColors.size)],
)
