package com.poulastaa.common.ui.design_system

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.min

fun appMaterialShapes(dimens: AppDimensions): Shapes = Shapes(
    extraSmall = SuperellipseShape(dimens.corner.extraSmall, DefaultSmoothnessAsPercent),
    small = SuperellipseShape(dimens.corner.small, DefaultSmoothnessAsPercent),
    medium = SuperellipseShape(dimens.corner.medium, DefaultSmoothnessAsPercent),
    large = SuperellipseShape(dimens.corner.large, DefaultSmoothnessAsPercent),
    extraLarge = SuperellipseShape(dimens.corner.extraLarge, DefaultSmoothnessAsPercent),
)

@Immutable
class SuperellipseShape(
    topStart: CornerSize,
    topEnd: CornerSize,
    bottomEnd: CornerSize,
    bottomStart: CornerSize,
    private val smoothnessAsPercent: Int = DefaultSmoothnessAsPercent,
) : CornerBasedShape(
    topStart = topStart,
    topEnd = topEnd,
    bottomEnd = bottomEnd,
    bottomStart = bottomStart,
) {
    init {
        require(smoothnessAsPercent in 0..100) {
            "Smoothness must be between 0 and 100."
        }
    }

    override fun createOutline(
        size: Size,
        topStart: Float,
        topEnd: Float,
        bottomEnd: Float,
        bottomStart: Float,
        layoutDirection: LayoutDirection,
    ): Outline {
        if (size.width <= 0f || size.height <= 0f) {
            return Outline.Rectangle(size.toRect())
        }

        val topLeft = if (layoutDirection == LayoutDirection.Ltr) topStart else topEnd
        val topRight = if (layoutDirection == LayoutDirection.Ltr) topEnd else topStart
        val bottomRight = if (layoutDirection == LayoutDirection.Ltr) bottomEnd else bottomStart
        val bottomLeft = if (layoutDirection == LayoutDirection.Ltr) bottomStart else bottomEnd

        if (topLeft + topRight + bottomRight + bottomLeft == 0f) {
            return Outline.Rectangle(size.toRect())
        }

        if (smoothnessAsPercent == 0) {
            return Outline.Rounded(
                RoundRect(
                    rect = size.toRect(),
                    topLeft = CornerRadius(topLeft),
                    topRight = CornerRadius(topRight),
                    bottomRight = CornerRadius(bottomRight),
                    bottomLeft = CornerRadius(bottomLeft),
                )
            )
        }

        return Outline.Generic(createSmoothPath(size, topLeft, topRight, bottomRight, bottomLeft))
    }

    override fun copy(
        topStart: CornerSize,
        topEnd: CornerSize,
        bottomEnd: CornerSize,
        bottomStart: CornerSize,
    ): CornerBasedShape = SuperellipseShape(
        topStart = topStart,
        topEnd = topEnd,
        bottomEnd = bottomEnd,
        bottomStart = bottomStart,
        smoothnessAsPercent = smoothnessAsPercent,
    )

    override fun toString(): String {
        return "SuperellipseShape(topStart = $topStart, topEnd = $topEnd, " +
            "bottomEnd = $bottomEnd, bottomStart = $bottomStart)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SuperellipseShape) return false
        if (topStart != other.topStart) return false
        if (topEnd != other.topEnd) return false
        if (bottomEnd != other.bottomEnd) return false
        if (bottomStart != other.bottomStart) return false
        if (smoothnessAsPercent != other.smoothnessAsPercent) return false
        return true
    }

    override fun hashCode(): Int {
        var result = topStart.hashCode()
        result = 31 * result + topEnd.hashCode()
        result = 31 * result + bottomEnd.hashCode()
        result = 31 * result + bottomStart.hashCode()
        result = 31 * result + smoothnessAsPercent
        return result
    }

    private fun createSmoothPath(
        size: Size,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float,
    ): Path = Path().apply {
        val halfOfShortestSide = min(size.height, size.width) / 2f

        var selectedSmoothCorner = SmoothCorner(
            topLeft,
            smoothnessAsPercent,
            halfOfShortestSide,
        )

        moveTo(
            selectedSmoothCorner.anchorPoint1.distanceToClosestSide,
            selectedSmoothCorner.anchorPoint1.distanceToFurthestSide,
        )

        cubicTo(
            selectedSmoothCorner.controlPoint1.distanceToClosestSide,
            selectedSmoothCorner.controlPoint1.distanceToFurthestSide,
            selectedSmoothCorner.controlPoint2.distanceToClosestSide,
            selectedSmoothCorner.controlPoint2.distanceToFurthestSide,
            selectedSmoothCorner.anchorPoint2.distanceToClosestSide,
            selectedSmoothCorner.anchorPoint2.distanceToFurthestSide,
        )

        arcToRad(
            rect = Rect(
                top = 0f,
                left = 0f,
                right = selectedSmoothCorner.arcSection.radius * 2f,
                bottom = selectedSmoothCorner.arcSection.radius * 2f,
            ),
            startAngleRadians = (PI + selectedSmoothCorner.arcSection.arcStartAngle).toFloat(),
            sweepAngleRadians = selectedSmoothCorner.arcSection.arcSweepAngle,
            forceMoveTo = false,
        )

        cubicTo(
            selectedSmoothCorner.controlPoint2.distanceToFurthestSide,
            selectedSmoothCorner.controlPoint2.distanceToClosestSide,
            selectedSmoothCorner.controlPoint1.distanceToFurthestSide,
            selectedSmoothCorner.controlPoint1.distanceToClosestSide,
            selectedSmoothCorner.anchorPoint1.distanceToFurthestSide,
            selectedSmoothCorner.anchorPoint1.distanceToClosestSide,
        )

        selectedSmoothCorner = SmoothCorner(
            topRight,
            smoothnessAsPercent,
            halfOfShortestSide,
        )

        lineTo(
            size.width - selectedSmoothCorner.anchorPoint1.distanceToFurthestSide,
            selectedSmoothCorner.anchorPoint1.distanceToClosestSide,
        )

        cubicTo(
            size.width - selectedSmoothCorner.controlPoint1.distanceToFurthestSide,
            selectedSmoothCorner.controlPoint1.distanceToClosestSide,
            size.width - selectedSmoothCorner.controlPoint2.distanceToFurthestSide,
            selectedSmoothCorner.controlPoint2.distanceToClosestSide,
            size.width - selectedSmoothCorner.anchorPoint2.distanceToFurthestSide,
            selectedSmoothCorner.anchorPoint2.distanceToClosestSide,
        )

        arcToRad(
            rect = Rect(
                top = 0f,
                left = size.width - selectedSmoothCorner.arcSection.radius * 2f,
                right = size.width,
                bottom = selectedSmoothCorner.arcSection.radius * 2f,
            ),
            startAngleRadians =
                (3 * PI / 2 + selectedSmoothCorner.arcSection.arcStartAngle).toFloat(),
            sweepAngleRadians = selectedSmoothCorner.arcSection.arcSweepAngle,
            forceMoveTo = false,
        )

        cubicTo(
            size.width - selectedSmoothCorner.controlPoint2.distanceToClosestSide,
            selectedSmoothCorner.controlPoint2.distanceToFurthestSide,
            size.width - selectedSmoothCorner.controlPoint1.distanceToClosestSide,
            selectedSmoothCorner.controlPoint1.distanceToFurthestSide,
            size.width - selectedSmoothCorner.anchorPoint1.distanceToClosestSide,
            selectedSmoothCorner.anchorPoint1.distanceToFurthestSide,
        )

        selectedSmoothCorner = SmoothCorner(
            bottomRight,
            smoothnessAsPercent,
            halfOfShortestSide,
        )

        lineTo(
            size.width - selectedSmoothCorner.anchorPoint1.distanceToClosestSide,
            size.height - selectedSmoothCorner.anchorPoint1.distanceToFurthestSide,
        )

        cubicTo(
            size.width - selectedSmoothCorner.controlPoint1.distanceToClosestSide,
            size.height - selectedSmoothCorner.controlPoint1.distanceToFurthestSide,
            size.width - selectedSmoothCorner.controlPoint2.distanceToClosestSide,
            size.height - selectedSmoothCorner.controlPoint2.distanceToFurthestSide,
            size.width - selectedSmoothCorner.anchorPoint2.distanceToClosestSide,
            size.height - selectedSmoothCorner.anchorPoint2.distanceToFurthestSide,
        )

        arcToRad(
            rect = Rect(
                top = size.height - selectedSmoothCorner.arcSection.radius * 2f,
                left = size.width - selectedSmoothCorner.arcSection.radius * 2f,
                right = size.width,
                bottom = size.height,
            ),
            startAngleRadians = selectedSmoothCorner.arcSection.arcStartAngle,
            sweepAngleRadians = selectedSmoothCorner.arcSection.arcSweepAngle,
            forceMoveTo = false,
        )

        cubicTo(
            size.width - selectedSmoothCorner.controlPoint2.distanceToFurthestSide,
            size.height - selectedSmoothCorner.controlPoint2.distanceToClosestSide,
            size.width - selectedSmoothCorner.controlPoint1.distanceToFurthestSide,
            size.height - selectedSmoothCorner.controlPoint1.distanceToClosestSide,
            size.width - selectedSmoothCorner.anchorPoint1.distanceToFurthestSide,
            size.height - selectedSmoothCorner.anchorPoint1.distanceToClosestSide,
        )

        selectedSmoothCorner = SmoothCorner(
            bottomLeft,
            smoothnessAsPercent,
            halfOfShortestSide,
        )

        lineTo(
            selectedSmoothCorner.anchorPoint1.distanceToFurthestSide,
            size.height - selectedSmoothCorner.anchorPoint1.distanceToClosestSide,
        )

        cubicTo(
            selectedSmoothCorner.controlPoint1.distanceToFurthestSide,
            size.height - selectedSmoothCorner.controlPoint1.distanceToClosestSide,
            selectedSmoothCorner.controlPoint2.distanceToFurthestSide,
            size.height - selectedSmoothCorner.controlPoint2.distanceToClosestSide,
            selectedSmoothCorner.anchorPoint2.distanceToFurthestSide,
            size.height - selectedSmoothCorner.anchorPoint2.distanceToClosestSide,
        )

        arcToRad(
            rect = Rect(
                top = size.height - selectedSmoothCorner.arcSection.radius * 2f,
                left = 0f,
                right = selectedSmoothCorner.arcSection.radius * 2f,
                bottom = size.height,
            ),
            startAngleRadians = (PI / 2 + selectedSmoothCorner.arcSection.arcStartAngle).toFloat(),
            sweepAngleRadians = selectedSmoothCorner.arcSection.arcSweepAngle,
            forceMoveTo = false,
        )

        cubicTo(
            selectedSmoothCorner.controlPoint2.distanceToClosestSide,
            size.height - selectedSmoothCorner.controlPoint2.distanceToFurthestSide,
            selectedSmoothCorner.controlPoint1.distanceToClosestSide,
            size.height - selectedSmoothCorner.controlPoint1.distanceToFurthestSide,
            selectedSmoothCorner.anchorPoint1.distanceToClosestSide,
            size.height - selectedSmoothCorner.anchorPoint1.distanceToFurthestSide,
        )

        close()
    }
}

