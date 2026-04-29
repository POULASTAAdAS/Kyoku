package com.poulastaa.common.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppDimensions(
    val spacing: Spacing,
    val layout: Layout,
    val icon: Icon,
    val component: Component,
    val touch: Touch,
    val corner: Corner,
    val border: Border,
    val elevation: Elevation,
) {

    // ── Spacing ───────────────────────────────────────────────────────────────
    // All gaps, paddings, and gutters.
    // Built on the M3 4dp base grid — every value is a multiple of 4dp except
    // `hairline` (1dp), which is reserved for dividers and thin rules only.
    data class Spacing(
        val none: Dp,           //  0dp
        val hairline: Dp,       //  1dp  — dividers / thin rules only; NOT for layout gaps
        val extraSmall: Dp,     //  4dp
        val small: Dp,          //  8dp
        val medium: Dp,         // 12dp
        val large: Dp,          // 16dp  — M3 baseline gap between items
        val extraLarge: Dp,     // 24dp
        val huge: Dp,           // 32dp
        val massive: Dp,        // 48dp
        val colossal: Dp,       // 64dp
    )

    // ── Layout ────────────────────────────────────────────────────────────────
    // Screen-level structural values that adapt to window-size class.
    // M3 specifies 16dp horizontal margins for Compact and 24dp for Medium/Expanded.
    // Ref: https://m3.material.io/foundations/layout/understanding-layout/spacing
    data class Layout(
        val marginHorizontal: Dp,   // 16 / 24 / 32
        val marginVertical: Dp,     // 16 / 24 / 24
        val contentPadding: Dp,     // alias for the most common inner padding
        val paneSpacing: Dp,        // gap between adaptive layout panes
    )

    // ── Icons ─────────────────────────────────────────────────────────────────
    // M3 system icons default to 24dp. Other sizes exist for decorative and
    // avatar-style uses. Avoid fractional dp values — they cause sub-pixel
    // rendering artefacts on Skia (iOS) and on low-density Android screens.
    data class Icon(
        val small: Dp,          // 16dp  — inline / trailing icons in dense rows
        val medium: Dp,         // 24dp  — M3 default; most toolbar / list icons
        val large: Dp,          // 32dp  — feature icons, empty-state illustrations
        val extraLarge: Dp,     // 40dp  — avatar-sized; profile pictures etc.
    )

    // ── Component heights ─────────────────────────────────────────────────────
    // Canonical heights for interactive controls (buttons, text fields, chips…).
    // `large` and above meet the 48dp touch-target floor from accessibility specs.
    data class Component(
        val small: Dp,          // 32dp  — compact / dense UI (e.g. filter chips)
        val medium: Dp,         // 40dp  — standard buttons, outlined text fields
        val large: Dp,          // 48dp  — meets M3 / WCAG touch-target minimum
        val extraLarge: Dp,     // 56dp  — FAB, large primary buttons
    )

    // ── Touch / pointer targets ───────────────────────────────────────────────
    // ⚠️  NEVER scale `touch` down below 48dp.
    //     Android's accessibility framework enforces a minimum touch-target size
    //     of 48×48dp via ViewCompat.setMinimumHeight / minimumTouchTargetSize.
    //     Compose Material components honour this automatically when you use the
    //     built-in Indication, but custom components must enforce it manually.
    //
    //     On iOS/Skia, Compose Multiplatform does NOT hook into UIAccessibility's
    //     minimum tap-target enforcement. You are responsible for ensuring that
    //     every tappable composable is wrapped in a Modifier.minimumInteractiveSize()
    //     or sized to at least touchTarget.
    //
    //     `pointer` (44dp) is safe only for mouse / trackpad targets on desktop.
    //     Never use it as the sole target size on a touch screen.
    data class Touch(
        val touch: Dp,          // 48dp  — WCAG / M3 minimum for finger targets
        val pointer: Dp,        // 44dp  — pointer-only (desktop / landscape iPad)
    )

    // ── Corners (M3 Shape tokens) ─────────────────────────────────────────────
    // Maps 1-to-1 with M3's shape scale so you can keep corner radii and
    // MaterialTheme.shapes in sync without duplicating magic numbers.
    // Ref: https://m3.material.io/styles/shape/shape-scale-tokens
    //
    // iOS/Skia note: Skia renders RoundRect corners correctly, but sub-pixel
    // corner radii (< 1dp) may look slightly different to CoreAnimation layers.
    // Keep cornerNone truly at 0dp to avoid a faint anti-aliased arc.
    data class Corner(
        val none: Dp,           //  0dp  — sharp (e.g. navigation rail indicator)
        val extraSmall: Dp,     //  4dp  — menu items, snackbars
        val small: Dp,          //  8dp  — chips, text fields
        val medium: Dp,         // 12dp  — cards (M3 default)
        val large: Dp,          // 16dp  — dialogs, navigation drawers
        val extraLarge: Dp,     // 28dp  — FAB, extended FAB
        val full: Dp,           // 1000dp — pill shape (buttons, search bars)
        //          Use a large finite value; Dp.Infinity
        //          cannot be passed to RoundedCornerShape.
    )

    // ── Borders / strokes ─────────────────────────────────────────────────────
    // Logical border widths.
    //
    // iOS/Skia note: Skia strokes are centred on the path, so a 1dp border on a
    // composable actually consumes 0.5dp inside + 0.5dp outside the boundary.
    // This matches how Android's Canvas draws borders, so no adjustment is needed.
    // However, on very-high-density iOS screens (3× scale factor) a `hairline`
    // (1dp) border will render as 3 physical pixels — this is usually desirable,
    // but verify in the iOS simulator at 3× to ensure it looks intentional.
    data class Border(
        val thin: Dp,           // 1dp  — outlined components (buttons, text fields)
        val medium: Dp,         // 2dp  — focus rings, emphasis borders
        val thick: Dp,          // 4dp  — decorative / illustrative dividers
    )

    // ── Elevation ─────────────────────────────────────────────────────────────
    // M3 defines 5 tonal-elevation levels, each mapped to a surface tint.
    // Pass these as `tonalElevation` (tint) or `shadowElevation` (shadow drop).
    //
    // iOS/Skia note: Skia renders drop shadows using blur + offset, but it does
    // NOT use the Android RenderThread elevation-based shadow system. This means:
    //   • Shadows may look subtly flatter or brighter on iOS.
    //   • Avoid heavy reliance on large `shadowElevation` for visual hierarchy
    //     in cross-platform components — prefer `tonalElevation` (tint) or
    //     explicit borders instead, which render identically on both platforms.
    data class Elevation(
        val none: Dp,           //  0dp
        val level1: Dp,         //  1dp  — cards resting on surface
        val level2: Dp,         //  3dp  — FAB resting, outlined card hovered
        val level3: Dp,         //  6dp  — navigation drawer, modal side sheet
        val level4: Dp,         //  8dp  — navigation bar
        val level5: Dp,         // 12dp  — navigation rail
    )
}

