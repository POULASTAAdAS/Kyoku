package com.poulastaa.core.presentation.designsystem

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.presentation.designsystem.model.CircularReveal
import com.poulastaa.core.presentation.designsystem.ui.backgroundDark
import com.poulastaa.core.presentation.designsystem.ui.backgroundLight
import com.poulastaa.core.presentation.designsystem.ui.secondaryDark
import com.poulastaa.core.presentation.designsystem.ui.secondaryLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ThemChanger @Inject constructor() {
    companion object Companion : ViewModel() {
        private var ds: DatastoreRepository? = null
        var them by mutableStateOf(true) // initial value is darkTheme
            private set

        internal fun init(ds: DatastoreRepository) {
            this.ds = ds
            loadThem()
        }

        @Composable
        fun getGradiantBackground() = when (them) {
            true -> listOf(
                backgroundDark,
                secondaryDark
            )

            false -> listOf(
                backgroundLight,
                secondaryLight
            )
        }

        fun toggleTheme() {
            viewModelScope.launch {
                ds?.storeThem(them.not())
            }
        }

        private fun loadThem() {
            viewModelScope.launch {
                ds?.readThem()?.collectLatest { state ->
                    them = state
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
            var them by remember { mutableStateOf(ThemChanger.them) }
            val backgroundColor = if (them) backgroundLight else backgroundDark
            var animationOffset by remember { mutableStateOf(Offset(0f, 0f)) }
            val revealSize = remember { Animatable(0f) }
            val screenWidthPx = with(density) { config.screenWidthDp.dp.toPx() }
            val screenHeightPx = with(density) { config.screenHeightDp.dp.toPx() }

            LaunchedEffect(offset) {
                animationOffset = offset
                if (offset.x > 0f) {
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
                    // Animate circle contraction when offset is reset
                    revealSize.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(animationTime)
                    )
                }

                delay(100) // Delay to stop screen flickering
                them = ThemChanger.them // set new them
            }

            return CircularReveal(
                backgroundColor = backgroundColor,
                revealSize = revealSize,
                center = animationOffset
            )
        }
    }
}
