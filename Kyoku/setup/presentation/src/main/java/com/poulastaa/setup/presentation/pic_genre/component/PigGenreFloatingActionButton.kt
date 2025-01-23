package com.poulastaa.setup.presentation.pic_genre.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppLoadingButton
import com.poulastaa.setup.presentation.pic_genre.PicGenreUiAction
import com.poulastaa.setup.presentation.pic_genre.PicGenreUiState

@Composable
fun PigGenreFloatingActionButton(
    state: PicGenreUiState,
    onAction: (PicGenreUiAction) -> Unit,
) {
    AnimatedVisibility(
        visible = state.isMinLimitReached,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 400)
        ) + slideInHorizontally(animationSpec = tween(400), initialOffsetX = { it }),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 400)
        ) + slideOutHorizontally(animationSpec = tween(400), targetOffsetX = { it })
    ) {
        AppLoadingButton(
            modifier = Modifier.navigationBarsPadding(),
            text = stringResource(R.string.continue_text),
            isLoading = state.isMakingApiCall,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background
            ),
            loadingColor = MaterialTheme.colorScheme.background,
            onClick = {
                onAction(PicGenreUiAction.OnContinueClick)
            }
        )
    }
}