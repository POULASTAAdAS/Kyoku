package com.poulastaa.auth.presentation.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.auth.presentation.intro.components.AppAuthButton
import com.poulastaa.core.presentation.designsystem.AppLogo
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.GoogleIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.MovingCirclesWithMetaballEffect
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun IntroCompactScreen(
    state: IntroUiState,
    navigateToEmailLogIn: () -> Unit,
    onAction: (IntroUiAction) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MovingCirclesWithMetaballEffect()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Image(
                painter = AppLogo,
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(2f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.65f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 100.dp,
                            topEnd = 100.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier
                            .offset(
                                y = MaterialTheme.typography.headlineLarge.fontSize.value.toInt().dp
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 50.dp)
                                .padding(MaterialTheme.dimens.medium2)
                                .navigationBarsPadding(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.app_description),
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium1)
                            )

                            Spacer(Modifier.weight(1f))

                            AppAuthButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                image = GoogleIcon,
                                isLoading = state.isGoogleAuthLoading,
                                text = stringResource(R.string.google_sign_in),
                                onClick = {
                                    onAction(IntroUiAction.OnGoogleSignInClick)
                                }
                            )

                            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium3))

                            AppAuthButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                text = stringResource(R.string.email_sign_in),
                                onClick = navigateToEmailLogIn
                            )

                            Spacer(
                                modifier = Modifier
                                    .height(MaterialTheme.typography.headlineLarge.fontSize.value.toInt().dp)
                                    .navigationBarsPadding()
                            )
                        }
                    }

                    Card(
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                    ) {
                        Text(
                            text = stringResource(R.string.app_name),
                            modifier = Modifier
                                .padding(
                                    horizontal = MaterialTheme.dimens.medium2,
                                    vertical = MaterialTheme.dimens.small2
                                ),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = MaterialTheme.typography.displayMedium.fontSize,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            IntroCompactScreen(
                state = IntroUiState(),
                navigateToEmailLogIn = {},
                onAction = {}
            )
        }
    }
}