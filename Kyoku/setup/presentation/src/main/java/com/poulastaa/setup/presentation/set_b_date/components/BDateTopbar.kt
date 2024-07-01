package com.poulastaa.setup.presentation.set_b_date.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.poulastaa.core.presentation.designsystem.components.AppBackButton
import com.poulastaa.core.presentation.designsystem.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BDateTopBar(
    isExpanded: Boolean,
    text: String,
    navigateBack: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.padding(start = MaterialTheme.dimens.medium1)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = text,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = if (isExpanded) TextAlign.Center else TextAlign.Start
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            AppBackButton {
                navigateBack()
            }
        }
    )
}