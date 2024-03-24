package com.poulastaa.kyoku.presentation.screen.home_root.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.screens.home.HomeScreenBottomNavigation
import com.poulastaa.kyoku.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenBottomBar(
    scrollBehavior: BottomAppBarScrollBehavior,
    isHome: Boolean = true,
    onClick: (HomeScreenBottomNavigation) -> Unit
) {
    BottomAppBar(
        containerColor = Color.Transparent,
        contentPadding = PaddingValues(
            start = MaterialTheme.dimens.medium1,
            end = MaterialTheme.dimens.medium1
        ),
        scrollBehavior = scrollBehavior
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                modifier = Modifier.fillMaxWidth(1f / 2),
                onClick = {
                    onClick.invoke(HomeScreenBottomNavigation.HOME_SCREEN)
                }
            ) {
                Icon(
                    painter = painterResource(
                        id =
                        if (isHome) R.drawable.home else R.drawable.home_empty_2
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(
                modifier = Modifier.fillMaxWidth(1f),
                onClick = {
                    onClick.invoke(HomeScreenBottomNavigation.LIBRARY_SCREEN)
                }
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isHome) R.drawable.libary_empty
                        else R.drawable.libary
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}