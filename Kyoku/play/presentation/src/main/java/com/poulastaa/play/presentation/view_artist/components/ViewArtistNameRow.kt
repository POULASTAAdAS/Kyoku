package com.poulastaa.play.presentation.view_artist.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.poulastaa.core.presentation.designsystem.FollowArtistIcon
import com.poulastaa.core.presentation.designsystem.UnFollowArtistIcon

@Composable
fun ViewArtistNameRow(
    modifier: Modifier = Modifier,
    popularity: Long,
    name: String,
    isArtistFollowed: Boolean,
    onFollowArtistToggle: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(.8f),
        ) {
            Text(
                text = name,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.primary.copy(alpha = .8f)
            )

            Text(
                text = "$popularity  Followers",
                color = MaterialTheme.colorScheme.onBackground.copy(.6f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        FollowArtistButton(
            isArtistFollowed = isArtistFollowed,
            onClick = onFollowArtistToggle
        )
    }
}

@Composable
private fun FollowArtistButton(
    isArtistFollowed: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isArtistFollowed)
                Color.Transparent
            else MaterialTheme.colorScheme.onBackground.copy(.2f),
            contentColor = if (isArtistFollowed)
                MaterialTheme.colorScheme.onBackground.copy(.7f)
            else MaterialTheme.colorScheme.primary.copy(.4f)
        )
    ) {
        Icon(
            imageVector = if (isArtistFollowed) UnFollowArtistIcon
            else FollowArtistIcon,
            contentDescription = null,
        )
    }
}