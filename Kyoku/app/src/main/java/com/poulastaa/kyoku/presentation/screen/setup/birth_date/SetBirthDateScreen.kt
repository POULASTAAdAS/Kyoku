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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.setup.set_b_date.SetBirthDateUiEvent
import com.poulastaa.kyoku.presentation.screen.auth.common.CustomOkButton

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
                            start = 8.dp
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
                            imageVector = Icons.Rounded.ArrowBack,
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
                    start = 16.dp,
                    end = 16.dp,
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
                        interactionSource = MutableInteractionSource()
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

            Spacer(modifier = Modifier.height(20.dp))

            CustomOkButton(
                text = "Continue",
                modifier = Modifier.fillMaxWidth(),
                loading = viewModel.state.isLoading,
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    viewModel.onEvent(SetBirthDateUiEvent.OnContinueClick)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            )
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
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize
                    )
                }
            )
        }
    }
}