// ─── Private scale carrier ────────────────────────────────────────────────────
// Mirrors AppDimensions but flat — easier to define the three window-size
// variants without nesting. Converted to AppDimensions in appDimensions().
// ─────────────────────────────────────────────────────────────────────────────

private data class DimScale(
    // Spacing
    val spacingNone: Dp,
    val spacingHairline: Dp,
    val spacingExtraSmall: Dp,
    val spacingSmall: Dp,
    val spacingMedium: Dp,
    val spacingLarge: Dp,
    val spacingExtraLarge: Dp,
    val spacingHuge: Dp,
    val spacingMassive: Dp,
    val spacingColossal: Dp,
    // Layout
    val marginHorizontal: Dp,
    val marginVertical: Dp,
    val contentPadding: Dp,
    val paneSpacing: Dp,
    // Icons
    val iconSmall: Dp,
    val iconMedium: Dp,
    val iconLarge: Dp,
    val iconExtraLarge: Dp,
    // Components
    val componentSmall: Dp,
    val componentMedium: Dp,
    val componentLarge: Dp,
    val componentExtraLarge: Dp,
    // Touch targets — intentionally not scaled; see Touch kdoc above
    val touchTarget: Dp,
    val pointerTarget: Dp,
    // Corners
    val cornerNone: Dp,
    val cornerExtraSmall: Dp,
    val cornerSmall: Dp,
    val cornerMedium: Dp,
    val cornerLarge: Dp,
    val cornerExtraLarge: Dp,
    val cornerFull: Dp,
    // Borders
    val borderThin: Dp,
    val borderMedium: Dp,
    val borderThick: Dp,
    // Elevation
    val elevationNone: Dp,
    val elevationLevel1: Dp,
    val elevationLevel2: Dp,
    val elevationLevel3: Dp,
    val elevationLevel4: Dp,
    val elevationLevel5: Dp,
)

