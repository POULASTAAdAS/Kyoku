import SwiftUI

/// One-time-code input: one fixed-size box per digit, centered, like Compose `OtpField`, drawn in
/// the iOS code-field style (filled continuous-corner cells, accent ring + caret on the active box).
/// A single hidden `TextField` owns the input, so the number pad, `.oneTimeCode` AutoFill from
/// Mail / Messages, paste and VoiceOver behave like a system field; the boxes are only its visual.
/// Apply `.focused(_:equals:)` to this view to drive focus.
struct OtpCodeField: View {
    let code: String
    let length: Int
    let isError: Bool
    let isFocused: Bool
    let onChange: (String) -> Void

    @Environment(\.isEnabled) private var isEnabled
    /// Compose sizes boxes from `headlineLarge` (32): width 1.5x, height text + 12pt padding each side.
    @ScaledMetric(relativeTo: .title) private var digitSize: CGFloat = 32

    var body: some View {
        // UIKit re-sends the unchanged text when focus moves; forwarding it would read as an edit.
        let text = Binding(get: { code }, set: { if $0 != code { onChange($0) } })
        let digits = Array(code)

        HStack(spacing: AppDimens.Spacing.large) {
            ForEach(0..<length, id: \.self) { index in
                box(
                    digit: index < digits.count ? String(digits[index]) : nil,
                    isActive: isFocused && isEnabled && index == min(digits.count, length - 1)
                )
            }
        }
        .accessibilityHidden(true)
        .overlay {
            // Hidden input over the boxes; it takes taps, keyboard, AutoFill and paste.
            TextField("", text: text)
                .keyboardType(.numberPad)
                .textContentType(.oneTimeCode)
                .foregroundStyle(.clear)
                .tint(.clear)
                .accessibilityLabel("Verification code")
                .accessibilityValue(code.isEmpty ? "Empty" : code.map(String.init).joined(separator: " "))
        }
        .opacity(isEnabled ? 1 : 0.5)
        .frame(maxWidth: .infinity)
        .animation(.snappy, value: code)
    }

    private func box(digit: String?, isActive: Bool) -> some View {
        ZStack {
            if let digit {
                Text(digit)
                    .appTextStyle(.headlineLarge)
                    .monospacedDigit()
                    .foregroundStyle(AppColors.onSurface)
                    .contentTransition(.numericText())
            } else if isActive {
                caret
            }
        }
        .frame(width: digitSize * 1.5, height: digitSize + AppDimens.Spacing.medium * 2)
        .background(Color(uiColor: .secondarySystemGroupedBackground), in: AppShapes.medium)
        .overlay {
            if isError || isActive {
                AppShapes.medium.strokeBorder(
                    isError ? AppColors.error : AppColors.primary,
                    lineWidth: AppDimens.Border.medium
                )
            }
        }
    }

    /// Step blink (on/off each half second), like the system caret and Compose's custom cursor.
    private var caret: some View {
        TimelineView(.periodic(from: .now, by: 0.5)) { context in
            Capsule()
                .fill(AppColors.primary)
                .frame(width: AppDimens.Border.medium, height: digitSize)
                .opacity(Int(context.date.timeIntervalSinceReferenceDate * 2) % 2 == 0 ? 1 : 0)
        }
    }
}

#Preview {
    @Previewable @State var code = "12"

    Form {
        Section {
            OtpCodeField(code: code, length: 5, isError: false, isFocused: true) { code = $0 }
        }
        .listRowBackground(Color.clear)
        .listRowInsets(EdgeInsets())

        Section {
            OtpCodeField(code: "12345", length: 5, isError: true, isFocused: false) { _ in }
        }
        .listRowBackground(Color.clear)
        .listRowInsets(EdgeInsets())
    }
    .scrollContentBackground(.hidden)
    .background(AppColors.background)
    .tint(AppColors.primary)
}
