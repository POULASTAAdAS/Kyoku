package com.poulastaa.setup.presentation.set_b_date.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BDatePicker(
    isExpanded: Boolean,
    onDateSelected: (Long?) -> Unit,
    dismissReq: () -> Unit,
) {
    val datePicker = rememberDatePickerState()

    Card(
        modifier = Modifier
            .padding(MaterialTheme.dimens.medium3)
            .then(
                if (isExpanded) Modifier.padding(horizontal = MaterialTheme.dimens.large2 + MaterialTheme.dimens.large2) else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = MaterialTheme.shapes.large
    ) {
        DatePicker(
            state = datePicker,
            headline = {
                Text(
                    text = stringResource(id = R.string.select_b_date),
                    modifier = Modifier.padding(start = MaterialTheme.dimens.medium1)
                )
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.medium2)
                .padding(bottom = MaterialTheme.dimens.small3),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = dismissReq,
                modifier = Modifier.padding(
                    end = MaterialTheme.dimens.medium1,
                    bottom = MaterialTheme.dimens.small1
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }

            Button(
                onClick = {
                    val today = System.currentTimeMillis()

                    datePicker.selectedDateMillis?.let {
                        if (it < today) onDateSelected(datePicker.selectedDateMillis)
                    }
                },
                modifier = Modifier.padding(
                    end = MaterialTheme.dimens.medium1,
                    bottom = MaterialTheme.dimens.small1
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}

@Preview(
    uiMode = UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BDatePicker(isExpanded = false,
                onDateSelected = {}) {

            }
        }
    }
}