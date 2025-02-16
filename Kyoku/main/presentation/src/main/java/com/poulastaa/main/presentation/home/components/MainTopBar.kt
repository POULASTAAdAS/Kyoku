package com.poulastaa.main.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.UiUser
import com.poulastaa.core.presentation.designsystem.noRippleClickable
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.SearchIcon
import com.poulastaa.core.presentation.designsystem.ui.UserIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppCacheImage

@OptIn(ExperimentalMaterial3Api::class)
internal val MAIN_TOP_BAR_PADDING = TopAppBarDefaults.TopAppBarExpandedHeight + 30.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainTopBar(
    scroll: TopAppBarScrollBehavior,
    user: UiUser,
    dayStatus: String,
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    TopAppBar(
        modifier = modifier.padding(start = MaterialTheme.dimens.small2),
        scrollBehavior = scroll,
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = MaterialTheme.dimens.medium1),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = dayStatus,
                    color = MaterialTheme.colorScheme.primary.copy(.8f),
                )

                Text(
                    text = user.username,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.offset {
                        IntOffset(x = 0, y = -10)
                    }
                )
            }
        },
        navigationIcon = {
            AppCacheImage(
                url = user.profilePic,
                errorIcon = UserIcon,
                iconColor = MaterialTheme.colorScheme.primary,
                borderColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .aspectRatio(1f)
                    .noRippleClickable {
                        onProfileClick()
                    }
            )
        },
        actions = {
            Card(
                modifier = Modifier
                    .padding(end =  MaterialTheme.dimens.small2)
                    .size(50.dp),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 0.dp
                ),
                onClick = {
                    onSearchClick()
                },
                interactionSource = null,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Icon(
                    imageVector = SearchIcon,
                    contentDescription = stringResource(R.string.search),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(9.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            MainTopBar(
                TopAppBarDefaults.enterAlwaysScrollBehavior(),
                UiUser(username = "Poulastaa"),
                "Good Morning",
                modifier = Modifier,
                {},
                {}
            )
        }
    }
}