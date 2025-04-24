package com.poulastaa.add.presentation.artist.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.add.presentation.artist.AddArtistUiAction
import com.poulastaa.add.presentation.artist.UiArtist
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ui.UserIcon

@Composable
internal fun AddArtistCard(
    modifier: Modifier = Modifier,
    item: UiArtist,
    haptic: HapticFeedback,
    onAction: (AddArtistUiAction.OnItemClick) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize(.75f)
                .aspectRatio(1f),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp,
                pressedElevation = 0.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                onAction(AddArtistUiAction.OnItemClick(item))
                if (item.isSelected.not()) haptic.performHapticFeedback(
                    HapticFeedbackType.LongPress
                )
            }
        ) {
            SubcomposeAsyncImage(
                model = CacheImageReq.imageReq(
                    item.poster,
                    LocalContext.current
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxSize(.4f),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = UserIcon,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(.7f),
                            tint = MaterialTheme.colorScheme.background
                        )
                    }
                }
            )

        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.9f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
        }
    }
}