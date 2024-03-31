package com.poulastaa.kyoku.presentation.screen.song_view.artist.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomImageView
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens


@Composable
fun Header(
    modifier: Modifier,
    text: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.4.dp)
                .background(color = MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun ArtistAllItem(
    modifier: Modifier,
    isDarkThem: Boolean,
    title: String,
    year: String,
    coverImage: String,
    isCookie: Boolean,
    headerValue: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(.2f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomImageView(
                isDarkThem = isDarkThem,
                isCookie = isCookie,
                headerValue = headerValue,
                url = coverImage
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = year,
                fontWeight = FontWeight.Light,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    TestThem {
        Column {
            ArtistAllItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(color = MaterialTheme.colorScheme.background),
                isDarkThem = isSystemInDarkTheme(),
                title = "Title",
                year = "2024",
                coverImage = "",
                isCookie = false,
                headerValue = ""
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

            Header(Modifier.fillMaxWidth(), "Album")
        }
    }
}