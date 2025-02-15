package com.poulastaa.main.presentation.main.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.toSize
import com.poulastaa.core.presentation.designsystem.model.WiggleButtonParams
import com.poulastaa.core.presentation.designsystem.noRippleClickable
import com.poulastaa.core.presentation.designsystem.toPxf

@Composable
internal fun WiggleButton(
    modifier: Modifier = Modifier,
    iconSize: Dp,
    isSelected: Boolean,
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    @DrawableRes backgroundIcon: Int,
    contentDescription: String? = null,
    enterExitAnimationSpec: AnimationSpec<Float>,
    backgroundIconColor: Color = MaterialTheme.colorScheme.background,
    wiggleColor: Color = MaterialTheme.colorScheme.primary,
    outlineColor: Color = MaterialTheme.colorScheme.primary,
    wiggleAnimationSpec: AnimationSpec<Float> = spring(dampingRatio = 0.6f, stiffness = 35f),
) {
    Box(
        modifier = modifier.noRippleClickable(onClick = onClick)
    ) {
        DrawWithBlendMode(
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.Center),
            icon = icon,
            backgroundIcon = backgroundIcon,
            isSelected = isSelected,
            wiggleColor = wiggleColor,
            backgroundIconColor = backgroundIconColor,
            outlineColor = outlineColor,
            contentDescription = contentDescription,
            enterExitAnimationSpec = enterExitAnimationSpec,
            wiggleAnimationSpec = wiggleAnimationSpec,
            size = iconSize,
        )
    }
}

@Composable
private fun DrawWithBlendMode(
    modifier: Modifier,
    isSelected: Boolean,
    @DrawableRes icon: Int,
    @DrawableRes backgroundIcon: Int,
    contentDescription: String? = null,
    wiggleColor: Color,
    size: Dp,
    backgroundIconColor: Color,
    enterExitAnimationSpec: AnimationSpec<Float>,
    wiggleAnimationSpec: AnimationSpec<Float>,
    outlineColor: Color,
) {
    val vector = ImageVector.vectorResource(id = icon)
    val painter = rememberVectorPainter(image = vector)

    val backgroundVector = ImageVector.vectorResource(id = backgroundIcon)
    val backgroundPainter = rememberVectorPainter(image = backgroundVector)

    var canvasSize by remember { mutableStateOf(Size.Zero) }

    val density = LocalDensity.current
    val sizePx = remember(size) { size.toPxf(density) }

    val wiggleButtonParams = animateWiggleButtonAsState(
        isSelected = isSelected,
        enterExitAnimationSpec = enterExitAnimationSpec,
        wiggleAnimationSpec = wiggleAnimationSpec,
        maxRadius = sizePx
    )

    val offset by remember {
        derivedStateOf {
            mutableStateOf(
                Offset(x = canvasSize.width / 2, y = canvasSize.height)
            )
        }
    }

    Canvas(
        modifier = modifier
            .graphicsLayer(
                alpha = wiggleButtonParams.value.alpha,
                scaleX = wiggleButtonParams.value.scale,
                scaleY = wiggleButtonParams.value.scale
            )
            .fillMaxSize()
            .onGloballyPositioned { canvasSize = it.size.toSize() },
        contentDescription = contentDescription ?: ""
    ) {
        with(backgroundPainter) {
            draw(
                size = Size(sizePx, sizePx),
                colorFilter = ColorFilter.tint(color = backgroundIconColor)
            )
        }

        drawCircle(
            color = wiggleColor,
            center = offset.value,
            radius = wiggleButtonParams.value.radius,
            blendMode = BlendMode.SrcIn
        )

        with(painter) {
            draw(
                size = Size(sizePx, sizePx),
                colorFilter = ColorFilter.tint(color = outlineColor)
            )
        }
    }
}

@Composable
private fun animateWiggleButtonAsState(
    isSelected: Boolean,
    enterExitAnimationSpec: AnimationSpec<Float>,
    wiggleAnimationSpec: AnimationSpec<Float>,
    maxRadius: Float,
): State<WiggleButtonParams> {
    val enterExitFraction = animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = enterExitAnimationSpec, label = ""
    )

    val wiggleFraction = animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = wiggleAnimationSpec, label = ""
    )

    val isAnimationRequired by rememberUpdatedState(newValue = isSelected)

    return produceState(
        initialValue = WiggleButtonParams(),
        key1 = enterExitFraction.value,
        key2 = wiggleFraction.value
    ) {
        this.value = this.value.copy(
            scale = scaleInterpolator(enterExitFraction.value),
            alpha = alphaInterpolator(enterExitFraction.value),
            radius = if (isAnimationRequired) calculateRadius(
                maxRadius = maxRadius * 0.8f,
                fraction = radiusInterpolator(wiggleFraction.value),
                minRadius = mildRadius * maxRadius
            ) else mildRadius * maxRadius
        )
    }
}

private const val mildRadius = 0.55f

private fun scaleInterpolator(fraction: Float): Float = 1 + fraction * 0.2f

private fun alphaInterpolator(fraction: Float): Float = fraction / 2 + 0.5f - 0.01f

private fun calculateRadius(
    maxRadius: Float,
    fraction: Float,
    minRadius: Float,
) = (fraction * (maxRadius - minRadius)) + minRadius

private fun radiusInterpolator(fraction: Float) = if (fraction < 0.5f) fraction * 2
else (1 - fraction) * 2