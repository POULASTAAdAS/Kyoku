package com.poulastaa.setup.presentation.set_b_date.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.setup.presentation.set_b_date.BDateUiEvent
import com.poulastaa.setup.presentation.set_b_date.BDateUiState

@Composable
fun BDateCompact(
    paddingValues: PaddingValues,
    state: BDateUiState,
    onEvent: (BDateUiEvent) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(paddingValues)
            .padding(horizontal = MaterialTheme.dimens.medium1)
            .then(
                Modifier.then(
                    if (state.isDateDialogOpen && !state.isMakingApiCall) Modifier.blur(
                        radius = 100.dp,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    ) else Modifier
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BDateSelector(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        onEvent(BDateUiEvent.OnBDateDialogToggle)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = null
                ),
            text = state.bDate.data,
            isError = state.bDate.isErr,
            supportingText = state.bDate.errText.asString()
        )

        Spacer(modifier = Modifier.heightIn(MaterialTheme.dimens.large1))

        SubmitButton(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .align(Alignment.CenterHorizontally),
            isLoading = state.isMakingApiCall,
            enabled = state.canMakeReq,
            onClick = {
                onEvent(BDateUiEvent.OnContinueClick)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        )

        Spacer(modifier = Modifier.heightIn(MaterialTheme.dimens.large2))
        Spacer(modifier = Modifier.heightIn(MaterialTheme.dimens.large2))
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    var show by remember {
        mutableStateOf(false)
    }
    val parentSize = remember {
        mutableStateOf(Size.Zero)
    }

    AppThem {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    parentSize.value = it.size.toSize()
                },
            contentAlignment = Alignment.Center
        ) {
            BDateCompact(
                paddingValues = PaddingValues(),
                state = BDateUiState()
            ) {
                if (it == BDateUiEvent.OnBDateDialogToggle) show = !show
            }


            if (show) {
                BDatePicker(
                    isExpanded = false,
                    onDateSelected = {

                    },
                    dismissReq = {

                    }
                )
            }
        }
    }
}