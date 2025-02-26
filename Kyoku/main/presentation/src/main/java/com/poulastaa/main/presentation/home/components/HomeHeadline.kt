package com.poulastaa.main.presentation.home.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
internal fun HomeHeadline(@StringRes id: Int) {
    Column {
        Spacer(Modifier.height(MaterialTheme.dimens.medium2))

        Text(
            text = stringResource(id),
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
