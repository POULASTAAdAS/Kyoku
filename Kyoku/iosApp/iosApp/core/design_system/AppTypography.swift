import SwiftUI

/// Typography for iOS. Per the HIG, text uses SF Pro through the semantic `Font.TextStyle`s so it
/// scales with Dynamic Type and gets the right leading and tracking automatically.
///
/// The cases keep the M3 names used in `Type.kt` so a screen built on Android can be ported token
/// by token; the mapping below is the HIG style each token corresponds to in role and size.
///
/// Usage: `Text("…").appTextStyle(.titleLarge)` (or `.appTextStyle(.bodyLarge, weight: .semibold)`).
enum AppTextStyle {
    case displayLarge, displayMedium, displaySmall
    case headlineLarge, headlineMedium, headlineSmall
    case titleLarge, titleMedium, titleSmall
    case bodyLarge, bodyMedium, bodySmall
    case labelLarge, labelMedium, labelSmall

    /// HIG text style: Large Title 34, Title 1 28, Title 2 22, Title 3 20, Headline 17 semibold,
    /// Body 17, Callout 16, Subheadline 15, Footnote 13, Caption 1 12, Caption 2 11.
    var textStyle: Font.TextStyle {
        switch self {
        case .displayLarge, .displayMedium, .displaySmall: .largeTitle
        case .headlineLarge: .title
        case .headlineMedium: .title2
        case .headlineSmall: .title3
        case .titleLarge: .title2
        case .titleMedium: .headline
        case .titleSmall: .subheadline
        case .bodyLarge: .body
        case .bodyMedium: .callout
        case .bodySmall: .footnote
        case .labelLarge: .subheadline
        case .labelMedium: .caption
        case .labelSmall: .caption2
        }
    }

    var weight: Font.Weight {
        switch self {
        case .displayLarge, .displayMedium, .displaySmall: .bold
        case .headlineLarge, .headlineMedium, .headlineSmall: .semibold
        case .titleLarge: .bold
        case .titleMedium, .titleSmall: .semibold
        case .bodyLarge, .bodyMedium, .bodySmall: .regular
        case .labelLarge, .labelMedium, .labelSmall: .medium
        }
    }

    /// SF Pro; the system picks the Text / Display optical size from the point size.
    func font(weight: Font.Weight? = nil) -> Font {
        .system(textStyle, weight: weight ?? self.weight)
    }
}

extension View {
    /// Applies a typography token; `weight` overrides the token default.
    func appTextStyle(_ style: AppTextStyle, weight: Font.Weight? = nil) -> some View {
        font(style.font(weight: weight))
    }
}

extension Text {
    /// `Text` concatenation needs a `Font`, not a view modifier; use this for rich spans.
    func appFont(_ style: AppTextStyle, weight: Font.Weight? = nil) -> Text {
        font(style.font(weight: weight))
    }
}
