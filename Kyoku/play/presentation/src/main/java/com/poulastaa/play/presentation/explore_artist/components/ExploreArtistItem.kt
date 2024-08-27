package com.poulastaa.play.presentation.explore_artist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.poulastaa.core.presentation.designsystem.ThreeDotIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.imageReqSongCover
import com.poulastaa.play.presentation.explore_artist.ExploreArtistSingleUiData

@Composable
fun ExploreArtistItem(
    modifier: Modifier = Modifier,
    header: String,
    item: ExploreArtistSingleUiData,
    onThreeDotCLick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.dimens.small3)
            .height(80.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.aspectRatio(1f),
            shape = MaterialTheme.shapes.extraSmall,
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            AsyncImage(
                model = imageReqSongCover(
                    header = header,
                    url = item.coverImage
                ),
                modifier = Modifier
                    .aspectRatio(1f),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.8f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.title,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = item.releaseYear.toString(),
                color = MaterialTheme.colorScheme.onBackground.copy(.7f),
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onThreeDotCLick) {
            Icon(
                imageVector = ThreeDotIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(.6f)
            )
        }
    }
}