package com.poulastaa.settings.presentation

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.domain.model.ThemColor
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemChanger
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.ui.ArrowBackIcon
import com.poulastaa.core.presentation.designsystem.ui.CheckIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.settings.domain.model.SettingsAllowedNavigationScreens
import com.poulastaa.settings.presentation.components.SettingsBottomSheet

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsRootScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    onLogOut: () -> Unit,
    navigate: (screen: SettingsAllowedNavigationScreens) -> Unit,
) {
    val context = LocalContext.current as Activity
    val windowSize = calculateWindowSizeClass(context)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val bottomSheet = rememberModalBottomSheetState(confirmValueChange = { !state.isLoading })

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val triple = ThemModeChanger.compute(
        density,
        configuration,
        state.offset,
        state.themChangeAnimationTime,
        resetAnimation = { viewModel.onAction(SettingsUiAction.ResetRevelAnimation) }
    )

    ObserveAsEvent(viewModel.uiEvent) { event ->
        when (event) {
            is SettingsUiEvent.OnLogOutSuccess -> onLogOut()
            is SettingsUiEvent.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is SettingsUiEvent.Navigate -> navigate(event.screen)
        }
    }

    LaunchedEffect(
        state.isLogoutBottomSheetVisible,
        state.isDeleteAccountVBottomSheetVisible
    ) {
        if (state.isLogoutBottomSheetVisible || state.isDeleteAccountVBottomSheetVisible) bottomSheet.show()
        else if (state.isLoading.not()) bottomSheet.hide()
    }

    Box(Modifier.fillMaxSize()) {
        KyokuWindowSize(
            windowSizeClass = windowSize,
            compactContent = {
                SettingsCompactScreen(
                    state = state,
                    onAction = viewModel::onAction,
                    navigateBack = navigateBack
                )
            },
            mediumContent = {
                SettingsCompactScreen(
                    state = state,
                    onAction = viewModel::onAction,
                    navigateBack = navigateBack
                )
            },
            expandedContent = {
                SettingsExpandedScreen(
                    state = state,
                    onAction = viewModel::onAction,
                    navigateBack = navigateBack
                )
            }
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            if (triple.revealSize.value > 0) {
                drawCircle(
                    color = triple.backgroundColor,
                    radius = triple.revealSize.value,
                    center = triple.center
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            visible = state.isThemChangeScreenVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.pic_them),
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                modifier = Modifier.padding(start = MaterialTheme.dimens.medium1)
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.onAction(SettingsUiAction.OnThemPicToggle)
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(
                                    imageVector = ArrowBackIcon,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .rotate(-90f)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = ThemModeChanger.getGradiantBackground()
                            )
                        )
                        .padding(MaterialTheme.dimens.medium1)
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
                ) {
                    ThemColor.entries.forEach { them ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 5.dp,
                                pressedElevation = 0.dp
                            ),
                            onClick = {
                                viewModel.onAction(SettingsUiAction.OnThemChange(them))
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = ThemChanger.themBackground(them)
                                        )
                                    )
                                    .fillMaxWidth()
                                    .padding(MaterialTheme.dimens.medium1)
                            ) {
                                Text(
                                    text = when (them) {
                                        ThemColor.GREEN -> stringResource(R.string.royal_green)
                                        ThemColor.BLUE -> stringResource(R.string.royal_blue)
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                                )

                                Spacer(Modifier.weight(1f))

                                AnimatedVisibility(visible = ThemChanger.themColor == them) {
                                    Icon(
                                        imageVector = CheckIcon,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary)
                                            .padding(3.dp),
                                        tint = MaterialTheme.colorScheme.background
                                    )
                                }
                            }
                        }
                    }
                }
            }

            BackHandler {
                viewModel.onAction(SettingsUiAction.OnThemPicToggle)
            }
        }
    }

    if (bottomSheet.isVisible) SettingsBottomSheet(
        state.isLoading,
        state.isLogoutBottomSheetVisible,
        viewModel::onAction
    )
}

