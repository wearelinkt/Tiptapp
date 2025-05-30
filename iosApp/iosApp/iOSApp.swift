import SwiftUI
import FirebaseCore

enum NavigationPath: Hashable {
    case login
    case smsVerify
}

@main
struct iOSApp: App {
    
    @State private var navigationPaths = [NavigationPath]()
    
    init() {
        FirebaseApp.configure()
    }
    
    var body: some Scene {
        WindowGroup {
            NavigationStack(path: $navigationPaths) {
                LoginView(viewModel: .init(), navigationPath: $navigationPaths)
                    .navigationDestination(for: NavigationPath.self) { path in
                        switch path {
                        case .login:
                            LoginView(viewModel: .init(), navigationPath: $navigationPaths)
                        case .smsVerify:
                            SMSCodeView(viewModel: .init())
                        }
                    }
            }
        }
    }
}
