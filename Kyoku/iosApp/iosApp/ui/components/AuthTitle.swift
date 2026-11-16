import SwiftUI

/// Auth screen title, same as Compose `AuthTypeTitle`: a large italic initial followed by the rest of
/// the title at a smaller size, both in `primary`, with a short rule underneath.
struct AuthTitle: View {
    let initial: String
    let rest: String

    /// Compose sizes the rule from the `displayLarge` font size (57); scale it with the text.
    @ScaledMetric(relativeTo: .largeTitle) private var ruleWidth: CGFloat = 57

    var body: some View {
        VStack(alignment: .leading, spacing: AppDimens.Spacing.hairline) {
            // One `Text` so VoiceOver reads the title as a single word.
            Text("\(Text(initial).font(.displayItalicLarge).tracking(-0.25))\(Text(rest).font(.displayItalicSmall))")
                .accessibilityAddTraits(.isHeader)

            Capsule()
                .frame(width: ruleWidth, height: AppDimens.Spacing.hairline)
                .accessibilityHidden(true)
        }
        .foregroundStyle(AppColors.primary)
    }
}

#Preview {
    AuthTitle(initial: "S", rest: "ign in.")
        .padding()
        .background(AppColors.background)
}
