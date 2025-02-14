package com.poulastaa.main.presentation.main

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.poulastaa.core.domain.model.DtoScreens
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.designsystem.gradiantBackground
import com.poulastaa.main.domain.model.isOpened
import com.poulastaa.main.presentation.main.components.AppDrawer
import kotlin.math.roundToInt

@Composable
fun MainRootScreen(
    isInitial: Boolean,
    viewmodel: MainViewmodel = hiltViewModel(),
    navigate: (DtoScreens) -> Unit,
) {
    val state by viewmodel.state.collectAsStateWithLifecycle()

    val nav = rememberNavController()

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density

    val screenWidth = remember {
        derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
    }
    val offsetValue by remember { derivedStateOf { (screenWidth.value / 4.5).dp } }
    val animatedOffset by animateDpAsState(
        targetValue = if (state.drawerState.isOpened()) offsetValue else 0.dp,
        label = "Animated Offset"
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (state.drawerState.isOpened())
            if (configuration.screenWidthDp > 480) 0.7f
            else 0.8f
        else 1f,
        label = "Animated Scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppDrawer(
            isOpen = state.drawerState.isOpened(),
            user = state.user,
            navigate = viewmodel::onAction,
            onCloseClick = {
                viewmodel.onAction(MainUiAction.ToggleDrawer)
            }
        )

        NavHost(
            navController = nav,
            startDestination = ScreensCore.Home,
            modifier = Modifier
                .padding(start = if (state.drawerState.isOpened()) MaterialTheme.dimens.medium1 else 0.dp)
                .offset { IntOffset(animatedOffset.roundToPx(), 0) }
                .scale(scale = animatedScale)
                .clip(if (state.drawerState.isOpened()) MaterialTheme.shapes.extraLarge else RectangleShape)
                .coloredShadow(
                    color = Color.Black,
                    alpha = 0.5f,
                )
                .clickable(
                    enabled = state.drawerState.isOpened(),
                    interactionSource = null,
                    indication = null,
                    onClick = {
                        viewmodel.onAction(MainUiAction.ToggleDrawer)
                    }
                )
        ) {
            composable<ScreensCore.Home> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = gradiantBackground()
                            )
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Home",
                        fontWeight = FontWeight.Black,
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            viewmodel.onAction(MainUiAction.ToggleDrawer)
                        }
                    )
                }
            }

            composable<ScreensCore.Library> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = gradiantBackground()
                            )
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Library",
                        fontWeight = FontWeight.Black,
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

private fun Modifier.coloredShadow(
    color: Color,
    alpha: Float = 0.2f,
    borderRadius: Dp = 0.dp,
    shadowRadius: Dp = 20.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
) = composed {
    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparent = color.copy(alpha = 0f).toArgb()
    this.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = transparent
            frameworkPaint.setShadowLayer(
                shadowRadius.toPx(),
                offsetX.toPx(),
                offsetY.toPx(),
                shadowColor
            )
            it.drawRoundRect(
                0f,
                0f,
                this.size.width,
                this.size.height,
                borderRadius.toPx(),
                borderRadius.toPx(),
                paint
            )
        }
    }
}