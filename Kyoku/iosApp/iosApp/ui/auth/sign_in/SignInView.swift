import ComposeApp
import SwiftUI

/// Sign-in screen. All state and business logic live in the shared `SignInViewmodel`; this view
/// only renders `viewModel.state`, forwards user input as `SignInUiAction`s, and maps
/// `SignInUiEvent`s onto the navigation `path` owned by the auth `NavigationStack`.
struct SignInView: View {
    @Binding var path: [AuthRoute]

    @StateObject private var viewModel = SignInViewModel()
    @EnvironmentObject private var commonUi: CommonUiStateObserver
    @Environment(\.verticalSizeClass) private var verticalSizeClass
    @FocusState private var focusedField: Field?

    private let googleAuth = IosViewModelProviderKt.googleAuthWrapper()

    private enum Field: Hashable {
        case email, password
    }

    private var state: SignInUiState { viewModel.state }
    private var isBusy: Bool {
        state.isMakingApiCall || state.isGoogleAuthInProgress
    }

    /// Validation messages for the credential section footer. API error text passes through as-is.
    private var credentialErrors: [String] {
        [state.email, state.password]
            .filter(\.isError)
            .compactMap(\.errorMessage)
    }

    /// Shared success / info message (e.g. "verification mail sent"), shown only while the sign-in
    /// it belongs to is still running so it never outlives its context.
    private var signInStatus: String? {
        state.isMakingApiCall ? commonUi.status?.message : nil
    }

    var body: some View {
        // ZStack (not Group) so onAppear/onDisappear stay on one container and rotating doesn't
        // re-run them, which would clear the Google callback mid-flow.
        ZStack {
            if verticalSizeClass == .compact {
                // Phone landscape, same structure as Compose `CompatHorizontalSignInScreen`:
                // actions on the left (40%), title + credentials on the right.
                HStack(spacing: AppDimens.Spacing.medium) {
                    actionButtons
                        .padding(.leading, AppDimens.Layout.contentPadding)
                        .containerRelativeFrame(.horizontal) { width, _ in
                            width * 0.4
                        }
                        .frame(maxHeight: .infinity)
                        // Only the form moves for the keyboard; the actions column stays put.
                        .ignoresSafeArea(.keyboard)

                    credentialForm(isLandscape: true)
                }
            } else {
                credentialForm(isLandscape: false)
                    .safeAreaInset(edge: .bottom) { actions }
            }
        }
        .background(AppColors.background)
        .tint(AppColors.primary)
        // Footer errors and the status row ease in and out instead of jumping the layout.
        .animation(.snappy, value: credentialErrors)
        .animation(.snappy, value: signInStatus)
        .sensoryFeedback(.selection, trigger: state.isPasswordVisible)
        // Error haptic only when errors appear, not when they clear.
        .sensoryFeedback(.error, trigger: credentialErrors) { _, errors in
            !errors.isEmpty
        }
        .onChange(of: focusedField) { _, field in
            if field != nil { viewModel.credentialFieldFocused() }
        }
        .onChange(of: state.isGoogleAuthInProgress) { _, inProgress in
            if inProgress { googleAuth.startGoogleAuth() }
        }
        .onAppear(perform: bind)
        .onDisappear { googleAuth.onResult = nil }
    }

    // MARK: - Sections

    /// Form gives the inset-grouped cells, separators, footer and keyboard avoidance that iOS
    /// credential screens use; the header rides inside it as a plain row so it scrolls away.
    /// Landscape drops the logo and eyebrow to save height, matching Compose, and fits without scrolling.
    private func credentialForm(isLandscape: Bool) -> some View {
        Form {
            Section {
                header(isCompact: isLandscape)
            }
            .listRowBackground(Color.clear)
            .listRowInsets(EdgeInsets())
            // Default inset-grouped spacing (~35pt) + the header's 8pt bottom padding read as too loose;
            // 26pt brings the title → email gap to ~80% of the default (34 vs 43).
            .listSectionSpacing(26)

            Section {
                emailField
                passwordField
            } footer: {
                // Errors first, like a system form footer; the link sits under them.
                VStack(alignment: .leading, spacing: AppDimens.Spacing.small) {
                    AuthSectionFooter(messages: credentialErrors)
                    forgotPassword
                }
            }
            // Separator starts under the leading icon.
            .alignmentGuide(.listRowSeparatorLeading) { $0[.leading] }
        }
        // Field rows match the action buttons.
        .environment(\.defaultMinListRowHeight, AppDimens.Component.control)
        .scrollContentBackground(.hidden)
        .scrollDismissesKeyboard(.interactively)
        .scrollDisabled(isLandscape)
    }

