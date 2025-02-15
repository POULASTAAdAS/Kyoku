package com.poulastaa.main.presentation.main

import android.app.Activity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.poulastaa.core.domain.model.DtoScreens
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.designsystem.gradiantBackground
import com.poulastaa.main.domain.model.AppBottomScrren
import com.poulastaa.main.domain.model.isOpened
import com.poulastaa.main.presentation.home.HomeRootScreen
import com.poulastaa.main.presentation.home.HomeViewmodel
import com.poulastaa.main.presentation.main.components.AppDrawer
import com.poulastaa.main.presentation.main.components.AppNavigationRail
import com.poulastaa.main.presentation.library.LibraryRootScreen
import com.poulastaa.main.presentation.main.components.AppBottomBar
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainRootScreen(
    isInitial: Boolean,
    viewmodel: MainViewmodel = hiltViewModel(),
    navigate: (DtoScreens) -> Unit,
) {
    val activity = LocalContext.current as Activity
    val nav = rememberNavController()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val windowSizeClass = calculateWindowSizeClass(activity)
    val haptic = LocalHapticFeedback.current

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> {
            Row(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = gradiantBackground()
                        )
                    )
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets(0, 0, 0, 0))
                    .navigationBarsPadding()
            ) {
                AppNavigationRail(
                    state = state,
                    activity = activity,
                    haptic = haptic,
                    onAction = viewmodel::onAction
                )

                Navigation(
                    modifier = Modifier.fillMaxSize(),
                    nav = nav,
                    isInitial = isInitial,
                    state = state,
                    onAction = viewmodel::onAction
                )
            }
        }

        else -> Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val configuration = LocalConfiguration.current
            val density = LocalDensity.current.density

            val calculatedScreenWidth = remember {
                derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
            }
            val offsetValue by remember { derivedStateOf { (calculatedScreenWidth.value / 4.5).dp } }
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

            AppDrawer(
                isOpen = state.drawerState.isOpened(),
                user = state.user,
                navigate = viewmodel::onAction,
                onCloseClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewmodel.onAction(MainUiAction.ToggleDrawer)
                }
            )

            Navigation(
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
                    ),
                nav = nav,
                isInitial = isInitial,
                state = state,
                onAction = viewmodel::onAction
            )

            AppBottomBar(
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.BottomCenter),
                screen = AppBottomScrren.HOME
            ) { }
        }
    }
}

@Composable
private fun Navigation(
    modifier: Modifier,
    nav: NavHostController,
    isInitial: Boolean,
    state: MainUiState,
    onAction: (MainUiAction) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = nav,
        startDestination = ScreensCore.Home,
    ) {
        composable<ScreensCore.Home> {
            val homeViewmodel = hiltViewModel<HomeViewmodel>()

            LaunchedEffect(isInitial) {
                homeViewmodel.init(isInitial)
            }

            HomeRootScreen(
                viewmodel = homeViewmodel,
                toggleDrawer = {
                    onAction(MainUiAction.ToggleDrawer)
                }
            )
        }

        composable<ScreensCore.Library> {
            LibraryRootScreen(
                toggleDrawer = {
                    onAction(MainUiAction.ToggleDrawer)
                }
            )
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