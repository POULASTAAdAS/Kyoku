package com.poulastaa.kyoku.presentation.screen.home_root

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeNavDrawerItemList
import com.poulastaa.kyoku.data.model.home_nav_drawer.NavDrawerUserInfo
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun HomeDrawerContent(
    navDrawerUserInfo: NavDrawerUserInfo,
    navigate: (HomeRootUiEvent) -> Unit
) {
    ModalDrawerSheet(
        windowInsets = WindowInsets.safeDrawing
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.medium1),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Column( // top
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f / 3)
            ) {
                TopPart(
                    navigate = navigate,
                    navDrawerUserInfo = navDrawerUserInfo
                )
            }

            Column(  // mid
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f / 2)
            ) {
                MidPart(
                    navigate = navigate
                )
            }

            Column( // end
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = MaterialTheme.dimens.large1
                    )
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.Bottom
            ) {
                EndPart(
                    navigate = navigate
                )
            }
        }
    }
}

@Composable
private fun TopPart(
    navigate: (HomeRootUiEvent) -> Unit,
    navDrawerUserInfo: NavDrawerUserInfo
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clip(CircleShape)
            .clickable(
                onClick = { navigate.invoke(HomeRootUiEvent.Navigate(Screens.Profile.route)) }
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(navDrawerUserInfo.imageUrl)
                .addHeader(
                    name = if (navDrawerUserInfo.isCookie) "Cookie" else "Authorization",
                    value = navDrawerUserInfo.headerValue
                )
                .fallback(
                    drawableResId = if (isSystemInDarkTheme()) R.drawable.night_logo
                    else R.drawable.light_logo
                )
                .error(
                    drawableResId = if (isSystemInDarkTheme()) R.drawable.night_logo
                    else R.drawable.light_logo
                )
                .crossfade(true)
                .build(),
            contentDescription = null,
            placeholder = painterResource(
                id = if (isSystemInDarkTheme()) R.drawable.night_logo
                else R.drawable.light_logo
            ),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .wrapContentSize()
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = navDrawerUserInfo.userName,
                fontWeight = FontWeight.ExtraBold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "View Profile",
                fontWeight = FontWeight.Light,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

        Spacer( // line
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.onPrimary)
        )
    }
}

@Composable
private fun MidPart(
    navigate: (HomeRootUiEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        items(HomeNavDrawerItemList.list) {
            NavigationDrawerItem(
                label = {
                    Text(
                        text = it.label,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                },
                selected = false,
                onClick = {
                    navigate.invoke(it.onClick)
                },
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = null
                    )
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
        }
    }
}

@Composable
private fun EndPart(
    navigate: (HomeRootUiEvent) -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = "L O G O U T",
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        },
        selected = false,
        onClick = {
            navigate.invoke(HomeRootUiEvent.LogOut)
        },
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ExitToApp,
                contentDescription = null
            )
        }
    )
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun Preview() {
    TestThem {
        HomeDrawerContent(
            navDrawerUserInfo = NavDrawerUserInfo(),
            navigate = { /*TODO*/ }
        )
    }
}