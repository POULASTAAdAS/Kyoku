package com.poulastaa.main.presentation.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemChanger
import com.poulastaa.core.presentation.designsystem.isElementVisible
import com.poulastaa.core.presentation.designsystem.noRippleClickable
import com.poulastaa.core.presentation.designsystem.ui.ShowMoreIcon

@Composable
fun HomeMoreButton(
    onClick: () -> Unit,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            if (ThemChanger.them) R.raw.lottie_more_light else R.raw.lottie_more_dark
        )
    )
    val animatable = rememberLottieAnimatable()
    var isMoreIconVisible by remember { mutableStateOf(false) }
    var showIcon by remember { mutableStateOf(false) }

    LaunchedEffect(isMoreIconVisible) {
        animatable.animate(
            composition = composition,
            iterations = 10,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        ).also {
            showIcon = true
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .isElementVisible {
                isMoreIconVisible = it
                showIcon = false
            }
            .noRippleClickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(targetState = showIcon) {
            when (it) {
                true -> Icon(
                    imageVector = ShowMoreIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(.5f),
                    tint = MaterialTheme.colorScheme.primary
                )

                false -> LottieAnimation(
                    modifier = Modifier
                        .fillMaxSize(.8f)
                        .rotate(-30f),
                    progress = { animatable.progress },
                    composition = composition,
                    alignment = Alignment.Center,
                    renderMode = RenderMode.SOFTWARE,
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}