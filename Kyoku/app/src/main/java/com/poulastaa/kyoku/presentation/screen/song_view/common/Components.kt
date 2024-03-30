package com.poulastaa.kyoku.presentation.screen.song_view.common

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomImageView
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun SongCard(
    modifier: Modifier,
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String,
    title: String,
    artist: String,
    coverImage: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
    ) {
        SongCardDragButton(
            modifier = Modifier
                .fillMaxWidth(.07f)
                .padding(start = MaterialTheme.dimens.small3)
        )


        Row(
            modifier = Modifier
                .fillMaxWidth(.75f)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
        ) {
            CustomImageView(
                modifier = Modifier
                    .fillMaxWidth(.2f)
                    .clip(MaterialTheme.shapes.extraSmall),
                isDarkThem = isDarkThem,
                isCookie = isCookie,
                headerValue = headerValue,
                url = coverImage,
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Medium,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = artist,
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(.5f)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .clickable(
                        onClick = {

                        }
                    ),
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_song_card_add),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(34.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.extraSmall)
                    .clickable(
                        onClick = {

                        }
                    ),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(34.dp)
                )
            }
        }
    }
}

@Composable
fun SongCardDragButton(
    modifier: Modifier,
    radius: Float = 2.4f,
    padding: Dp = 3.dp,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Column {
            repeat(5) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = padding,
                            top = padding
                        )
                ) {
                    drawCircle(
                        color = color,
                        radius = radius
                    )
                }
            }
        }
        Column {
            repeat(5) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = padding,
                            top = padding
                        )
                ) {
                    drawCircle(
                        color = color,
                        radius = radius
                    )
                }
            }
        }
    }
}


fun LazyListScope.navigateBackButton(
    navigateBack: () -> Unit
){
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = navigateBack,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null
                )
            }
        }
    }
}


fun LazyListScope.poster(
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String,
    poster: String,
    isSmallPhone: Boolean,
) {
    item {
        Card(
            modifier = Modifier
                .size(if (isSmallPhone) 200.dp else 240.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            CustomImageView(
                modifier = Modifier.fillMaxSize(),
                isDarkThem = isDarkThem,
                isCookie = isCookie,
                headerValue = headerValue,
                url = poster,
                contentScale = ContentScale.Fit
            )
        }
    }
}

fun LazyListScope.info(
    name: String,
    size: Int
) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = MaterialTheme.dimens.medium1),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = name,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "$size songs",
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Light
            )
        }
    }
}

fun LazyListScope.playControl(
    isDownloading: Boolean,
    onDownloadClick: () -> Unit,
    onShuffleClick: () -> Unit,
    onPlayClick: () -> Unit
) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = MaterialTheme.dimens.medium1,
                    end = MaterialTheme.dimens.medium1
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(.1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = onDownloadClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier // todo show loading animation with border while downloading
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = CircleShape
                        )
                        .size(25.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_down),
                            contentDescription = null
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_shuffle),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable {
                            onShuffleClick.invoke()
                        }
                )

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

                Icon(
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .clickable {
                            onPlayClick.invoke()
                        }
                )
            }
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
        SongCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(color = MaterialTheme.colorScheme.background)
                .clickable(
                    onClick = {

                    }
                ),
            isDarkThem = isSystemInDarkTheme(),
            isCookie = false,
            headerValue = "",
            title = "Title",
            artist = "Artist",
            coverImage = ""
        )
    }
}