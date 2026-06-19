package com.poulastaa.setup.ui.select_artist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.design_system.IconAppLogo
import com.poulastaa.common.ui.design_system.IconArtist
import com.poulastaa.common.ui.design_system.IconCheck
import com.poulastaa.common.ui.design_system.dimens
import com.poulastaa.setup.ui.components.SetupSelectionScreen
import kotlin.random.Random


@Immutable
data class UiArtist(
    val id: Long,
    val title: String,
    val icon: String,
    val isSelected: Boolean = false,
)


private val dummyData = (1..30).map {
    UiArtist(
        id = it.toLong(),
        title = "Artist $it",
        icon = "https://picsum.photos/200",
        isSelected = Random.nextBoolean(),
    )
}

@Composable
fun SelectArtist() {
    val navigation = LocalNavController.current
    var isMinGenreSelected by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    val selectedArtists by remember { mutableIntStateOf(5) }

    SetupSelectionScreen(
        title = "Select artists you like",
        searchText = query,
        onSearchTextChange = { query = it },
        searchLabel = "search artists...",
        searchLeadingIcon = IconArtist,
        isMinimumSelectionMet = isMinGenreSelected,
        minimumSelectionText = "Select at-least $selectedArtists more artist to continue.",
        onBackClick = navigation::popBackStack,
        onConfirmClick = {

        },
    ) {
        items(
            items = dummyData,
            key = { item -> item.id }
        ) { item ->
            Column(
                modifier = Modifier.aspectRatio(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Card(
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 3.dp,
                        pressedElevation = 0.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = MaterialTheme.shapes.small,
                    onClick = {

                    }
                ) {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = IconAppLogo,
                            modifier = Modifier.fillMaxSize(0.7f),
                            contentDescription = null
                        )

                        Box(
                            Modifier
                                .clip(
                                    RoundedCornerShape(
                                        topEnd = MaterialTheme.dimens.spacing.small,
                                        bottomStart = MaterialTheme.dimens.spacing.small
                                    )
                                )
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .align(Alignment.TopEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = IconCheck,
                                contentDescription = "check",
                                modifier = Modifier
                                    .padding(MaterialTheme.dimens.spacing.hairline)
                                    .size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                ColumnSpacer(MaterialTheme.dimens.spacing.extraSmall)

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}