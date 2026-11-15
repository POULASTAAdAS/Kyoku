import SwiftUI
import UIKit

/// Color roles shared with Compose (`common/ui/.../design_system/Colors.kt`). Values must stay in
/// sync with that file; the names follow the M3 roles so both platforms speak the same language.
/// Each color adapts to light / dark through `UITraitCollection`, like a color asset would.
enum AppColors {
    static let primary = dynamic(light: 0xD97706, dark: 0xE8A820)
    static let onPrimary = dynamic(light: 0xFFFFFF, dark: 0x1A0C00)
    static let primaryContainer = dynamic(light: 0xFFE4B8, dark: 0x4A3600)
    static let onPrimaryContainer = dynamic(light: 0x462400, dark: 0xFFDF9E)

    static let secondary = dynamic(light: 0x6A5844, dark: 0xB8A890)
    static let onSecondary = dynamic(light: 0xFFFFFF, dark: 0x201810)
    static let secondaryContainer = dynamic(light: 0xF5E0CC, dark: 0x322820)
    static let onSecondaryContainer = dynamic(light: 0x4A3828, dark: 0xD8C8B8)

    static let tertiary = dynamic(light: 0x8C4A46, dark: 0xC49490)
    static let onTertiary = dynamic(light: 0xFFFFFF, dark: 0x1E1010)
    static let tertiaryContainer = dynamic(light: 0xFFD9D6, dark: 0x481E1C)
    static let onTertiaryContainer = dynamic(light: 0x5A1E1C, dark: 0xE8C4C0)

    static let error = dynamic(light: 0xBA1A1A, dark: 0xFFB4AB)
    static let onError = dynamic(light: 0xFFFFFF, dark: 0x690005)
    static let errorContainer = dynamic(light: 0xFFDAD6, dark: 0x93000A)
    static let onErrorContainer = dynamic(light: 0x93000A, dark: 0xFFDAD6)

    static let background = dynamic(light: 0xFFF1E9, dark: 0x0E0E0B)
    static let onBackground = dynamic(light: 0x1E1710, dark: 0xEDE4D6)
    static let surface = dynamic(light: 0xFFF1E9, dark: 0x0E0E0B)
    static let onSurface = dynamic(light: 0x1E1710, dark: 0xEDE4D6)
    static let surfaceVariant = dynamic(light: 0xEEE0D0, dark: 0x382E26)
    static let onSurfaceVariant = dynamic(light: 0x4A3C32, dark: 0xC8B8A6)
    static let outline = dynamic(light: 0x7A6558, dark: 0x9A8878)
    static let outlineVariant = dynamic(light: 0xD2BFAD, dark: 0x382E26)

    static let surfaceContainerLowest = dynamic(light: 0xFFFFFF, dark: 0x090906)
    static let surfaceContainerLow = dynamic(light: 0xFEEDE0, dark: 0x171410)
    static let surfaceContainer = dynamic(light: 0xF8E6D8, dark: 0x1C1812)
    static let surfaceContainerHigh = dynamic(light: 0xF2E0D0, dark: 0x272219)
    static let surfaceContainerHighest = dynamic(light: 0xECDACC, dark: 0x332C24)

    private static func dynamic(light: UInt32, dark: UInt32) -> Color {
        Color(uiColor: UIColor { traits in
            UIColor(rgb: traits.userInterfaceStyle == .dark ? dark : light)
        })
    }
}

private extension UIColor {
    convenience init(rgb: UInt32) {
        self.init(
            red: CGFloat((rgb >> 16) & 0xFF) / 255,
            green: CGFloat((rgb >> 8) & 0xFF) / 255,
            blue: CGFloat(rgb & 0xFF) / 255,
            alpha: 1
        )
    }
}
