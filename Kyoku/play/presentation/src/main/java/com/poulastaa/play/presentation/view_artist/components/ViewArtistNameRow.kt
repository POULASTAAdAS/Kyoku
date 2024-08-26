package com.poulastaa.play.presentation.view_artist.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R

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
            modifier = Modifier.fillMaxWidth(.75f),
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
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(
            width = if (isArtistFollowed) 1.3.dp else 2.dp,
            color = if (isArtistFollowed) MaterialTheme.colorScheme.onBackground.copy(.2f)
            else MaterialTheme.colorScheme.primary.copy(.8f),
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isArtistFollowed) MaterialTheme.colorScheme.onBackground.copy(.15f) else
                Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.un_follow),
                modifier = Modifier.alpha(if (isArtistFollowed) 1f else 0f)
            )

            Text(
                text = stringResource(id = R.string.follow),
                modifier = Modifier.alpha(if (isArtistFollowed) 0f else 1f)
            )
        }
    }
}