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
    @Binding var navigationPath: [NavigationPath]
    
    var body: some View {
        ZStack {
            ProgressView()
                .progressViewStyle(CircularProgressViewStyle())
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .task {
            await viewModel.userExist()
            if viewModel.isUserExist {
                navigationPath.append(.composeView)
            } else {
                navigationPath.append(.login)
            }
        }
    }
}
