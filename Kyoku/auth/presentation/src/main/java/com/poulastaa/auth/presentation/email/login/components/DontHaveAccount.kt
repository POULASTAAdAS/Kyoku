package com.poulastaa.auth.presentation.email.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun DontHaveAccount(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
    ) {
        Text(
            text = "${stringResource(id = R.string.dont_have_account)}  ",
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.small1))

        Text(
            text = stringResource(id = R.string.signUp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
        )
    }
}