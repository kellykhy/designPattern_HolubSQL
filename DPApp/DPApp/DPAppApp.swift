import SwiftUI

@main
struct DPAppApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView(viewModel: OrderViewModel())
        }
    }
}
