package com.poulastaa.core.presentation.designsystem

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.model.ThemColor
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.presentation.designsystem.model.CircularReveal
import com.poulastaa.core.presentation.designsystem.ui.blueBackgroundDark
import com.poulastaa.core.presentation.designsystem.ui.blueBackgroundLight
import com.poulastaa.core.presentation.designsystem.ui.greenBackgroundDark
import com.poulastaa.core.presentation.designsystem.ui.greenBackgroundLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ThemModeChanger @Inject constructor() {
    companion object Companion : ViewModel() {
        private var ds: DatastoreRepository? = null
        var themMode by mutableStateOf(true) // initial value is darkTheme
            private set

        internal fun init(ds: DatastoreRepository) {
            this.ds = ds
            loadThemMode()
        }

        @Composable
        fun getGradiantBackground() = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.secondary
        )

        fun toggleThemeMode() {
            viewModelScope.launch {
                ds?.storeThemMode(themMode.not())
            }
        }

        private fun loadThemMode() {
            viewModelScope.launch {
                ds?.readThemMode()?.collectLatest { state ->
                    themMode = state
                }
            }
        }

        @Composable
        fun compute(
            density: Density,
            config: Configuration,
            offset: Offset,
            animationTime: Int,
            resetAnimation: () -> Unit,
        ): CircularReveal {
            // prevents color change when changing them
            var them by remember { mutableStateOf(themMode) }
            val backgroundColor = when (ThemChanger.themColor) {
                ThemColor.GREEN -> if (them) greenBackgroundLight else greenBackgroundDark
                ThemColor.BLUE -> if (them) blueBackgroundLight else blueBackgroundDark
            }
            var animationOffset by remember { mutableStateOf(Offset(0f, 0f)) }
            val revealSize = remember { Animatable(0f) }
            val screenWidthPx = with(density) { config.screenWidthDp.dp.toPx() }
            val screenHeightPx = with(density) { config.screenHeightDp.dp.toPx() }

            var initialOffset by remember { mutableStateOf(offset) }

            LaunchedEffect(offset) {
                animationOffset = offset
                if (offset.x > 0f) {
                    initialOffset = offset
                    // Calculate maximum radius from touch point to screen corners
                    val maxRadius = listOf(
                        (animationOffset - Offset(0f, 0f)).getDistance(),
                        (animationOffset - Offset(screenWidthPx, 0f)).getDistance(),
                        (animationOffset - Offset(0f, screenHeightPx)).getDistance(),
                        (animationOffset - Offset(screenWidthPx, screenHeightPx)).getDistance()
                    ).maxOrNull() ?: 0f

                    // Animate circle expansion
                    revealSize.snapTo(0f)
                    revealSize.animateTo(
                        targetValue = maxRadius,
                        animationSpec = tween(animationTime)
                    )
                    // Reset offset after animation to trigger contraction
                    resetAnimation()
                } else {
                    animationOffset = initialOffset

                    // Animate circle contraction when offset is reset
                    revealSize.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(animationTime)
                    )
                }

                delay(100) // Delay to stop screen flickering
                them = themMode // set new them
            }

            return CircularReveal(
                backgroundColor = backgroundColor,
                revealSize = revealSize,
                center = animationOffset
            )
        }
    }
}
