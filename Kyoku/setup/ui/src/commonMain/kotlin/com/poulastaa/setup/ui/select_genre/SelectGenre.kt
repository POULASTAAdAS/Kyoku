package com.poulastaa.setup.ui.select_genre

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.components.ScreenSizeType
import com.poulastaa.common.ui.components.ScreenSizeWrapper
import com.poulastaa.setup.ui.select_genre.screens.CompatVerticalSelectGenreScreen
import kotlin.random.Random

@Immutable
data class UiGenre(
    val id: Long,
    val title: String,
    val icon: String,
    val isSelected: Boolean = false,
)


private val dummyData = (1..30).map {
    UiGenre(
        id = it.toLong(),
        title = "Genre $it",
        icon = "https://picsum.photos/200",
        isSelected = Random.nextBoolean(),
    )
}


@Composable
fun SelectGenre() {
    val navigation = LocalNavController.current
    var isMinGenreSelected by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    val selectedGenres by remember { mutableIntStateOf(5) }

    ScreenSizeWrapper { screenSizeType ->
        when (screenSizeType) {
            ScreenSizeType.CompactVertical -> CompatVerticalSelectGenreScreen(
                navigation,
                isMinGenreSelected,
                query,
                onQueryChange = { query = it },
                selectedGenres,
                dummyData
            )

            ScreenSizeType.CompactHorizontal -> TODO()

            ScreenSizeType.LargeVertical -> TODO()
            ScreenSizeType.LargeHorizontal -> TODO()
        }
    }
}
