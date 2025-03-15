package com.poulastaa.view.presentation.others.compoenets

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.ArrowBackIcon
import com.poulastaa.core.presentation.designsystem.ui.ThreeDotIcon
import com.poulastaa.view.presentation.model.UiViewType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ViewOtherTopBar(
    modifier: Modifier = Modifier,
    scroll: TopAppBarScrollBehavior,
    title: String,
    type: UiViewType,
    isMenuOpened: Boolean = false,
    navigateBack: () -> Unit,
    onMenuClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        scrollBehavior = scroll,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = ArrowBackIcon,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(.9f)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = Color.Transparent,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            IconButton(
                onClick = onMenuClick
            ) {
                Icon(
                    imageVector = ThreeDotIcon,
                    contentDescription = null,
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ViewOtherTopBar(
                title = "That Cool Album",
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                type = UiViewType.ALBUM,
                navigateBack = { },
                onMenuClick = { }
            )
        }
    }
}