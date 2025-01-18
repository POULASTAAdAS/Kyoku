package com.poulastaa.setup.presentation.set_bdate

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.designsystem.CancelIcon
import com.poulastaa.core.presentation.designsystem.CheckIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.KyokuWindowSize
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.setup.presentation.set_bdate.components.SetBDateTopBar

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SetBDateRootScreen(
    viewmodel: SetBDateViewmodel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToPicGenre: () -> Unit,
) {
    val activity = LocalContext.current as Activity
    val windowSize = calculateWindowSizeClass(activity)
    val state by viewmodel.state.collectAsStateWithLifecycle()

    val haptic = LocalHapticFeedback.current

    val datePicker = rememberDatePickerState()


    ObserveAsEvent(viewmodel.uiEvent) {
        when (it) {
            is SetBDateUiEvent.EmitToast -> Toast.makeText(
                activity,
                it.message.asString(activity),
                Toast.LENGTH_LONG
            ).show()

            SetBDateUiEvent.NavigateBack -> navigateBack()
            SetBDateUiEvent.OnSuccess -> navigateToPicGenre()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        KyokuWindowSize(
            windowSizeClass = windowSize,
            compactContent = {
                SetBDateCompactScreen(
                    state = state,
                    onAction = viewmodel::onAction
                )
            },
            mediumContent = {
                SetBDateMediumScreen(
                    state = state,
                    onAction = viewmodel::onAction
                )
            },
            expandedContent = {
                SetBDateExpandedScreen(
                    state = state,
                    onAction = viewmodel::onAction
                )
            }
        )

        SetBDateTopBar {
            viewmodel.onAction(SetBDateUiAction.OnBackClick)
        }

        if (state.isDialogOpn && !state.isMakingApiCall) {
            DatePickerDialog(
                shape = MaterialTheme.shapes.small,
                onDismissRequest = {
                    viewmodel.onAction(SetBDateUiAction.OnDateDialogToggle)
                },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.dimens.medium1)
                    ) {
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            onClick = {
                                viewmodel.onAction(SetBDateUiAction.OnDateDialogToggle)
                            },
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 0.dp
                            )
                        ) {
                            Icon(
                                imageVector = CancelIcon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(4.dp)
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            onClick = {
                                datePicker.selectedDateMillis?.let {
                                    viewmodel.onAction(SetBDateUiAction.OnDateChange(it))
                                }

                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 0.dp
                            )
                        ) {
                            Icon(
                                imageVector = CheckIcon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                },
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                content = {
                    DatePicker(
                        state = datePicker,
                        title = {
                            Text(
                                text = stringResource(R.string.bdate_title),
                                modifier = Modifier.padding(MaterialTheme.dimens.medium1)
                            )
                        },
                        colors = DatePickerDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                            headlineContentColor = MaterialTheme.colorScheme.primary,
                            weekdayContentColor = MaterialTheme.colorScheme.primary,
                            navigationContentColor = MaterialTheme.colorScheme.primary,

                            yearContentColor = MaterialTheme.colorScheme.primary,
                            currentYearContentColor = MaterialTheme.colorScheme.primary,
                            selectedYearContentColor = MaterialTheme.colorScheme.background,
                            selectedYearContainerColor = MaterialTheme.colorScheme.primary,

                            dayContentColor = MaterialTheme.colorScheme.primary,
                            selectedDayContentColor = MaterialTheme.colorScheme.background,
                            selectedDayContainerColor = MaterialTheme.colorScheme.primary,

                            dividerColor = MaterialTheme.colorScheme.secondary,
                        )
                    )
                }
            )
        }
    }
}