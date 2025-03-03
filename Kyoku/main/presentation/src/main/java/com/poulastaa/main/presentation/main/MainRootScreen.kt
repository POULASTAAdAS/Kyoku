package com.poulastaa.main.presentation.main

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.poulastaa.core.domain.model.DtoCoreScreens
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.coloredShadow
import com.poulastaa.core.presentation.designsystem.noRippleClickable
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.main.domain.model.isOpened
import com.poulastaa.main.presentation.home.HomeRootScreen
import com.poulastaa.main.presentation.home.HomeViewmodel
import com.poulastaa.main.presentation.library.LibraryRootScreen
import com.poulastaa.main.presentation.main.components.AppBottomBar
import com.poulastaa.main.presentation.main.components.AppDrawer
import com.poulastaa.main.presentation.main.components.AppNavigationRail
import com.poulastaa.main.presentation.main.components.AppTopBar
import kotlinx.coroutines.flow.first
import kotlin.math.roundToInt

private const val ANIMATION_TIME = 600

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainRootScreen(
    isInitial: Boolean,
    viewmodel: MainViewmodel = hiltViewModel(),
    navigate: (DtoCoreScreens) -> Unit,
) {
    val activity = LocalContext.current as Activity
    val nav = rememberNavController()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val windowSizeClass = calculateWindowSizeClass(activity)
    val haptic = LocalHapticFeedback.current

    val topBarScroll = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var isBottomNavigationVisible by remember { mutableStateOf(false) }

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val triple = ThemModeChanger.compute(
        density,
        configuration,
        state.offset,
        state.themChangeAnimationTime,
        resetAnimation = { viewmodel.onAction(MainUiAction.ResetRevelAnimation) }
    )

    LaunchedEffect(nav.currentBackStackEntryFlow) {
        nav.currentBackStackEntryFlow.first().destination.route?.let {
            viewmodel.updateMainScreenNavigationUiAfterNavigation(it)
        }
    }

    ObserveAsEvent(viewmodel.uiEvent) { event ->
        when (event) {
            is MainUiEvent.EmitToast -> Toast.makeText(
                activity,
                event.message.asString(activity),
                Toast.LENGTH_LONG
            ).show()

            is MainUiEvent.Navigate -> navigate(event.screen)

            is MainUiEvent.NavigateMain -> {
                nav.popBackStack()
                nav.navigate(event.screen)
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Expanded -> {
                Row(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = ThemModeChanger.getGradiantBackground()
                            )
                        )
                        .fillMaxSize(),
                ) {
                    AppNavigationRail(
                        state = state,
                        haptic = haptic,
                        onAction = viewmodel::onAction
                    )

                    Box(Modifier.fillMaxSize()) {
                        Navigation(
                            nav = nav,
                            isInitial = isInitial,
                            topBarScroll = topBarScroll,
                            onAction = viewmodel::onAction
                        )

                        AppTopBar(
                            modifier = Modifier.navigationBarsPadding(),
                            scroll = topBarScroll,
                            user = state.user,
                            dayStatus = state.greetings,
                            screen = state.navigationBottomBarScreen,
                            onSearchClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                            onProfileClick = {}
                        )
                    }
                }
            }

            else -> Box(
                modifier = Modifier.fillMaxSize()
            ) {
                val calculatedScreenWidth = remember {
                    derivedStateOf { (configuration.screenWidthDp * density.density).roundToInt() }
                }
                val offsetValue by remember { derivedStateOf { (calculatedScreenWidth.value / 4.5).dp } }
                val animatedOffset by animateDpAsState(
                    targetValue = if (state.navigationDrawerState.isOpened()) offsetValue else 0.dp,
                    label = "Animated Offset"
                )

                val animatedScale by animateFloatAsState(
                    targetValue = if (state.navigationDrawerState.isOpened())
                        if (configuration.screenWidthDp > 480) 0.7f
                        else 0.8f
                    else 1f,
                    label = "Animated Scale"
                )

                AppDrawer(
                    isOpen = state.navigationDrawerState.isOpened(),
                    user = state.user,
                    navigate = viewmodel::onAction,
                    onCloseClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewmodel.onAction(MainUiAction.ToggleDrawer)
                    }
                )

                Navigation(
                    modifier = Modifier
                        .padding(start = if (state.navigationDrawerState.isOpened()) MaterialTheme.dimens.medium1 else 0.dp)
                        .offset { IntOffset(animatedOffset.roundToPx(), 0) }
                        .scale(scale = animatedScale)
                        .clip(if (state.navigationDrawerState.isOpened()) MaterialTheme.shapes.extraLarge else RectangleShape)
                        .coloredShadow(
                            color = Color.Black,
                            alpha = 0.5f,
                        )
                        .noRippleClickable(state.navigationDrawerState.isOpened()) {
                            viewmodel.onAction(MainUiAction.ToggleDrawer)
                        },
                    nav = nav,
                    isInitial = isInitial,
                    topBarScroll = topBarScroll,
                    onAction = viewmodel::onAction
                )

                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    visible = isBottomNavigationVisible,
                    enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { it },
                    exit = fadeOut(tween(400)) + slideOutVertically(tween(400)) { it }
                ) {
                    AppBottomBar(
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.medium1)
                            .fillMaxWidth(
                                if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) 1f
                                else .7f
                            )
                            .padding(start = if (state.navigationDrawerState.isOpened()) MaterialTheme.dimens.medium1 else 0.dp)
                            .offset { IntOffset(animatedOffset.roundToPx(), 0) }
                            .scale(scale = animatedScale)
                            .coloredShadow(
                                Color.Black,
                                alpha = .3f,
                            ),
                        screen = state.navigationBottomBarScreen,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (state.navigationDrawerState.isOpened()) viewmodel.onAction(
                                MainUiAction.ToggleDrawer
                            )
                            else viewmodel.onAction(MainUiAction.NavigateBottomBarScreen(it))
                        }
                    )
                }

                AppTopBar(
                    modifier = Modifier
                        .padding(start = if (state.navigationDrawerState.isOpened()) MaterialTheme.dimens.medium1 else 0.dp)
                        .offset { IntOffset(animatedOffset.roundToPx(), 0) }
                        .scale(scale = animatedScale)
                        .onGloballyPositioned {
                            isBottomNavigationVisible = topBarScroll.state.collapsedFraction == 0f
                        },
                    scroll = topBarScroll,
                    user = state.user,
                    dayStatus = state.greetings,
                    screen = state.navigationBottomBarScreen,
                    onSearchClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                    onProfileClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewmodel.onAction(MainUiAction.ToggleDrawer)
                    }
                )
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            if (triple.revealSize.value > 0) {
                drawCircle(
                    color = triple.backgroundColor,
                    radius = triple.revealSize.value,
                    center = triple.center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Navigation(
    modifier: Modifier = Modifier,
    isInitial: Boolean,
    nav: NavHostController,
    topBarScroll: TopAppBarScrollBehavior,
    onAction: (MainUiAction) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = nav,
        startDestination = ScreensCore.Home
    ) {
        composable<ScreensCore.Home>(
            enterTransition = {
                fadeIn(tween(ANIMATION_TIME))
            },
            exitTransition = {
                fadeOut(tween(ANIMATION_TIME))
            }
        ) {
            val homeViewmodel = hiltViewModel<HomeViewmodel>()

            LaunchedEffect(isInitial) {
                homeViewmodel.init(isInitial)
            }

            HomeRootScreen(
                viewmodel = homeViewmodel,
                topBarScroll = topBarScroll,
            )
        }

        composable<ScreensCore.Library>(
            enterTransition = {
                fadeIn(tween(ANIMATION_TIME))
            },
            exitTransition = {
                fadeOut(tween(ANIMATION_TIME))
            }
        ) {
            LibraryRootScreen(topBarScroll)
        }
    }
}