    private func header(isCompact: Bool) -> some View {
        // Same gap as Compose `CompatVerticalSignInScreen` between the eyebrow and the title.
        VStack(alignment: .leading, spacing: AppDimens.Spacing.large) {
            if !isCompact {
                Image("KyokuSplashLogo")
                    .renderingMode(.template)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 92, height: 92)
                    .frame(maxWidth: .infinity)
                    .foregroundStyle(AppColors.primary)
                    .accessibilityLabel("Kyoku")
                    .padding(.vertical, AppDimens.Spacing.large)

                Text("Welcome back")
                    .appTextStyle(.titleSmall)
                    // Same letter spacing as Compose `CompatVerticalSignInScreen` (4.sp).
                    .tracking(4)
                    .foregroundStyle(AppColors.primary)
                    .textCase(.uppercase)
            }

            AuthTitle(initial: "S", rest: "ign in.")
        }
        // The row has zero insets and list cells clip, so without this the "g" descender is cut off.
        .padding(.bottom, AppDimens.Spacing.small)
    }

    private var forgotPassword: some View {
        Button("Forgot password?") {
            viewModel.forgotPasswordClicked(email: state.email.value)
        }
        .appTextStyle(.labelLarge)
        .disabled(isBusy)
        .frame(maxWidth: .infinity, alignment: .trailing)
    }

    private var emailField: some View {
        AuthTextField(
            label: "Email",
            systemImage: "envelope",
            value: state.email.value,
            onChange: viewModel.emailChanged,
            isError: state.email.isError,
            field: .email,
            focus: $focusedField
        )
        .textContentType(.username)
        .keyboardType(.emailAddress)
        .textInputAutocapitalization(.never)
        .autocorrectionDisabled()
        .submitLabel(.next)
        .onSubmit { focusedField = .password }
    }

    private var passwordField: some View {
        AuthTextField(
            label: "Password",
            systemImage: "lock",
            value: state.password.value,
            onChange: viewModel.passwordChanged,
            isError: state.password.isError,
            field: .password,
            focus: $focusedField,
            isSecure: true,
            isSecureTextVisible: state.isPasswordVisible,
            onToggleSecureTextVisibility: viewModel.togglePasswordVisibility
        )
        .textContentType(.password)
        .submitLabel(.go)
        .onSubmit(signIn)
    }

    /// Bottom action bar; stays above the keyboard and home indicator while the form scrolls.
    private var actions: some View {
        actionButtons
            .padding(.horizontal, AppDimens.Layout.contentPadding)
            .padding(.top, AppDimens.Spacing.medium)
            .padding(.bottom, AppDimens.Spacing.small)
            .frame(maxWidth: 480)  // readable width on iPad
            .frame(maxWidth: .infinity)
            .background(AppColors.background)
    }

    private var actionButtons: some View {
        VStack(spacing: AppDimens.Spacing.medium) {
            if let signInStatus {
                Label(signInStatus, systemImage: "envelope.badge")
                    .appTextStyle(.bodySmall)
                    .foregroundStyle(.secondary)
                    .multilineTextAlignment(.center)
                    .transition(.opacity.combined(with: .move(edge: .bottom)))
            }

            AuthActionButton(
                isLoading: state.isMakingApiCall,
                isEnabled: !isBusy,
                action: signIn
            ) {
                Text("Sign In")
                    .appTextStyle(.titleMedium)
            }

            Text("or continue with")
                .appTextStyle(.bodySmall)
                .foregroundStyle(.secondary)

            AuthActionButton(
                role: .secondary,
                isLoading: state.isGoogleAuthInProgress,
                isEnabled: !isBusy,
                action: viewModel.googleSignInClicked
            ) {
                Label {
                    Text("Continue with Google")
                        .appTextStyle(.titleMedium)
                } icon: {
                    Image("GoogleLogo")
                        .resizable()
                        .scaledToFit()
                        .frame(
                            width: AppDimens.Icon.small,
                            height: AppDimens.Icon.small
                        )
                }
                .foregroundStyle(AppColors.onSurface)
            }

            createAccount
                .padding(.top, AppDimens.Spacing.extraSmall)
        }
    }

    private var createAccount: some View {
        HStack(spacing: AppDimens.Spacing.extraSmall) {
            Text("Don't have an account?")
                .foregroundStyle(.secondary)

            Button("Create Account") {
                viewModel.createAccountClicked()
            }
            .fontWeight(.semibold)
            .disabled(isBusy)
        }
        .appTextStyle(.labelLarge)
        .frame(maxWidth: .infinity)
    }

    // MARK: - Wiring

    private func signIn() {
        focusedField = nil
        viewModel.signIn()
    }

    /// Subscribes to one-off viewmodel events and to the Google sign-in result. Called from
    /// `onAppear` so the subscriptions are re-established when navigating back to this screen.
    private func bind() {
        googleAuth.onResult = viewModel.googleAuthFinished

        viewModel.onEvent { event in
            // Exhaustive (SKIE): a new Kotlin event fails the Swift build instead of being ignored.
            switch onEnum(of: event) {
            case .navigateToForgotPassword(let event):
                path.append(.forgotPassword(email: event.email))
            case .navigateToSignUp:
                path.append(.signUp)
            case .navigateToHome, .navigateToImportPlaylist:
                // Setup and main graphs are not implemented in SwiftUI yet.
                break
            }
        }
    }
}
