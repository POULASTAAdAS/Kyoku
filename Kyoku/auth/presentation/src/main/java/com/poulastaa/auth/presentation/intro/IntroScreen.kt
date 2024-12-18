package com.poulastaa.auth.presentation.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.AppLogo
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.MovingCirclesWithMetaballEffect
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun IntroRootScreen(
    viewmodel: IntroViewmodel = hiltViewModel(),
) {
    IntroScreen(
        state = IntroUiState(),
        onAction = viewmodel::onAction
    )
}

@Composable
private fun IntroScreen(
    state: IntroUiState,
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
                    .fillMaxHeight(.7f),
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
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // TODO:
                            //  Add Description
                            //  Add Email Sign In Button
                            //  Add Google Sign In Button
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
            IntroScreen(
                state = IntroUiState(),
                onAction = {}
            )
        }
    }
}