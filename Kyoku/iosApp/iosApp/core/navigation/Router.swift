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

    @discardableResult
    func popTo(
        where predicate: (Destination) -> Bool
    ) -> Bool {
        guard let destinationIndex = path.lastIndex(where: predicate) else {
            return false
        }

        let removalStartIndex = path.index(after: destinationIndex)
        path.removeSubrange(removalStartIndex..<path.endIndex)

        return true
    }
}
