import SwiftUI

/// Spacing, size and shape tokens shared with Compose (`common/ui/.../design_system/Dimen.kt`),
/// compact window class. Points here equal dp there.
enum AppDimens {
    enum Spacing {
        static let hairline: CGFloat = 2
        static let extraSmall: CGFloat = 4
        static let small: CGFloat = 8
        static let medium: CGFloat = 12
        static let large: CGFloat = 16
        static let extraLarge: CGFloat = 24
        static let huge: CGFloat = 32
        static let massive: CGFloat = 48
        static let colossal: CGFloat = 64
    }

    enum Layout {
        static let contentPadding: CGFloat = 16
    }

    enum Icon {
        static let small: CGFloat = 16
        static let medium: CGFloat = 24
        static let large: CGFloat = 32
        static let extraLarge: CGFloat = 40
    }

    enum Component {
        static let small: CGFloat = 32
        static let medium: CGFloat = 40
        static let large: CGFloat = 48
        static let extraLarge: CGFloat = 56
        /// Height of buttons and text field rows: 85% of the system `.large` button + 8pt padding (~58pt).
        static let control: CGFloat = 49
    }

    enum Corner {
        static let extraSmall: CGFloat = 4
        static let small: CGFloat = 8
        static let medium: CGFloat = 12
        static let large: CGFloat = 16
        static let extraLarge: CGFloat = 28
    }

    enum Border {
        static let thin: CGFloat = 1
        static let medium: CGFloat = 2
        static let thick: CGFloat = 4
    }
}

/// Shape tokens; `.continuous` matches the superellipse `SuperellipseShape` used on Android.
enum AppShapes {
    static let extraSmall = RoundedRectangle(cornerRadius: AppDimens.Corner.extraSmall, style: .continuous)
    static let small = RoundedRectangle(cornerRadius: AppDimens.Corner.small, style: .continuous)
    static let medium = RoundedRectangle(cornerRadius: AppDimens.Corner.medium, style: .continuous)
    static let large = RoundedRectangle(cornerRadius: AppDimens.Corner.large, style: .continuous)
    static let extraLarge = RoundedRectangle(cornerRadius: AppDimens.Corner.extraLarge, style: .continuous)
}
