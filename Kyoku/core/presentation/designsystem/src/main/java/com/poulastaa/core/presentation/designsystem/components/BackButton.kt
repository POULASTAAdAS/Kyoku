package com.poulastaa.core.presentation.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.ArrowBackIcon
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.background
        )
    ) {
        Icon(
            modifier = Modifier.padding(MaterialTheme.dimens.small1),
            imageVector = ArrowBackIcon,
            contentDescription = null
        )
    }
}

@Preview
@Preview(
    uiMode = UI_MODE_NIGHT_YES,
)
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(MaterialTheme.dimens.medium1)
        ) {
            BackButton {

            }
        }
    }
}