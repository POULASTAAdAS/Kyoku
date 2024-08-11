package com.poulastaa.play.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UserIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.imageReq

@Composable
private fun BasicCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSecondary
            else MaterialTheme.colorScheme.background
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) { content() }
}

@Composable
fun SettingBasicCard(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    BasicCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.small3),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { },
                enabled = false,
                colors = IconButtonDefaults.iconButtonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.primaryContainer,
                )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
        }
    }
}

@Composable
fun SettingAccountCard(
    modifier: Modifier = Modifier,
    profilePic: String,
    header: String,
    onAccountClick: () -> Unit,
) {
    BasicCard(
        modifier = modifier,
        onClick = onAccountClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { },
                enabled = false,
                colors = IconButtonDefaults.iconButtonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.primaryContainer,
                )
            ) {
                Icon(
                    imageVector = UserIcon,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

            Text(
                text = stringResource(id = R.string.account),
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )

            Spacer(modifier = Modifier.weight(1f))

            SubcomposeAsyncImage(
                model = imageReq(
                    header = header,
                    url = profilePic
                ),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(56.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary.copy(.8f),
                        shape = CircleShape
                    ),
                loading = {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            strokeWidth = 1.7.dp,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(16.dp)
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
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.small1))
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(MaterialTheme.dimens.large1)
        ) {
            SettingAccountCard(
                modifier = Modifier
                    .fillMaxWidth(),
                profilePic = "",
                header = ""
            ) {}
        }
    }
}