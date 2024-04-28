package com.poulastaa.kyoku.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.data.model.screens.view_aritst.ViewArtistUiArtist
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomImageView
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun ArtistView(
    data: ViewArtistUiArtist,
    modifier: Modifier,
    imageSize: Dp,
    isDarkThem: Boolean,
    isCookie: Boolean,
    header: String,
    nameFontSize: TextUnit,
    listenedFontSize: TextUnit,
    nameColor: Color = MaterialTheme.colorScheme.onBackground,
    listenedColor: Color = MaterialTheme.colorScheme.onBackground,
    spacer: Dp = MaterialTheme.dimens.medium1,
) {
    Row(
        modifier = modifier
    ) {
        CustomImageView(
            modifier = Modifier
                .size(imageSize)
                .clip(MaterialTheme.shapes.extraSmall),
            isDarkThem = isDarkThem,
            isCookie = isCookie,
            headerValue = header,
            url = data.coverImage
        )

        Spacer(modifier = Modifier.width(spacer))

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = data.name,
                fontWeight = FontWeight.Medium,
                fontSize = nameFontSize,
                color = nameColor
            )

            Text(
                text = "${data.listened} listened",
                fontWeight = FontWeight.Light,
                fontSize = listenedFontSize,
                color = listenedColor
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TestThem {
        ArtistView(
            data = ViewArtistUiArtist(),
            modifier = Modifier.size(120.dp),
            imageSize = 120.dp,
            isDarkThem = false,
            isCookie = false,
            header = "",
            nameFontSize = MaterialTheme.typography.titleLarge.fontSize,
            listenedFontSize = MaterialTheme.typography.titleSmall.fontSize
        )
    }
}