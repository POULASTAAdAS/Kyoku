package com.poulastaa.core.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
fun AppErrorScreen(
    modifier: Modifier = Modifier,
    error: LoadingType.Error,
    navigateBack: () -> Unit
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(error.lottieId)
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = CloseIcon,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(.8f)
                )
            }
        }

        LottieAnimation(
            modifier = Modifier.fillMaxSize(.4f),
            composition = composition,
            iterations = 10,
            alignment = Alignment.Center,
            renderMode = RenderMode.SOFTWARE,
            contentScale = ContentScale.Crop,
        )

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        Text(
            text = when (error.type) {
                LoadingType.ERROR_TYPE.NO_INTERNET -> stringResource(R.string.error_no_internet)
                LoadingType.ERROR_TYPE.UNKNOWN -> stringResource(R.string.error_something_went_wrong)
            },
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AppErrorScreen(
                Modifier
                    .fillMaxSize()
                    .background(brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground()))
                    .padding(MaterialTheme.dimens.medium1),
                if (isSystemInDarkTheme()) LoadingType.Error(
                    LoadingType.ERROR_TYPE.NO_INTERNET,
                    R.raw.lottie_error_1
                ) else LoadingType.Error(
                    LoadingType.ERROR_TYPE.UNKNOWN,
                    R.raw.lottie_error_4
                )
            ){}
        }
    }
}