fun SuperellipseShape(
    corner: CornerSize,
    smoothnessAsPercent: Int = DefaultSmoothnessAsPercent,
): SuperellipseShape = SuperellipseShape(
    topStart = corner,
    topEnd = corner,
    bottomEnd = corner,
    bottomStart = corner,
    smoothnessAsPercent = smoothnessAsPercent,
)

fun SuperellipseShape(
    size: Dp,
    smoothnessAsPercent: Int = DefaultSmoothnessAsPercent,
): SuperellipseShape = SuperellipseShape(
    corner = CornerSize(size),
    smoothnessAsPercent = smoothnessAsPercent,
)

fun SuperellipseShape(
    topStart: Dp = 0.dp,
    topEnd: Dp = 0.dp,
    bottomEnd: Dp = 0.dp,
    bottomStart: Dp = 0.dp,
    smoothnessAsPercent: Int = DefaultSmoothnessAsPercent,
): SuperellipseShape = SuperellipseShape(
    topStart = CornerSize(topStart),
    topEnd = CornerSize(topEnd),
    bottomEnd = CornerSize(bottomEnd),
    bottomStart = CornerSize(bottomStart),
    smoothnessAsPercent = smoothnessAsPercent,
)

private const val DefaultSmoothnessAsPercent = 100