// ─── Scale definitions ────────────────────────────────────────────────────────
// Three tiers that mirror M3's Compact / Medium / Expanded breakpoints.
//
//  Compact  < 600dp   → phone portrait, small foldable inner screen
//  Medium   600–840dp → phone landscape, small tablet, foldable outer screen
//  Expanded ≥ 840dp   → large tablet, desktop window
//
// Design decisions:
//  • Spacing tokens themselves do NOT change between tiers — the 4dp grid is
//    universal. Only layout-level values (margins, pane gaps, content padding)
//    grow so that the extra real-estate is used for breathing room, not noise.
//  • Icons grow one step on Expanded because larger canvases make 24dp icons
//    look proportionally small.
//  • Touch targets are fixed at 48dp on all tiers (accessibility requirement).
//  • Corners stay constant — consistent shape language across window sizes is
//    a M3 principle; don't scale corners with screen size.
//  • Elevation stays constant — shadow/tint semantics are window-size-agnostic.
// ─────────────────────────────────────────────────────────────────────────────

private val CompactScale = DimScale(
    // Spacing
    spacingNone = 0.dp,
    spacingHairline = 2.dp,
    spacingExtraSmall = 4.dp,
    spacingSmall = 8.dp,
    spacingMedium = 12.dp,
    spacingLarge = 16.dp,
    spacingExtraLarge = 24.dp,
    spacingHuge = 32.dp,
    spacingMassive = 48.dp,
    spacingColossal = 64.dp,
    // Layout — M3 specifies 16dp horizontal margin for compact screens
    marginHorizontal = 16.dp,
    marginVertical = 16.dp,
    contentPadding = 16.dp,
    paneSpacing = 0.dp,         // single-pane; no inter-pane gap
    // Icons
    iconSmall = 16.dp,
    iconMedium = 24.dp,
    iconLarge = 32.dp,
    iconExtraLarge = 40.dp,
    // Components
    componentSmall = 32.dp,
    componentMedium = 40.dp,
    componentLarge = 48.dp,
    componentExtraLarge = 56.dp,
    // Touch targets (never scale down)
    touchTarget = 48.dp,
    pointerTarget = 44.dp,
    // Corners — M3 shape scale (identical across all tiers)
    cornerNone = 0.dp,
    cornerExtraSmall = 4.dp,
    cornerSmall = 8.dp,
    cornerMedium = 12.dp,
    cornerLarge = 16.dp,
    cornerExtraLarge = 28.dp,
    cornerFull = 1000.dp,
    // Borders
    borderThin = 1.dp,
    borderMedium = 2.dp,
    borderThick = 4.dp,
    // Elevation — M3 tonal levels (identical across all tiers)
    elevationNone = 0.dp,
    elevationLevel1 = 1.dp,
    elevationLevel2 = 3.dp,
    elevationLevel3 = 6.dp,
    elevationLevel4 = 8.dp,
    elevationLevel5 = 12.dp,
)

