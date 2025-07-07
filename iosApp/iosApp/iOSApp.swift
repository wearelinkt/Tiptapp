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
    
    private let networkService: NetworkServiceProtocol = NetworkService()
    
    var body: some Scene {
        WindowGroup {
            NavigationStack(path: $navigationPaths) {
                //SplashView(viewModel: .init(networkService: networkService), monitor: .init(), navigationPath: $navigationPaths)
                    ComposeView()
                    .navigationDestination(for: NavigationPath.self) { path in
                        switch path {
                        case .login:
                            LoginView(viewModel: .init(networkService: networkService), navigationPath: $navigationPaths).navigationBarBackButtonHidden(true)
                        case .smsVerify:
                            SMSCodeView(viewModel: .init(networkService: networkService), navigationPath: $navigationPaths)
                        case .composeView:
                            ComposeView().navigationBarBackButtonHidden(true)
                                .navigationBarTitleDisplayMode(.inline)
                        }
                    }
            }
        }
    }
}
