package com.poulastaa.auth.presentation.intro.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.auth.presentation.intro.IntroUiEvent
import com.poulastaa.auth.presentation.intro.IntroUiState
import com.poulastaa.core.presentation.designsystem.AppLogo
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.EmailIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.IntroAuthButton
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun ColumnScope.IntroCompactScreen(
    state: IntroUiState,
    onEvent: (IntroUiEvent) -> Unit,
) {
    Spacer(modifier = Modifier.weight(1f))

    Image(
        painter = AppLogo,
        contentDescription = null,
        modifier = Modifier.aspectRatio(1f)
    )

    Text(
        text = stringResource(id = R.string.intro_text),
        fontWeight = FontWeight.SemiBold,
        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
        textAlign = TextAlign.Center,
        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background
        else MaterialTheme.colorScheme.onBackground
    )


    Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
    Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))

    IntroAuthButton(
        modifier = Modifier
            .fillMaxWidth(),
        isLoading = state.isGoogleLogging,
        text = stringResource(id = R.string.continue_with_google),
        icon = {
            Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = null
            )
        },
        onClick = {
            onEvent(IntroUiEvent.OnGoogleLogInClick)
        }
    )

    Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

    IntroAuthButton(
        modifier = Modifier
            .fillMaxWidth(),
        isLoading = false,
        text = stringResource(id = R.string.continue_with_email),
        icon = {
            Icon(
                imageVector = EmailIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(MaterialTheme.dimens.small3)
            )
        },
        onClick = {
            onEvent(IntroUiEvent.OnGoogleLogInClick)
        }
    )

    Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.onTertiary,
                            MaterialTheme.colorScheme.tertiary,
                        )
                    )
                )
                .padding(MaterialTheme.dimens.medium1),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IntroCompactScreen(state = IntroUiState()) {

            }
        }
    }
}