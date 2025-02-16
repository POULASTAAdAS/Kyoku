package com.poulastaa.main.presentation.main

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
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
import com.poulastaa.core.presentation.designsystem.coloredShadow
import com.poulastaa.core.presentation.designsystem.noRippleClickable
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.designsystem.ui.gradiantBackground
import com.poulastaa.main.domain.model.isOpened
import com.poulastaa.main.presentation.home.HomeRootScreen
import com.poulastaa.main.presentation.home.HomeViewmodel
import com.poulastaa.main.presentation.home.components.MainTopBar
import com.poulastaa.main.presentation.library.LibraryRootScreen
import com.poulastaa.main.presentation.main.components.AppBottomBar
import com.poulastaa.main.presentation.main.components.AppDrawer
import com.poulastaa.main.presentation.main.components.AppNavigationRail
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

    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
            ) {
                AppNavigationRail(
                    state = state,
                    activity = activity,
                    haptic = haptic,
                    onAction = viewmodel::onAction
                )

                Box(Modifier.fillMaxSize()) {
                    Navigation(
                        isExpanded = true,
                        nav = nav,
                        isInitial = isInitial,
                        mainTopBarScroll = scroll,
                        onAction = viewmodel::onAction
                    )

                    MainTopBar(
                        modifier = Modifier.navigationBarsPadding(),
                        scroll = scroll,
                        user = state.user,
                        dayStatus = state.greetings,
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
            val configuration = LocalConfiguration.current
            val density = LocalDensity.current.density

            val calculatedScreenWidth = remember {
                derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
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
                mainTopBarScroll = scroll,
                onAction = viewmodel::onAction
            )

            AppBottomBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .align(Alignment.BottomCenter)
                    .padding(MaterialTheme.dimens.medium1)
                    .fillMaxWidth(
                        if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) 1f
                        else .7f
                    )
                    .padding(start = if (state.navigationDrawerState.isOpened()) MaterialTheme.dimens.medium1 else 0.dp)
                    .offset { IntOffset(animatedOffset.roundToPx(), 0) }
                    .scale(scale = animatedScale),
                screen = state.navigationBottomBarScreen,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (state.navigationDrawerState.isOpened()) viewmodel.onAction(MainUiAction.ToggleDrawer)
                    else viewmodel.onAction(MainUiAction.NavigateBottomBarScreen(it))
                }
            )

            MainTopBar(
                modifier = Modifier
                    .padding(start = if (state.navigationDrawerState.isOpened()) MaterialTheme.dimens.medium1 else 0.dp)
                    .offset { IntOffset(animatedOffset.roundToPx(), 0) }
                    .scale(scale = animatedScale),
                scroll = scroll,
                user = state.user,
                dayStatus = state.greetings,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Navigation(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    isInitial: Boolean,
    nav: NavHostController,
    mainTopBarScroll: TopAppBarScrollBehavior,
    onAction: (MainUiAction) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = nav,
        startDestination = ScreensCore.Library // todo change to Home
    ) {
        composable<ScreensCore.Home>(
            enterTransition = {
                if (isExpanded) fadeIn(tween(ANIMATION_TIME))
                else slideInHorizontally(
                    animationSpec = tween(ANIMATION_TIME),
                    initialOffsetX = { -it }
                ) + fadeIn(tween(ANIMATION_TIME))
            },
            exitTransition = {
                if (isExpanded) fadeOut(tween(ANIMATION_TIME))
                else slideOutHorizontally(
                    tween(ANIMATION_TIME),
                    targetOffsetX = { -it }
                ) + fadeOut(tween(ANIMATION_TIME))
            }
        ) {
            val homeViewmodel = hiltViewModel<HomeViewmodel>()

            LaunchedEffect(isInitial) {
                homeViewmodel.init(isInitial)
            }

            HomeRootScreen(
                viewmodel = homeViewmodel,
                scrollBehavior = mainTopBarScroll,
            )
        }

        composable<ScreensCore.Library>(
            enterTransition = {
                if (isExpanded) fadeIn(tween(ANIMATION_TIME))
                else slideInHorizontally(
                    tween(ANIMATION_TIME),
                    initialOffsetX = { it }
                ) + fadeIn(tween(ANIMATION_TIME))
            },
            exitTransition = {
                if (isExpanded) fadeOut(tween(ANIMATION_TIME))
                else slideOutHorizontally(
                    tween(ANIMATION_TIME),
                    targetOffsetX = { it }
                ) + fadeOut(tween(ANIMATION_TIME))
            }
        ) {
            LibraryRootScreen(scroll = mainTopBarScroll)
        }
    }
}
