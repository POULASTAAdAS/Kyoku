import Observation

/// Owns one graph's `NavigationStack` path. Each graph host creates its own `Router<Route>` and
/// injects it with `.environment(router)`; screens push routes in response to viewmodel events.
@MainActor
@Observable
final class Router<Destination: Route> {
    var path: [Destination] = []

    func push(_ route: Destination) {
        path.append(route)
    }

    func pop() {
        guard !path.isEmpty else { return }
        path.removeLast()
    }

    func popToRoot() {
        path.removeAll()
    }
}
