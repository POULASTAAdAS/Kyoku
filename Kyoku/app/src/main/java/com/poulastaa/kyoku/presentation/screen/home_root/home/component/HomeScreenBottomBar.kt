package com.poulastaa.kyoku.presentation.screen.home_root.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeScreenBottomNavigation
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun HomeScreenBottomBar(
    isHome: Boolean,
    onClick: (HomeScreenBottomNavigation) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(
                start = MaterialTheme.dimens.medium1,
                end = MaterialTheme.dimens.medium1,
                bottom = MaterialTheme.dimens.small1
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            modifier = Modifier.fillMaxWidth(1f / 2),
            onClick = {
                onClick.invoke(HomeScreenBottomNavigation.HOME_SCREEN)
            },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = if (isHome) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                painter = painterResource(
                    id =
                    if (isHome) R.drawable.home else R.drawable.home_empty_2
                ),
                contentDescription = null
            )
        }

        IconButton(
            modifier = Modifier.fillMaxWidth(1f),
            onClick = {
                onClick.invoke(HomeScreenBottomNavigation.LIBRARY_SCREEN)
            },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = if (isHome) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                painter = painterResource(
                    id = if (isHome) R.drawable.libary_empty
                    else R.drawable.libary
                ),
                contentDescription = null
            )
        }
    }
}
