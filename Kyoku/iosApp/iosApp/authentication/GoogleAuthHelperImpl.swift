import ComposeApp
//
//  GoogleAuthHelperImpl.swift
//  iosApp
//
//  Created by Poulastaa Das on 26/07/26.
//
import GoogleSignIn
import UIKit

final class GoogleAuthHelperImpl: GoogleAuthWrapper {
    var onResult: ((GoogleAuthResult) -> Void)?

    func startGoogleAuth() {
        guard let viewController = UIApplication.shared.keyWindowPresentedController else { return }
        guard let clientId = Bundle.main.object(forInfoDictionaryKey: "GIDClientID") as? String else { return }

        GIDSignIn.sharedInstance.configuration = GIDConfiguration(clientID: clientId)
        GIDSignIn.sharedInstance.signIn(withPresenting: viewController) { [self] result, error in
            if let error {
                print("Google Authentication Error: \(error.localizedDescription)")
                self.onResult?(
                    GoogleAuthResult.Error(
                        exception: KotlinException(message: error.localizedDescription)
                    )
                )
                return
            }

            guard let idToken = result?.user.idToken?.tokenString else {
                print("Google Authentication Error: Token retrieval failed")
                self.onResult?(
                    GoogleAuthResult.Error(
                        exception: KotlinException(message: "Failed to get ID token")
                    )
                )
                return
            }

            guard let onResult = self.onResult else { return }
            onResult(GoogleAuthResult.Success(token: idToken))
        }
    }
}
