package com.poulastaa.common.ui.design_system

import androidx.compose.material3.Typography
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kyoku.common.ui.generated.resources.Res
import kyoku.common.ui.generated.resources.arima_bold
import kyoku.common.ui.generated.resources.arima_extra_light
import kyoku.common.ui.generated.resources.arima_medium
import kyoku.common.ui.generated.resources.arima_regular
import kyoku.common.ui.generated.resources.arima_semi_bold
import kyoku.common.ui.generated.resources.arima_thin
import kyoku.common.ui.generated.resources.averia_serif_libre_bold
import kyoku.common.ui.generated.resources.averia_serif_libre_bold_italic
import kyoku.common.ui.generated.resources.averia_serif_libre_italic
import kyoku.common.ui.generated.resources.averia_serif_libre_light
import kyoku.common.ui.generated.resources.averia_serif_libre_light_italic
import kyoku.common.ui.generated.resources.averia_serif_libre_regular
import org.jetbrains.compose.resources.Font

@Composable
expect fun calculateWindowSizeClass(): WindowSizeClass

@Composable
fun displayFontFamily(): FontFamily = FontFamily(
    Font(Res.font.averia_serif_libre_light, FontWeight.Thin),
    Font(Res.font.averia_serif_libre_light, FontWeight.ExtraLight),
    Font(Res.font.averia_serif_libre_light, FontWeight.Light),
    Font(Res.font.averia_serif_libre_regular, FontWeight.Normal),
    Font(Res.font.averia_serif_libre_regular, FontWeight.Medium),
    Font(Res.font.averia_serif_libre_bold, FontWeight.SemiBold),
    Font(Res.font.averia_serif_libre_bold, FontWeight.Bold),
    Font(Res.font.averia_serif_libre_bold, FontWeight.ExtraBold),
    Font(Res.font.averia_serif_libre_bold, FontWeight.Black),

    Font(Res.font.averia_serif_libre_light_italic, FontWeight.Thin, FontStyle.Italic),
    Font(Res.font.averia_serif_libre_light_italic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(Res.font.averia_serif_libre_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(Res.font.averia_serif_libre_italic, FontWeight.Normal, FontStyle.Italic),
    Font(Res.font.averia_serif_libre_italic, FontWeight.Medium, FontStyle.Italic),
    Font(Res.font.averia_serif_libre_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(Res.font.averia_serif_libre_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(Res.font.averia_serif_libre_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(Res.font.averia_serif_libre_bold_italic, FontWeight.Black, FontStyle.Italic),
)

@Composable
fun bodyFontFamily(): FontFamily = FontFamily(
    Font(Res.font.arima_thin, FontWeight.Thin),
    Font(Res.font.arima_extra_light, FontWeight.ExtraLight),
    Font(Res.font.arima_regular, FontWeight.Light),
    Font(Res.font.arima_regular, FontWeight.Normal),
    Font(Res.font.arima_medium, FontWeight.Medium),
    Font(Res.font.arima_semi_bold, FontWeight.SemiBold),
    Font(Res.font.arima_bold, FontWeight.Bold),
    Font(Res.font.arima_bold, FontWeight.ExtraBold),
    Font(Res.font.arima_bold, FontWeight.Black),

    Font(Res.font.arima_thin, FontWeight.Thin, FontStyle.Italic),
    Font(Res.font.arima_extra_light, FontWeight.ExtraLight, FontStyle.Italic),
    Font(Res.font.arima_regular, FontWeight.Light, FontStyle.Italic),
    Font(Res.font.arima_regular, FontWeight.Normal, FontStyle.Italic),
    Font(Res.font.arima_medium, FontWeight.Medium, FontStyle.Italic),
    Font(Res.font.arima_semi_bold, FontWeight.SemiBold, FontStyle.Italic),
    Font(Res.font.arima_bold, FontWeight.Bold, FontStyle.Italic),
    Font(Res.font.arima_bold, FontWeight.ExtraBold, FontStyle.Italic),
    Font(Res.font.arima_bold, FontWeight.Black, FontStyle.Italic),
)


private data class TypeScale(
    // Display
    val displayLargeSize: TextUnit,
    val displayLargeHeight: TextUnit,
    val displayMediumSize: TextUnit,
    val displayMediumHeight: TextUnit,
    val displaySmallSize: TextUnit,
    val displaySmallHeight: TextUnit,
    // Headline
    val headlineLargeSize: TextUnit,
    val headlineLargeHeight: TextUnit,
    val headlineMediumSize: TextUnit,
    val headlineMediumHeight: TextUnit,
    val headlineSmallSize: TextUnit,
    val headlineSmallHeight: TextUnit,
    // Title
    val titleLargeSize: TextUnit,
    val titleLargeHeight: TextUnit,
    val titleMediumSize: TextUnit,
    val titleMediumHeight: TextUnit,
    val titleSmallSize: TextUnit,
    val titleSmallHeight: TextUnit,
    // Body
    val bodyLargeSize: TextUnit,
    val bodyLargeHeight: TextUnit,
    val bodyMediumSize: TextUnit,
    val bodyMediumHeight: TextUnit,
    val bodySmallSize: TextUnit,
    val bodySmallHeight: TextUnit,
    // Label
    val labelLargeSize: TextUnit,
    val labelLargeHeight: TextUnit,
    val labelMediumSize: TextUnit,
    val labelMediumHeight: TextUnit,
    val labelSmallSize: TextUnit,
    val labelSmallHeight: TextUnit,
)

// Compact — standard M3 phone scale
private val CompactScale = TypeScale(
    displayLargeSize = 57.sp,
    displayLargeHeight = 64.sp,
    displayMediumSize = 45.sp,
    displayMediumHeight = 52.sp,
    displaySmallSize = 36.sp,
    displaySmallHeight = 44.sp,

    headlineLargeSize = 32.sp,
    headlineLargeHeight = 40.sp,
    headlineMediumSize = 28.sp,
    headlineMediumHeight = 36.sp,
    headlineSmallSize = 24.sp,
    headlineSmallHeight = 32.sp,

    titleLargeSize = 22.sp,
    titleLargeHeight = 28.sp,
    titleMediumSize = 16.sp,
    titleMediumHeight = 24.sp,
    titleSmallSize = 14.sp,
    titleSmallHeight = 20.sp,

    bodyLargeSize = 16.sp,
    bodyLargeHeight = 24.sp,
    bodyMediumSize = 14.sp,
    bodyMediumHeight = 20.sp,
    bodySmallSize = 12.sp,
    bodySmallHeight = 16.sp,

    labelLargeSize = 14.sp,
    labelLargeHeight = 20.sp,
    labelMediumSize = 12.sp,
    labelMediumHeight = 16.sp,
    labelSmallSize = 11.sp,
    labelSmallHeight = 16.sp,
)

// Medium — tablet portrait / phone landscape
private val MediumScale = TypeScale(
    displayLargeSize = 64.sp,
    displayLargeHeight = 72.sp,
    displayMediumSize = 52.sp,
    displayMediumHeight = 60.sp,
    displaySmallSize = 40.sp,
    displaySmallHeight = 48.sp,
    headlineLargeSize = 36.sp,
    headlineLargeHeight = 44.sp,
    headlineMediumSize = 32.sp,
    headlineMediumHeight = 40.sp,
    headlineSmallSize = 28.sp,
    headlineSmallHeight = 36.sp,
    titleLargeSize = 24.sp,
    titleLargeHeight = 32.sp,
    titleMediumSize = 18.sp,
    titleMediumHeight = 26.sp,
    titleSmallSize = 16.sp,
    titleSmallHeight = 22.sp,
    bodyLargeSize = 16.sp,
    bodyLargeHeight = 24.sp,
    bodyMediumSize = 14.sp,
    bodyMediumHeight = 20.sp,
    bodySmallSize = 12.sp,
    bodySmallHeight = 16.sp,
    labelLargeSize = 14.sp,
    labelLargeHeight = 20.sp,
    labelMediumSize = 12.sp,
    labelMediumHeight = 16.sp,
    labelSmallSize = 11.sp,
    labelSmallHeight = 16.sp,
)

// Expanded — tablet landscape / desktop
private val ExpandedScale = TypeScale(
    displayLargeSize = 72.sp,
    displayLargeHeight = 80.sp,
    displayMediumSize = 57.sp,
    displayMediumHeight = 66.sp,
    displaySmallSize = 45.sp,
    displaySmallHeight = 52.sp,
    headlineLargeSize = 40.sp,
    headlineLargeHeight = 48.sp,
    headlineMediumSize = 36.sp,
    headlineMediumHeight = 44.sp,
    headlineSmallSize = 32.sp,
    headlineSmallHeight = 40.sp,
    titleLargeSize = 28.sp,
    titleLargeHeight = 36.sp,
    titleMediumSize = 20.sp,
    titleMediumHeight = 28.sp,
    titleSmallSize = 18.sp,
    titleSmallHeight = 24.sp,
    bodyLargeSize = 18.sp,
    bodyLargeHeight = 26.sp,
    bodyMediumSize = 16.sp,
    bodyMediumHeight = 22.sp,
    bodySmallSize = 14.sp,
    bodySmallHeight = 20.sp,
    labelLargeSize = 16.sp,
    labelLargeHeight = 22.sp,
    labelMediumSize = 14.sp,
    labelMediumHeight = 20.sp,
    labelSmallSize = 12.sp,
    labelSmallHeight = 16.sp,
)

@Composable
fun appTypography(
    windowSizeClass: WindowSizeClass = calculateWindowSizeClass(),
): Typography {
    val display = displayFontFamily()
    val body = bodyFontFamily()

    val scale = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> ExpandedScale
        WindowWidthSizeClass.Medium -> MediumScale
        else -> CompactScale
    }

    // ─── iOS / Skia letter-spacing note ──────────────────────────────────────
    // Compose Multiplatform renders via Skia on iOS. Skia delegates text
    // shaping to HarfBuzz, which historically applies wider tracking than
    // CoreText. The safest mitigation is:
    //   • 0.sp  on display / headline / title (already the M3 default)
    //   • keep body/label values small (≤ 0.25.sp) to minimise the visual gap
    // Negative letterSpacing is intentionally kept only on displayLarge
    // because it is the only M3 token where it is specified, and the absolute
    // difference is small enough that the Skia over-tracking is not noticeable.
    // ─────────────────────────────────────────────────────────────────────────

    return Typography(
        // ── Display ──────────────────────────────────────────────────────────
        displayLarge = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.Normal,
            fontSize = scale.displayLargeSize,
            lineHeight = scale.displayLargeHeight,
            letterSpacing = (-0.25).sp,  // only token with negative spacing in M3
        ),
        displayMedium = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.Normal,
            fontSize = scale.displayMediumSize,
            lineHeight = scale.displayMediumHeight,
            letterSpacing = 0.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.Normal,
            fontSize = scale.displaySmallSize,
            lineHeight = scale.displaySmallHeight,
            letterSpacing = 0.sp,
        ),

        // ── Headline ─────────────────────────────────────────────────────────
        headlineLarge = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.SemiBold,
            fontSize = scale.headlineLargeSize,
            lineHeight = scale.headlineLargeHeight,
            letterSpacing = 0.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.SemiBold,
            fontSize = scale.headlineMediumSize,
            lineHeight = scale.headlineMediumHeight,
            letterSpacing = 0.sp,
        ),
        headlineSmall = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.SemiBold,
            fontSize = scale.headlineSmallSize,
            lineHeight = scale.headlineSmallHeight,
            letterSpacing = 0.sp,
        ),

        // ── Title ─────────────────────────────────────────────────────────────
        titleLarge = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.Bold,
            fontSize = scale.titleLargeSize,
            lineHeight = scale.titleLargeHeight,
            letterSpacing = 0.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.Medium,
            fontSize = scale.titleMediumSize,
            lineHeight = scale.titleMediumHeight,
            // M3 spec: 0.15sp — halved to 0.1sp to reduce iOS Skia over-tracking
            letterSpacing = 0.1.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.Medium,
            fontSize = scale.titleSmallSize,
            lineHeight = scale.titleSmallHeight,
            letterSpacing = 0.1.sp,
        ),

        // ── Body ──────────────────────────────────────────────────────────────
        bodyLarge = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Normal,
            fontSize = scale.bodyLargeSize,
            lineHeight = scale.bodyLargeHeight,
            // M3 spec: 0.5sp — reduced to 0.25sp; the original causes visible
            // over-tracking on iOS due to the Skia/HarfBuzz letter-spacing issue
            letterSpacing = 0.25.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Normal,
            fontSize = scale.bodyMediumSize,
            lineHeight = scale.bodyMediumHeight,
            letterSpacing = 0.15.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Normal,
            fontSize = scale.bodySmallSize,
            lineHeight = scale.bodySmallHeight,
            letterSpacing = 0.1.sp,
        ),

        // ── Label ─────────────────────────────────────────────────────────────
        labelLarge = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Medium,
            fontSize = scale.labelLargeSize,
            lineHeight = scale.labelLargeHeight,
            letterSpacing = 0.1.sp,
        ),
        labelMedium = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Medium,
            fontSize = scale.labelMediumSize,
            lineHeight = scale.labelMediumHeight,
            // M3 spec: 0.5sp — reduced to 0.25sp for same iOS reason as bodyLarge
            letterSpacing = 0.25.sp,
        ),
        labelSmall = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Medium,
            fontSize = scale.labelSmallSize,
            lineHeight = scale.labelSmallHeight,
            letterSpacing = 0.25.sp,
        ),
    )
}