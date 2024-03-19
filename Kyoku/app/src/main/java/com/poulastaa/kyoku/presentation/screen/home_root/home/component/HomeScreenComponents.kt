package com.poulastaa.kyoku.presentation.screen.home_root.home.component

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens

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
fun HomeScreenCard(
    modifier: Modifier = Modifier,
    size: Dp,
    elevation: Dp = 10.dp,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    shape: CornerBasedShape = MaterialTheme.shapes.small,
    imageUrl: String,
    authHeader: String,
    isCookie: Boolean,
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
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .addHeader(
                    name = if (isCookie) "Cookie" else "Authorization",
                    value = authHeader
                ).fallback(
                    drawableResId = if (isDarkThem) R.drawable.night_logo
                    else R.drawable.light_logo
                )
                .error(
                    drawableResId = if (isDarkThem) R.drawable.night_logo
                    else R.drawable.light_logo
                )
                .crossfade(true)
                .build(),
            placeholder = painterResource(
                id = if (isDarkThem) R.drawable.night_logo
                else R.drawable.light_logo
            ),
            contentScale = ContentScale.Fit,
            contentDescription = null
        )
    }
}

@Composable
fun HomeScreenCardMore(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(120.dp),
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
                text = "More",
                fontWeight = FontWeight.Black,
                fontSize = MaterialTheme.typography.displaySmall.fontSize,
                maxLines = 1,
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
    authHeader: String,
    isCookie: Boolean,
    elevation: Dp = 10.dp,
    isDarkThem: Boolean = isSystemInDarkTheme(),
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
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .addHeader(
                        name = if (isCookie) "Cookie" else "Authorization",
                        value = authHeader
                    ).fallback(
                        drawableResId = if (isDarkThem) R.drawable.night_logo
                        else R.drawable.light_logo
                    )
                    .error(
                        drawableResId = if (isDarkThem) R.drawable.night_logo
                        else R.drawable.light_logo
                    )
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(
                    id = if (isDarkThem) R.drawable.night_logo
                    else R.drawable.light_logo
                ),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

            Text(
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
    name: String,
    imageUrls: List<String>,
    authHeader: String,
    isCookie: Boolean,
    elevation: Dp = 10.dp,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    shape: CornerBasedShape = MaterialTheme.shapes.small,
    context: Context = LocalContext.current,
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
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(1f / 3)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f / 2)
                ) {
                    PlaylistImage(
                        context = context,
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(1f / 2),
                        isDarkThem = isDarkThem,
                        url = imageUrls[0],
                        authHeader = authHeader,
                        isCookie = isCookie
                    )

                    if (imageUrls.size >= 2)
                        PlaylistImage(
                            context = context,
                            modifier = Modifier
                                .fillMaxSize(),
                            isDarkThem = isDarkThem,
                            url = imageUrls[1],
                            authHeader = authHeader,
                            isCookie = isCookie
                        )
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (imageUrls.size >= 3)
                        PlaylistImage(
                            context = context,
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(1f / 2),
                            isDarkThem = isDarkThem,
                            url = imageUrls[2],
                            authHeader = authHeader,
                            isCookie = isCookie
                        )
                    if (imageUrls.size >= 4)
                        PlaylistImage(
                            context = context,
                            modifier = Modifier
                                .fillMaxSize(),
                            isDarkThem = isDarkThem,
                            url = imageUrls[3],
                            authHeader = authHeader,
                            isCookie = isCookie
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
private fun PlaylistImage(
    context: Context,
    modifier: Modifier,
    isDarkThem: Boolean,
    url: String,
    authHeader: String,
    isCookie: Boolean
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(context)
            .data(url)
            .addHeader(
                name = if (isCookie) "Cookie" else "Authorization",
                value = authHeader
            ).fallback(
                drawableResId = if (isDarkThem) R.drawable.night_logo
                else R.drawable.light_logo
            )
            .error(
                drawableResId = if (isDarkThem) R.drawable.night_logo
                else R.drawable.light_logo
            )
            .crossfade(true)
            .build(),
        placeholder = painterResource(
            id = if (isDarkThem) R.drawable.night_logo
            else R.drawable.light_logo
        ),
        contentScale = ContentScale.FillBounds,
        contentDescription = null
    )

}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    TestThem {
//        HomeScreenCardWithText(
//            modifier = Modifier
//                .height(100.dp)
//                .width(240.dp),
//            imageUrl = "",
//            isCookie = false,
//            authHeader = "",
//            name = "Your Favourite"
//        ) {
//
//        }

        HomeScreenCardPlaylistPrev(
            modifier = Modifier
                .height(100.dp)
                .width(240.dp),
            imageUrls = listOf("", "", "", ""),
            isCookie = false,
            authHeader = "",
            name = "Your Favourite"
        ) {

        }
    }
}