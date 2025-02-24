package com.poulastaa.main.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.noRippleClickable
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.SongIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
fun MainBoxImageCard(
    modifier: Modifier = Modifier,
    title: String,
    urls: List<String?>,
    description: String? = null,
    icon: ImageVector,
) {
    val density = LocalDensity.current
    var cardWidthDp by remember { mutableStateOf(0.dp) }
    val fontSize = if (urls.size <= 3) MaterialTheme.typography.bodyMedium.fontSize
    else MaterialTheme.typography.titleMedium.fontSize

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight(.75f)
                .aspectRatio(1f)
                .onSizeChanged { size ->
                    cardWidthDp = with(density) { size.width.toDp() }
                },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp,
                pressedElevation = 0.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = MaterialTheme.shapes.small,
        ) {
            if (urls.size <= 3) MainImageCard(
                errorIcon = icon,
                url = urls.firstOrNull(),
                contentDescription = description,
                modifier = Modifier.fillMaxSize()
            ) else Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.5f)
                ) {
                    MainImageCard(
                        url = urls.getOrNull(0),
                        contentDescription = description,
                        errorIcon = icon,
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .fillMaxHeight()
                    )

                    MainImageCard(
                        url = urls.getOrNull(1),
                        errorIcon = icon,
                        contentDescription = description,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    MainImageCard(
                        url = urls.getOrNull(2),
                        errorIcon = icon,
                        contentDescription = description,
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .fillMaxHeight()
                    )

                    MainImageCard(
                        url = urls.getOrNull(3),
                        errorIcon = icon,
                        contentDescription = description,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Spacer(Modifier.height(MaterialTheme.dimens.small2))

        Text(
            modifier = Modifier.widthIn(max = cardWidthDp),
            text = title,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = fontSize,
            lineHeight = fontSize,
            textAlign = TextAlign.Center,
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.medium1)
            ) {
                Row(
                    modifier = Modifier
                        .height(120.dp)
                ) {
                    MainBoxImageCard(
                        modifier = Modifier.aspectRatio(1f),
                        urls = listOf(),
                        title = "That Cool Album",
                        icon = SongIcon,
                    )

                    Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                    MainBoxImageCard(
                        modifier = Modifier.aspectRatio(1f),
                        urls = listOf(""),
                        title = "That Cool Album",
                        icon = SongIcon,
                    )
                }

                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                Row(
                    modifier = Modifier
                        .height(120.dp)
                ) {
                    MainBoxImageCard(
                        modifier = Modifier.aspectRatio(1f),
                        urls = listOf("", "", "", ""),
                        title = "That Cool Playlist",
                        icon = SongIcon,
                    )

                    Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                    MainBoxImageCard(
                        modifier = Modifier.aspectRatio(1f),
                        urls = listOf("", "", "", ""),
                        title = "That Cool Playlist",
                        icon = SongIcon,
                    )
                }
            }
        }
    }
}