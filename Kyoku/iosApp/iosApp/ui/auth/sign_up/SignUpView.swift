import ComposeApp
import SwiftUI

struct SignUpView: View {
    let onNavigateToImportPlaylist: () -> Void
    let onNavigateToMain: () -> Void

    @Environment(Router<AuthRoute>.self) private var router

    @StateObject private var viewModel = SignUpViewModel()
    @EnvironmentObject private var commonUi: CommonUiStateObserver
    @Environment(\.verticalSizeClass) private var verticalSizeClass
    private let googleAuth = IosViewModelProviderKt.googleAuthWrapper()

    private enum Field: Hashable {
        case username, email, password
    }

    @FocusState private var focusedField: Field?

    private var state: SingUpUiState { viewModel.state }
    private var isBusy: Bool {
        state.isMakingApiCall || state.isGoogleAuthInProgress
    }

    private var textFieldErrors: [String] {
        [state.username, state.email, state.password]
            .filter(\.isError)
            .compactMap(\.errorMessage)
    }

    private var authStatus: String? {
        state.isMakingApiCall ? commonUi.status?.message : nil
    }

    var body: some View {
        ZStack {
            if verticalSizeClass == .compact {
                verticalView
            } else {
                horizontalView
            }
        }
        .background(AppColors.background)
        .tint(AppColors.primary)
        .floatingBackButton(action: router.pop)
        .animation(.snappy, value: textFieldErrors)
        .animation(.snappy, value: authStatus)
        .sensoryFeedback(.selection, trigger: state.isPasswordVisible)
        .sensoryFeedback(.error, trigger: textFieldErrors) { _, errros in
            !errros.isEmpty
        }
        .onChange(of: state.isGoogleAuthInProgress) { _, inPogress in
            guard inPogress else { return }
            googleAuth.onResult = viewModel.googleAuthFinished
            googleAuth.startGoogleAuth()
        }
        .onAppear(perform: bind)
        .onDisappear { googleAuth.onResult = nil }
    }

    var verticalView: some View {
        HStack(spacing: AppDimens.Spacing.medium) {
            actionButtons
                .padding(.leading, AppDimens.Layout.contentPadding)
                .containerRelativeFrame(.horizontal) { width, _ in
                    width * 0.4
                }
                .frame(maxHeight: .infinity)
                // Only the form moves for the keyboard; the actions column stays put.
                .ignoresSafeArea(.keyboard)

            inputSection(isCompact: true)
        }
    }

    private var horizontalView: some View {
        inputSection(isCompact: false)
            .safeAreaInset(edge: .bottom) {
                actionButtons
                    .padding(.horizontal, AppDimens.Layout.contentPadding)
                    .padding(.top, AppDimens.Spacing.medium)
                    .padding(.bottom, AppDimens.Spacing.small)
                    .frame(maxWidth: 400)
                    .frame(maxWidth: .infinity)
            }
    }

    // MARK: - Sections

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

                Text("Listen to your heart's content")
                    .appTextStyle(.bodyMedium)
                    // Same letter spacing as Compose `CompatVerticalSignInScreen` (4.sp).
                    .tracking(2)
                    .frame(maxWidth: .infinity, alignment: .center)
                    .foregroundStyle(AppColors.primary)
                    .textCase(.uppercase)
            }

            AuthTitle(initial: "S", rest: "ign up.")
        }
        // The row has zero insets and list cells clip, so without this the "g" descender is cut off.
        .padding(.bottom, AppDimens.Spacing.small)
    }

    private var usernameField: some View {
        AuthTextField(
            label: "Username",
            systemImage: "person",
            value: state.username.value,
            onChange: viewModel.usernameChanged,
            isError: state.username.isError,
            field: .username,
            focus: $focusedField
        )
        .textContentType(.username)
        .keyboardType(.default)
        .textInputAutocapitalization(.none)
        .autocorrectionDisabled()
        .submitLabel(.next)
        .onSubmit { focusedField = .email }
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
        .autocorrectionDisabled()
        .submitLabel(.go)
        .onSubmit(signUp)
    }

    private func inputSection(isCompact: Bool) -> some View {
        Form {
            Section {
                header(isCompact: isCompact)
            }
            .listRowBackground(Color.clear)
            .listRowInsets(EdgeInsets())
            .listSectionSpacing(26)

            Section {
                usernameField
                emailField
                passwordField
            } footer: {
                VStack(
                    alignment: .leading,
                    spacing: AppDimens.Spacing.small
                ) {
                    AuthSectionFooter(messages: textFieldErrors)
                }
            }
        }
        .environment(\.defaultMinListRowHeight, AppDimens.Component.control)
        .scrollContentBackground(.hidden)
        .scrollDismissesKeyboard(.interactively)
        .scrollDisabled(true)
    }

    private var actionButtons: some View {
        VStack(spacing: AppDimens.Spacing.medium) {
            toast(authStatus: authStatus)
            submitButton
            Text("or continue with")
                .appTextStyle(.bodySmall)
                .foregroundStyle(.secondary)
            googleAuthButton
            loginAccount
        }
    }

    private var loginAccount: some View {
        HStack(spacing: AppDimens.Spacing.extraSmall) {
            Text("Already have an account?")
                .foregroundStyle(.secondary)

            Button("LogIn") {
                viewModel.loginClicked()
            }
            .fontWeight(.semibold)
            .disabled(isBusy)
        }
        .appTextStyle(.labelLarge)
        .frame(maxWidth: .infinity)
    }

    private var submitButton: some View {
        AuthActionButton(
            isLoading: state.isMakingApiCall,
            isEnabled: !isBusy,
            action: signUp,
            label: {
                Text("Sign Up")
                    .appTextStyle(.titleMedium)
            }
        )
    }

    private var googleAuthButton: some View {
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
    }

    @ViewBuilder
    private func toast(authStatus: String?) -> some View {
        if let authStatus {
            Label(authStatus, systemImage: "envelope.badge")
                .appTextStyle(.bodySmall)
                .foregroundStyle(.secondary)
                .multilineTextAlignment(.center)
                .transition(.opacity.combined(with: .move(edge: .bottom)))
        }
    }

    // MARK: - Wiring

    private func signUp() {
        focusedField = nil
        viewModel.signUp()
    }

    private func bind() {
        googleAuth.onResult = viewModel.googleAuthFinished

        viewModel.onEvent { event in
            switch onEnum(of: event) {
            case .navigateToHome:
                onNavigateToMain()
            case .navigateToImportPlaylist:
                onNavigateToImportPlaylist()
            case .navigateToLogIn:
                router.popToRoot()
            }
        }
    }
}
