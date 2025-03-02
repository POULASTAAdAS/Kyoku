package com.poulastaa.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.poulastaa.core.presentation.designsystem.ui.CalenderIcon
import com.poulastaa.core.presentation.designsystem.ui.EmailAlternateIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens


@Composable
@OptIn(ExperimentalLayoutApi::class)
internal fun OtherDetailsCard(email: String, bDate: String) {
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
    ) {
        ProfileOutlinedCard(Modifier) {
            Row(
                modifier = Modifier.padding(MaterialTheme.dimens.medium1),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = EmailAlternateIcon,
                    contentDescription = null
                )

                Spacer(Modifier.width(MaterialTheme.dimens.small3))

                Text(email)
            }
        }

        Spacer(Modifier.weight(1f))

        ProfileOutlinedCard(Modifier) {
            Row(
                modifier = Modifier.padding(MaterialTheme.dimens.medium1),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = CalenderIcon,
                    contentDescription = null
                )

                Spacer(Modifier.width(MaterialTheme.dimens.small3))

                Text(bDate)
            }
        }
    }
}