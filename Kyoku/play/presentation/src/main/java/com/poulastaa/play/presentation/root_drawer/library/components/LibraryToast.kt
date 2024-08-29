package com.poulastaa.play.presentation.root_drawer.library.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CheckIcon
import com.poulastaa.core.presentation.designsystem.WarningIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.play.presentation.root_drawer.library.LibraryToastType
import com.poulastaa.play.presentation.root_drawer.library.LibraryUiToast
import kotlinx.coroutines.delay

@Composable
fun LibraryToast(
    data: LibraryUiToast,
    paddingValues: PaddingValues
) {
    AnimatedVisibility(
        visible = data.isVisible,
        enter = slideInVertically(initialOffsetY = { -it }, animationSpec = tween(600)) +
                fadeIn(animationSpec = tween(600)),
        exit = slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(600)) +
                fadeOut(animationSpec = tween(600))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = when (data.type) {
                        LibraryToastType.ERROR -> MaterialTheme.colorScheme.error
                        LibraryToastType.SUCCESS -> MaterialTheme.colorScheme.primary.copy(.8f)
                        LibraryToastType.DEFAULT -> MaterialTheme.colorScheme.secondaryContainer
                    }
                )
                .padding(top = paddingValues.calculateTopPadding())
                .padding(vertical = MaterialTheme.dimens.medium1),
            horizontalArrangement = Arrangement.Absolute.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (data.type) {
                LibraryToastType.ERROR -> {
                    Icon(
                        imageVector = WarningIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onError
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))
                }

                LibraryToastType.SUCCESS -> {
                    Icon(
                        imageVector = CheckIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                            .padding(MaterialTheme.dimens.small1)
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))
                }

                LibraryToastType.DEFAULT -> Unit
            }

            Text(
                text = data.message.asString(),
                fontWeight = FontWeight.SemiBold,
                color = when (data.type) {
                    LibraryToastType.ERROR -> MaterialTheme.colorScheme.onError
                    LibraryToastType.SUCCESS -> MaterialTheme.colorScheme.onSecondary
                    LibraryToastType.DEFAULT -> MaterialTheme.colorScheme.onBackground
                },
            )
        }

    }
}

@PreviewLightDark
@Composable
private fun Preview1() {
    var data by remember {
        mutableStateOf(
            LibraryUiToast(
                isVisible = true,
                message = UiText.DynamicString("Playlist is now pinned"),
                type = LibraryToastType.ERROR
            )
        )
    }

    LaunchedEffect(key1 = Unit) {
        (1..20).map {
            data = data.copy(isVisible = data.isVisible.not())
            delay(5000)
        }
    }

    AppThem {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LibraryToast(data = data, PaddingValues())
        }
    }
}