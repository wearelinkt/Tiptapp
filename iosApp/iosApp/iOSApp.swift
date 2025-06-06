import SwiftUI
import FirebaseCore

enum NavigationPath: Hashable {
    case login
    case smsVerify
    case composeView
}

@main
struct iOSApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    @State private var navigationPaths = [NavigationPath]()
    
    var body: some Scene {
        WindowGroup {
            NavigationStack(path: $navigationPaths) {
                ComposeView()
                    .navigationDestination(for: NavigationPath.self) { path in
                        switch path {
                        case .login:
                            LoginView(viewModel: .init(), navigationPath: $navigationPaths)
                        case .smsVerify:
                            SMSCodeView(viewModel: .init(), navigationPath: $navigationPaths)
                        case .composeView:
                            ComposeView().navigationBarBackButtonHidden(true)
                                .navigationBarTitleDisplayMode(.inline)
                        }
                    }
            }
        }
    }
}
