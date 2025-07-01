//
//  SplashView.swift
//  iosApp
//
//  Created by Ali on 6/12/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct SplashView: View {
    @ObservedObject var viewModel: PhoneAuthViewModel
    @ObservedObject var monitor: Monitor
    @Binding var navigationPath: [NavigationPath]
    
    var body: some View {
        ZStack {
            switch monitor.status {
            case .connected:
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle())
            case .disconnected:
                Text("Check your network connection")
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .task {
            if monitor.status == .connected {
                await viewModel.userExist()
                if viewModel.isUserExist {
                    navigationPath.append(.composeView)
                } else {
                    navigationPath.append(.login)
                }
            }
        }
    }
}
