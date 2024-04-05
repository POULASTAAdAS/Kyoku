package com.poulastaa.kyoku.presentation.screen.home_root.home.component

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.data.model.screens.home.HomeUiArtistPrev
import com.poulastaa.kyoku.data.model.screens.home.HomeUiEvent
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun LazyListScope.homeScreenArtistList(
    artistPrev: List<HomeUiArtistPrev>,
    isSmallPhone: Boolean,
    isCookie: Boolean,
    headerValue: String,
    scope: CoroutineScope,
    onClick: (HomeUiEvent.ItemClick) -> Unit
) {
    items(artistPrev.size) { artistIndex ->
        Row(
            modifier = Modifier
                .height(if (isSmallPhone) 60.dp else 70.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .windowInsetsPadding(
                    insets = WindowInsets(
                        left = MaterialTheme.dimens.medium1,
                        right = MaterialTheme.dimens.medium1
                    )
                )
                .clickable {
                    scope.launch {
                        onClick.invoke(
                            HomeUiEvent.ItemClick(
                                type = ItemsType.ARTIST,
                                name = artistPrev[artistIndex].name,
                                id = artistPrev[artistIndex].id,
                                isApiCall = true
                            )
                        )
                    }
                },
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
        ) {
            HomeScreenCard(
                size = 60.dp,
                imageUrl = artistPrev[artistIndex].artistCover,
                shape = CircleShape,
                isCookie = isCookie,
                headerValue = headerValue,
                onClick = {
                    scope.launch {
                        onClick.invoke(
                            HomeUiEvent.ItemClick(
                                type = ItemsType.ARTIST,
                                name = artistPrev[artistIndex].name,
                                id = artistPrev[artistIndex].id,
                                isApiCall = true
                            )
                        )
                    }
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "More from")
                Text(
                    text = artistPrev[artistIndex].name,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        LazyRow(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
            contentPadding = PaddingValues(
                start = MaterialTheme.dimens.medium1,
                end = MaterialTheme.dimens.medium1
            )
        ) {
            items(artistPrev[artistIndex].lisOfPrevSong.size) { songIndex ->
                Box(
                    contentAlignment = Alignment.BottomCenter
                ) {
                    HomeScreenCard(
                        size = 120.dp,
                        imageUrl = artistPrev[artistIndex]
                            .lisOfPrevSong[songIndex].coverImage,
                        isCookie = isCookie,
                        headerValue = headerValue,
                        onClick = {
                            scope.launch {
                                onClick.invoke(
                                    HomeUiEvent.ItemClick(
                                        type = ItemsType.SONG,
                                        id = artistPrev[artistIndex]
                                            .lisOfPrevSong[songIndex].id
                                    )
                                )
                            }
                        }
                    )

                    Text(
                        modifier = Modifier
                            .width(120.dp)
                            .background(
                                color = MaterialTheme.colorScheme.background.copy(.8f),
                                shape = RoundedCornerShape(
                                    bottomEnd = MaterialTheme.dimens.small3,
                                    bottomStart = MaterialTheme.dimens.small3
                                )
                            ),
                        text = artistPrev[artistIndex]
                            .lisOfPrevSong[songIndex].title,
                        maxLines = 2,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
            item {
                HomeScreenCardMore {
                    scope.launch {
                        onClick.invoke(
                            HomeUiEvent.ItemClick(
                                type = ItemsType.ERR, // using err as Screens.AllFromArtist.route check viewmodel
                                name = artistPrev[artistIndex].name,
                                isApiCall = true // using isApiCall to load song first
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
    }
}


@Composable
fun CustomToast(
    modifier: Modifier = Modifier,
    message: String,
    color: Color = MaterialTheme.colorScheme.primary,
    fontWeight: FontWeight = FontWeight.Medium,
    fontSize: TextUnit = MaterialTheme.typography.titleMedium.fontSize
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message, modifier = Modifier.padding(MaterialTheme.dimens.small3),
            fontWeight = fontWeight,
            fontSize = fontSize,
            color = color
        )
    }
}


@Composable
fun ArtistMixCard(
    coverImage: String,
    label: String,
    isCookie: Boolean,
    headerValue: String,
    isSmallPhone: Boolean,
    onClick: (HomeUiEvent.ItemClick) -> Unit
) {
    Box(
        modifier = Modifier.wrapContentSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        HomeScreenCard(
            size = if (isSmallPhone) 120.dp else 130.dp,
            imageUrl = coverImage,
            isCookie = isCookie,
            headerValue = headerValue,
            onClick = {
                onClick.invoke(
                    HomeUiEvent.ItemClick(
                        type = ItemsType.ARTIST_MIX
                    )
                )
            }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        Text(
            text = label,
            maxLines = 2,
            modifier = Modifier
                .width(if (isSmallPhone) 120.dp else 130.dp)
                .background(color = MaterialTheme.colorScheme.background.copy(.8f)),
            overflow = TextOverflow.Ellipsis,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun HomeScreenCard(
    modifier: Modifier = Modifier,
    size: Dp,
    elevation: Dp = 10.dp,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    isCookie: Boolean,
    headerValue: String,
    shape: CornerBasedShape = MaterialTheme.shapes.small,
    imageUrl: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .size(size),
        shape = shape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        onClick = onClick
    ) {
        CustomImageView(
            isDarkThem = isDarkThem,
            url = imageUrl,
            isCookie = isCookie,
            headerValue = headerValue
        )
    }
}

@Composable
fun HomeScreenCardMore(
    text: String = "More",
    maxLine: Int = 1,
    size: Dp = 120.dp,
    fontWeight: FontWeight = FontWeight.Black,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(size),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                fontWeight = fontWeight,
                fontSize = MaterialTheme.typography.displaySmall.fontSize,
                maxLines = maxLine,
                letterSpacing = 2.sp,
                lineHeight = MaterialTheme.typography.displaySmall.fontSize,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun HomeScreenCardWithText(
    modifier: Modifier,
    name: String,
    imageUrl: String,
    elevation: Dp = 10.dp,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    isCookie: Boolean,
    headerValue: String,
    shape: CornerBasedShape = MaterialTheme.shapes.small,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            CustomImageView(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(1f / 3),
                isDarkThem = isDarkThem,
                url = imageUrl,
                isCookie = isCookie,
                headerValue = headerValue
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = name,
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                maxLines = 2,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun HomeScreenCardPlaylistPrev(
    modifier: Modifier,
    imageModifier: Modifier = Modifier,
    name: String,
    imageUrls: List<String>,
    elevation: CardElevation = CardDefaults.cardElevation(
        defaultElevation = 10.dp
    ),
    isDarkThem: Boolean = isSystemInDarkTheme(),
    isCookie: Boolean,
    headerValue: String,
    color: Color = MaterialTheme.colorScheme.background,
    shape: Shape = MaterialTheme.shapes.small,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        elevation = elevation,
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(1f / 3)
                    .then(imageModifier)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f / 2)
                ) {
                    CustomImageView(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(1f / 2),
                        isDarkThem = isDarkThem,
                        url = imageUrls[0],
                        isCookie = isCookie,
                        headerValue = headerValue
                    )

                    if (imageUrls.size >= 2)
                        CustomImageView(
                            modifier = Modifier
                                .fillMaxSize(),
                            isDarkThem = isDarkThem,
                            url = imageUrls[1],
                            isCookie = isCookie,
                            headerValue = headerValue
                        )
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (imageUrls.size >= 3)
                        CustomImageView(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(1f / 2),
                            isDarkThem = isDarkThem,
                            url = imageUrls[2],
                            isCookie = isCookie,
                            headerValue = headerValue
                        )
                    if (imageUrls.size >= 4)
                        CustomImageView(
                            modifier = Modifier
                                .fillMaxSize(),
                            isDarkThem = isDarkThem,
                            url = imageUrls[3],
                            isCookie = isCookie,
                            headerValue = headerValue
                        )
                }
            }

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

            Text(
                text = name,
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                maxLines = 2,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun CustomImageView(
    modifier: Modifier = Modifier,
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String,
    url: String,
    context: Context = LocalContext.current,
    contentScale: ContentScale = ContentScale.Crop
) {
//    BitmapConverter.decodeToBitmap(url).let {
//        if (it == null)
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(context)
            .data(url)
            .addHeader(
                name = if (isCookie) "Cookie" else "Authorization",
                value = headerValue
            )
            .fallback(
                drawableResId = if (isDarkThem) R.drawable.night_logo
                else R.drawable.light_logo
            )
            .error(
                drawableResId = if (isDarkThem) R.drawable.night_logo
                else R.drawable.light_logo
            )
            .crossfade(true)
            .build(),
        contentDescription = null,
        placeholder = painterResource(
            id = if (isDarkThem) R.drawable.night_logo
            else R.drawable.light_logo
        ),
        contentScale = contentScale
    )
//        else Image(
//            modifier = modifier,
//            bitmap = it,
//            contentDescription = null
//        )
//    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    TestThem {
        HomeScreenCardPlaylistPrev(
            modifier = Modifier
                .height(100.dp)
                .width(240.dp),
            imageUrls = listOf("", "", "", ""),
            name = "Your Favourite",
            isCookie = false,
            headerValue = ""
        ) {

        }
    }
}