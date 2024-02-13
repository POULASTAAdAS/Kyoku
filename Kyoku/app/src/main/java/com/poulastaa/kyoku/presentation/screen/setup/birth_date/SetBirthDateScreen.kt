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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.poulastaa.kyoku.data.model.auth.UiEvent
import com.poulastaa.kyoku.data.model.setup.set_birth_date.SetBirthDateUiEvent
import com.poulastaa.kyoku.presentation.screen.auth.common.CustomOkButton

@Composable
fun SetBirthDateScreen(
    viewModel: SetBirthDateViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {}
                is UiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select Your Birth Date",
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimary
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
            loading = false,
            shape = RoundedCornerShape(8.dp),
            onClick = {
                viewModel.onEvent(SetBirthDateUiEvent.OnContinueClick)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        )
    }
}