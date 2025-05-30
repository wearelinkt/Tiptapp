//
//  ErrorAlertModifier.swift
//  iosApp
//
//  Created by Ali on 5/30/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct ErrorAlertModifier<ViewModel: ErrorPresentable>: ViewModifier {
    @ObservedObject var viewModel: ViewModel

    func body(content: Content) -> some View {
        content
            .alert("Error", isPresented: viewModel.showErrorBinding) {
                Button("OK", role: .cancel) {}
            } message: {
                Text(viewModel.errorMessage)
            }
    }
}

extension View {
    func errorAlert<ViewModel: ErrorPresentable>(for viewModel: ViewModel) -> some View {
        self.modifier(ErrorAlertModifier(viewModel: viewModel))
    }
}
