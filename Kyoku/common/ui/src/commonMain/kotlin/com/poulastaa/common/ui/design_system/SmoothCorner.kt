package com.poulastaa.common.ui.design_system

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

internal class SmoothCorner(
    cornerRadius: Float,
    smoothnessAsPercent: Int,
    maximumCurveStartDistanceFromVertex: Float,
) {
    init {
        require(smoothnessAsPercent >= 0) {
            "Smoothness cannot be negative."
        }
    }

    private val radius = min(cornerRadius, maximumCurveStartDistanceFromVertex)
    private val smoothness = smoothnessAsPercent / 100f
    private val curveStartDistance = min(
        maximumCurveStartDistanceFromVertex,
        (1f + smoothness) * radius,
    )

    private val shouldCurveInterpolate = radius <= maximumCurveStartDistanceFromVertex / 2f
    private val interpolationMultiplier = (radius - maximumCurveStartDistanceFromVertex / 2f) /
            (maximumCurveStartDistanceFromVertex / 2f)

    private val angleAlpha = if (shouldCurveInterpolate) {
        toRadians(45.0 * smoothness)
    } else {
        toRadians(45.0 * smoothness * (1 - interpolationMultiplier))
    }

    private val angleBeta = if (shouldCurveInterpolate) {
        toRadians(90.0 * (1.0 - smoothness))
    } else {
        toRadians(90.0 * (1 - smoothness * (1 - interpolationMultiplier)))
    }

    private val angleTheta = ((toRadians(90.0) - angleBeta) / 2.0).toFloat()
    private val distanceE = radius * tan(angleTheta / 2f)
    private val distanceC = distanceE * cos(angleAlpha)
    private val distanceD = distanceC * tan(angleAlpha)
    private val distanceK = sin(angleBeta / 2f) * radius
    private val distanceL = (distanceK * sqrt(2.0)).toFloat()
    private val distanceB =
        ((curveStartDistance - distanceL) - (1f + tan(angleAlpha)) * distanceC) / 3f
    private val distanceA = 2f * distanceB

    val anchorPoint1 = PointRelativeToVertex(
        min(curveStartDistance, maximumCurveStartDistanceFromVertex),
        0f,
    )

    val controlPoint1 = PointRelativeToVertex(
        anchorPoint1.distanceToFurthestSide - distanceA,
        0f,
    )

    val controlPoint2 = PointRelativeToVertex(
        controlPoint1.distanceToFurthestSide - distanceB,
        0f,
    )

    val anchorPoint2 = PointRelativeToVertex(
        controlPoint2.distanceToFurthestSide - distanceC,
        distanceD,
    )

    val arcSection = Arc(
        radius = radius,
        arcStartAngle = angleTheta,
        arcSweepAngle = angleBeta,
    )

    private fun toRadians(angle: Double): Float = (angle * (PI / 180.0)).toFloat()
}

internal data class PointRelativeToVertex(
    val distanceToFurthestSide: Float,
    val distanceToClosestSide: Float,
)

internal data class Arc(
    val radius: Float,
    val arcStartAngle: Float,
    val arcSweepAngle: Float,
)