private val MediumScale = CompactScale.copy(
    // Layout — M3 specifies 24dp horizontal margin for medium screens
    marginHorizontal = 24.dp,
    marginVertical = 24.dp,
    contentPadding = 24.dp,
    paneSpacing = 24.dp,        // two-pane gap (list-detail etc.)
)

private val ExpandedScale = MediumScale.copy(
    // Layout — more generous margins on large canvases
    marginHorizontal = 32.dp,
    contentPadding = 32.dp,
    paneSpacing = 32.dp,
    // Icons — scale up one step on large screens for proportional balance
    iconSmall = 20.dp,
    iconMedium = 28.dp,
    iconLarge = 36.dp,
    iconExtraLarge = 48.dp,
)

// ─── Composition local ────────────────────────────────────────────────────────

val LocalDimensions = staticCompositionLocalOf {
    // Sensible default so previews / non-themed usages don't crash.
    // CompactScale matches the most common device class.
    dimScaleToAppDimensions(CompactScale)
}

// ─── Public factory ───────────────────────────────────────────────────────────

@Composable
fun appDimensions(
    windowSizeClass: WindowSizeClass = calculateWindowSizeClass(),
): AppDimensions {
    val scale = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> ExpandedScale
        WindowWidthSizeClass.Medium -> MediumScale
        else -> CompactScale
    }
    return dimScaleToAppDimensions(scale)
}

// ─── MaterialTheme extension ──────────────────────────────────────────────────
// Access tokens anywhere inside a themed composable tree:
//   MaterialTheme.dimens.spacing.large
//   MaterialTheme.dimens.corner.medium
//   MaterialTheme.dimens.elevation.level2

val MaterialTheme.dimens: AppDimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensions.current

// ─── Mapping helper ───────────────────────────────────────────────────────────

private fun dimScaleToAppDimensions(s: DimScale) = AppDimensions(
    spacing = AppDimensions.Spacing(
        none = s.spacingNone,
        hairline = s.spacingHairline,
        extraSmall = s.spacingExtraSmall,
        small = s.spacingSmall,
        medium = s.spacingMedium,
        large = s.spacingLarge,
        extraLarge = s.spacingExtraLarge,
        huge = s.spacingHuge,
        massive = s.spacingMassive,
        colossal = s.spacingColossal,
    ),
    layout = AppDimensions.Layout(
        marginHorizontal = s.marginHorizontal,
        marginVertical = s.marginVertical,
        contentPadding = s.contentPadding,
        paneSpacing = s.paneSpacing,
    ),
    icon = AppDimensions.Icon(
        small = s.iconSmall,
        medium = s.iconMedium,
        large = s.iconLarge,
        extraLarge = s.iconExtraLarge,
    ),
    component = AppDimensions.Component(
        small = s.componentSmall,
        medium = s.componentMedium,
        large = s.componentLarge,
        extraLarge = s.componentExtraLarge,
    ),
    touch = AppDimensions.Touch(
        touch = s.touchTarget,
        pointer = s.pointerTarget,
    ),
    corner = AppDimensions.Corner(
        none = s.cornerNone,
        extraSmall = s.cornerExtraSmall,
        small = s.cornerSmall,
        medium = s.cornerMedium,
        large = s.cornerLarge,
        extraLarge = s.cornerExtraLarge,
        full = s.cornerFull,
    ),
    border = AppDimensions.Border(
        thin = s.borderThin,
        medium = s.borderMedium,
        thick = s.borderThick,
    ),
    elevation = AppDimensions.Elevation(
        none = s.elevationNone,
        level1 = s.elevationLevel1,
        level2 = s.elevationLevel2,
        level3 = s.elevationLevel3,
        level4 = s.elevationLevel4,
        level5 = s.elevationLevel5,
    ),
)