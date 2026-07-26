import UIKit

extension UIApplication {
    var keyWindowPresentedController: UIViewController? {
        var controller = connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap(\.windows)
            .last(where: \.isKeyWindow)?
            .rootViewController

        while let presentedController = controller?.presentedViewController {
            controller = presentedController
        }

        return controller
    }
}
