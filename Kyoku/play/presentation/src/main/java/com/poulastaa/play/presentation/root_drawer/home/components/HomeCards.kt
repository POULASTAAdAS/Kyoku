package com.poulastaa.play.presentation.root_drawer.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.MoreFromArtistIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UserIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.imageReq
import com.poulastaa.core.presentation.ui.imageReqSongCover
import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.core.presentation.ui.model.UiPrevPlaylist
import com.poulastaa.play.presentation.root_drawer.home.model.UiPrevAlbum
import com.poulastaa.play.presentation.root_drawer.home.model.UiSongWithInfo

private val defaultElevationSmall = 6.dp
private val defaultElevationMedium = 10.dp

@Composable
fun SavedPlaylistCard(
    modifier: Modifier = Modifier,
    header: String,
    uiPlaylist: UiPrevPlaylist,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(
            defaultElevation = defaultElevationSmall
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
        ) {
            ImageGrid(
                modifier = Modifier.aspectRatio(1f),
                header = header,
                urls = uiPlaylist.urls,
                elevation = CardDefaults.cardElevation(0.dp)
            )

            Text(
                text = uiPlaylist.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SavedAlbumCard(
    modifier: Modifier = Modifier,
    header: String,
    uiAlbum: UiPrevAlbum,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(
            defaultElevation = defaultElevationSmall
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
        ) {
            AsyncImage(
                model = imageReqSongCover(
                    header = header,
                    url = uiAlbum.coverImage
                ),
                modifier = Modifier
                    .aspectRatio(1f),
                contentDescription = null
            )

            Text(
                text = uiAlbum.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Composable
fun HomePlaylistCard(
    modifier: Modifier = Modifier,
    header: String,
    uiPrevPlaylist: UiPrevPlaylist,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    elevation: CardElevation = CardDefaults.cardElevation(
        defaultElevation = defaultElevationSmall
    ),
    colors: CardColors = CardDefaults.cardColors(),
    onClick: (id: Long) -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        elevation = elevation,
        colors = colors,
        onClick = {
            onClick(uiPrevPlaylist.id)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageGrid(
                modifier = Modifier.aspectRatio(1f),
                header = header,
                urls = uiPrevPlaylist.urls,
                elevation = CardDefaults.cardElevation(0.dp)
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = uiPrevPlaylist.name,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun HomeAlbumCard(
    modifier: Modifier = Modifier,
    header: String,
    prevAlbum: UiPrevAlbum,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    elevation: CardElevation = CardDefaults.cardElevation(
        defaultElevation = defaultElevationMedium
    ),
    colors: CardColors = CardDefaults.cardColors(),
) {
    Card(
        modifier = modifier,
        shape = shape,
        elevation = elevation,
        colors = colors
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AsyncImage(
                model = imageReqSongCover(
                    header = header,
                    url = prevAlbum.coverImage
                ),
                modifier = Modifier
                    .fillMaxSize(),
                contentDescription = null
            )

            Text(
                text = prevAlbum.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.primary.copy(.7f),
                                MaterialTheme.colorScheme.primary.copy(.7f),
                            ),
                        )
                    )
                    .padding(top = MaterialTheme.dimens.medium1),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun GridImageCard(
    modifier: Modifier = Modifier,
    size: Dp = 90.dp,
    header: String,
    urls: List<String>,
    title: String,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        ImageGrid(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(size),
            header = header,
            urls = urls
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))

        Text(
            modifier = Modifier
                .width(size)
                .align(Alignment.CenterHorizontally),
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SingleSongCard(
    modifier: Modifier = Modifier,
    header: String,
    song: UiSongWithInfo,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = defaultElevationSmall
        ),
        shape = MaterialTheme.shapes.small,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AsyncImage(
                model = imageReqSongCover(
                    header = header,
                    url = song.coverImage
                ),
                modifier = Modifier
                    .fillMaxSize(),
                contentDescription = null
            )

            Text(
                text = song.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.primary.copy(.8f),
                                MaterialTheme.colorScheme.primary.copy(.8f),
                            ),
                        )
                    )
                    .padding(top = MaterialTheme.dimens.small1)
                    .padding(horizontal = MaterialTheme.dimens.small1),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SuggestedArtistCard(
    modifier: Modifier = Modifier,
    artist: UiArtist,
    header: String,
    fontSize: TextUnit = MaterialTheme.typography.titleLarge.fontSize,
    maxLine: Int = 1,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularArtist(
            modifier = Modifier.fillMaxSize(.75f),
            header = header, url = artist.coverImageUrl
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        Text(
            text = artist.name,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = maxLine,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MoreFromArtist(
    modifier: Modifier = Modifier,
    header: String,
    artist: UiArtist,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.small2)
            .then(modifier)
    ) {
        CircularArtist(
            modifier = Modifier.aspectRatio(1f),
            header = header,
            url = artist.coverImageUrl
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.more_from_artist),
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                color = MaterialTheme.colorScheme.onBackground.copy(.9f),
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = artist.name,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.primary.copy(.9f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            imageVector = MoreFromArtistIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(.9f)
        )
    }
}

@Composable
fun ViewMore(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = defaultElevationMedium
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(.7f),
                            MaterialTheme.colorScheme.inversePrimary.copy(.5f),
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.view_more),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Composable
fun CircularArtist(
    modifier: Modifier = Modifier,
    header: String,
    url: String,
) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = defaultElevationSmall
        )
    ) {
        SubcomposeAsyncImage(
            model = imageReq(
                header = header,
                url = url
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            error = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = UserIcon,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            color = MaterialTheme.colorScheme.onBackground.copy(.7f)
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(MaterialTheme.dimens.small3)
                    )
                }
            },
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 1.5.dp,
                        strokeCap = StrokeCap.Round,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
        )
    }
}

@Composable
private fun ImageGrid(
    modifier: Modifier = Modifier,
    header: String,
    urls: List<String>,
    elevation: CardElevation = CardDefaults.cardElevation(
        defaultElevation = defaultElevationSmall
    ),
    colors: CardColors = CardDefaults.cardColors(),
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        elevation = elevation,
        colors = colors
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.5f)
        ) {
            AsyncImage(
                model = imageReqSongCover(
                    header = header,
                    url = urls.getOrElse(0) { "" }
                ),
                modifier = Modifier
                    .fillMaxWidth(.5f)
                    .fillMaxHeight(),
                contentDescription = null
            )

            AsyncImage(
                model = imageReqSongCover(
                    header = header,
                    url = urls.getOrElse(1) { "" }
                ),
                modifier = Modifier
                    .fillMaxSize(),
                contentDescription = null
            )
        }

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = imageReqSongCover(
                    header = header,
                    url = urls.getOrElse(2) { "" }
                ),
                modifier = Modifier
                    .fillMaxWidth(.5f)
                    .fillMaxHeight(),
                contentDescription = null
            )

            AsyncImage(
                model = imageReqSongCover(
                    header = header,
                    url = urls.getOrElse(3) { "" }
                ),
                modifier = Modifier
                    .fillMaxSize(),
                contentDescription = null
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    val data = (1..2).map {
        UiArtist(
            id = 1,
            name = "Artist $it",
        )
    }

    AppThem {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1),
        ) {
            Row(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
            ) {
                data.forEach {
                    SuggestedArtistCard(
                        modifier = Modifier.aspectRatio(1f),
                        header = "",
                        artist = it,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                }
            }
        }
    }
}