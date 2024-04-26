package com.poulastaa.kyoku.presentation.screen.setup.birth_date

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.UiEvent
import com.poulastaa.kyoku.data.model.screens.setup.set_b_date.SetBirthDateUiEvent
import com.poulastaa.kyoku.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetBirthDateScreen(
    viewModel: SetBirthDateViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val datePicker = rememberDatePickerState()

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.padding(
                            start = MaterialTheme.dimens.small3
                        ),
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.onEvent(SetBirthDateUiEvent.OnBackClick)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = MaterialTheme.dimens.medium1,
                    end = MaterialTheme.dimens.medium1,
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Your Birth Date",
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            viewModel.onEvent(SetBirthDateUiEvent.OnDateSelectorClicked)
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ),
                value = viewModel.state.bDate,
                onValueChange = {},
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = Color.Transparent,
                    disabledTextColor = MaterialTheme.colorScheme.primary,
                    disabledLabelColor = MaterialTheme.colorScheme.primary,
                    disabledBorderColor = MaterialTheme.colorScheme.primary
                ),
                label = {
                    Text(text = "day/month/year")
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))

            FilledIconButton(
                onClick = {
                    viewModel.onEvent(SetBirthDateUiEvent.OnContinueClick)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = MaterialTheme.dimens.large2)
                    .shadow(
                        elevation = 3.dp,
                        shape = MaterialTheme.shapes.small,
                        clip = true
                    ),
                shape = MaterialTheme.shapes.small
            ) {
                if (viewModel.state.isLoading)
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                else
                    Text(
                        text = "C O N T I N U E",
                        fontWeight = FontWeight.Medium,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
            }
        }
    }


    if (viewModel.state.isDialogOpen) {
        DatePickerDialog(
            onDismissRequest = {
                viewModel.onEvent(SetBirthDateUiEvent.OnDateSelected(""))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(
                            SetBirthDateUiEvent.OnDateSelected(
                                date = if (datePicker.selectedDateMillis != null) datePicker.selectedDateMillis.toString() else ""
                            )
                        )
                    }
                ) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(SetBirthDateUiEvent.OnDateSelected(date = ""))
                    }
                ) {
                    Text(text = "CANCEL")
                }
            }
        ) {
            DatePicker(
                state = datePicker,
                title = {
                    Text(
                        text = "Select Your B'Date",
                        modifier = Modifier.padding(
                            start = MaterialTheme.dimens.medium1,
                            top = MaterialTheme.dimens.medium1
                        ),
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize
                    )
                }
            )
        }
    }
}