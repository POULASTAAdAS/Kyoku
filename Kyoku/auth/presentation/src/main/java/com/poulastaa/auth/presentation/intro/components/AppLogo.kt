package com.poulastaa.auth.presentation.intro.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.poulastaa.core.presentation.ui.AppLogo
import com.poulastaa.core.presentation.ui.R

@Composable
internal fun AppLogo(modifier: Modifier) {
    Image(
        imageVector = AppLogo,
        contentDescription = stringResource(R.string.kyoku),
        modifier = modifier
    )
}
