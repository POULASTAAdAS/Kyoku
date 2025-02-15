package com.poulastaa.auth.presentation.intro

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.auth.presentation.intro.components.AppAuthButton
import com.poulastaa.core.presentation.designsystem.ui.AppLogo
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.GoogleIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.components.MovingCirclesWithMetaballEffect
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
fun IntroExpandedScreen(
    state: IntroUiState,
    navigateToEmailLogIn: () -> Unit,
    onAction: (IntroUiAction) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MovingCirclesWithMetaballEffect()

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.large1),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(.4f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier.aspectRatio(1f),
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = AppLogo,
                            contentDescription = null,
                            modifier = Modifier.aspectRatio(2.5f),
                        )

                        Text(
                            text = stringResource(R.string.app_description),
                            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium1)
                        )
                    }
                }
            }

            Spacer(Modifier.width(MaterialTheme.dimens.medium1))

            Column(
                modifier = Modifier
                    .fillMaxWidth(.9f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AppAuthButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    image = GoogleIcon,
                    isLoading = state.isGoogleAuthLoading,
                    text = stringResource(R.string.google_sign_in),
                    onClick = {
                        onAction(IntroUiAction.OnGoogleSignInClick)
                    }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))

                AppAuthButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    text = stringResource(R.string.email_sign_in),
                    onClick = navigateToEmailLogIn
                )
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 840,
    heightDp = 540
)
@Preview(
    widthDp = 840,
    heightDp = 540
)
@Composable
private fun Preview() {
    AppThem {
        Surface {
            IntroExpandedScreen(
                state = IntroUiState(),
                navigateToEmailLogIn = {},
                onAction = {}
            )
        }
    }
}