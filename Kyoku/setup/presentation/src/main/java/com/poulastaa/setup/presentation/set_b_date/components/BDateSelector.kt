package com.poulastaa.setup.presentation.set_b_date.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CalenderIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun BDateSelector(
    modifier: Modifier = Modifier,
    text: String,
    isError: Boolean,
    supportingText: String,
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = {},
        enabled = false,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            disabledLabelColor = if (isError) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.primary.copy(.7f),
            disabledBorderColor = if (isError) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.primary.copy(.7f),
            disabledTrailingIconColor = if (isError) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.primary.copy(.7f),
            disabledTextColor = if (isError) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onBackground,
            disabledPlaceholderColor = if (isError) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onBackground.copy(.5f),
            disabledSupportingTextColor = MaterialTheme.colorScheme.error,
        ),
        label = {
            Text(text = stringResource(id = R.string.select_b_date))
        },
        supportingText = {
            Text(text = supportingText)
        },
        trailingIcon = {
            Icon(
                imageVector = CalenderIcon,
                contentDescription = null
            )
        }
    )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .padding(MaterialTheme.dimens.small3)
        ) {
            BDateSelector(
                text = "10-10-2024",
                isError = false,
                supportingText = ""
            )
        }
    }
}