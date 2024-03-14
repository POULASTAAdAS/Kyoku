package com.poulastaa.kyoku.presentation.screen.home_root.home.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    title: String,
    profileUrl: String,
    isCookie: Boolean,
    authHeader: String,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(profileUrl)
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
                modifier = Modifier
                    .padding(MaterialTheme.dimens.small1)
                    .size(48.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                    )
                    .clickable(
                        onClick = onProfileClick,
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ),
                contentDescription = null
            )
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun Preview() {
    TestThem {
        HomeTopAppBar(
            title = "Good Morning",
            profileUrl = "",
            isCookie = false,
            authHeader = "",
            onProfileClick = { /*TODO*/ }
        ) {

        }
    }
}