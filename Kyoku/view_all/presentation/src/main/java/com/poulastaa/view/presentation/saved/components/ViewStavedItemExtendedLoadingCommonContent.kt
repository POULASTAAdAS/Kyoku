package com.poulastaa.view.presentation.saved.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.shimmerEffect
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppBasicTopBar
import com.poulastaa.view.presentation.saved.ViewSavedUiItemType


@Composable
internal fun ViewStavedItemExtendedLoadingCommonContent(
    padding: PaddingValues,
    type: ViewSavedUiItemType,
    navigateBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
            )
            .fillMaxSize()
            .padding(padding)
            .padding(MaterialTheme.dimens.medium1)
            .navigationBarsPadding()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        AppBasicTopBar(
            title = "${stringResource(R.string.save)}d ${stringResource(type.value)}s",
            navigateBack = navigateBack,
        )

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(.5f)
                    .height(48.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                ),
                shape = CircleShape
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .shimmerEffect(MaterialTheme.colorScheme.primary)
                )
            }
        }

        content()
    }
}