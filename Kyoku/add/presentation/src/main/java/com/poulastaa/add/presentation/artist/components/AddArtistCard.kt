package com.poulastaa.add.presentation.artist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.add.presentation.artist.AddArtistUiAction
import com.poulastaa.add.presentation.artist.UiArtist
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.UserIcon

@Composable
internal fun AddArtistCard(
    modifier: Modifier = Modifier,
    isEditEnabled: Boolean,
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
            Box(Modifier.fillMaxSize()) {
                SubcomposeAsyncImage(
                    model = CacheImageReq.imageReq(
                        item.poster,
                        LocalContext.current
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .blur(if (item.isSelected) 2.dp else 0.dp),
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

                this@Card.AnimatedVisibility(
                    visible = item.isSelected,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .padding(12.dp)
                        .clip(CircleShape)
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

                this@Card.AnimatedVisibility(
                    visible = isEditEnabled,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Checkbox(
                        checked = item.isSelected,
                        onCheckedChange = {
                            onAction(AddArtistUiAction.OnItemClick(item))
                        },
                        colors = CheckboxDefaults.colors(
                            checkmarkColor = MaterialTheme.colorScheme.background,
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.primary,
                        )
                    )
                }
            }
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

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AddArtistCard(
                modifier = Modifier.size(120.dp),
                item = UiArtist(
                    id = 1,
                    name = "Artist Name",
                    poster = "",
                    isSelected = isSystemInDarkTheme()
                ),
                isEditEnabled = true,
                haptic = LocalHapticFeedback.current,
                onAction = { }
            )
        }
    }
}