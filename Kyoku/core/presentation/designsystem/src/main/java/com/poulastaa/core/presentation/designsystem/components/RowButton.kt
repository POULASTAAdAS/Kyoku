package com.poulastaa.core.presentation.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun RowButton(
    modifier: Modifier = Modifier,
    isMakingApiCall: Boolean,
    cancel: () -> Unit,
    save: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = cancel,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary.copy(.8f)
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(.6f)
            )
        ) {
            Text(
                text = stringResource(id = R.string.cancel),
                modifier = Modifier.padding(MaterialTheme.dimens.small2),
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        }

        Button(
            onClick = save,
            shape = MaterialTheme.shapes.medium
        ) {
            Box(modifier = Modifier) {
                Text(
                    text = stringResource(id = R.string.save),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(if (isMakingApiCall) 0f else 1f),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )

                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(if (isMakingApiCall) 1f else 0f)
                        .size(30.dp),
                    color = MaterialTheme.colorScheme.inversePrimary,
                    strokeWidth = 2.dp
                )
            }
        }
    }
}