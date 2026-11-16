import XCTest

/// Forgot password: an invalid email must show the validation error on the first tap of "Get OTP".
final class ForgotPasswordUITests: XCTestCase {
    private var app: XCUIApplication!

    override func setUpWithError() throws {
        continueAfterFailure = false
        app = XCUIApplication()
        app.launch()
    }

    func testInvalidEmailShowsErrorOnFirstTap() {
        let forgotPassword = app.buttons["Forgot password?"]
        XCTAssertTrue(forgotPassword.waitForExistence(timeout: 10), "Sign-in screen not shown")
        forgotPassword.tap()

        let email = app.textFields["Email"]
        XCTAssertTrue(email.waitForExistence(timeout: 5), "Forgot-password screen not shown")
        email.tap()
        email.typeText("someone@gmai.")

        // Keyboard stays up, same as the manual repro.
        app.buttons["Get OTP"].tap()

        XCTAssertTrue(
            app.staticTexts["Invalid email"].waitForExistence(timeout: 3),
            "Error not shown after the first tap"
        )
    }
}
