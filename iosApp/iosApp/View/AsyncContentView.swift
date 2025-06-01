//
//  AsyncContentView.swift
//  iosApp
//
//  Created by Ali on 5/27/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import SwiftUI

enum ViewState: Equatable {
    case idle
    case loading
    case completed
    case failure(error: Error)
    
    static func == (lhs: ViewState, rhs: ViewState) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle): return true
        case (.completed, .completed): return true
        case (.loading, .loading): return true
        case (.failure(error: _), .failure(error: _)):
            return true
        default: return false
        }
    }
    
}

struct AsyncContentView<Content: View>: View {
    
    var viewState: ViewState
    let content: () -> Content
    let onRetry: ()  -> Void
    
    var body: some View {
        switch viewState {
        case .idle:
            EmptyView()
        case .loading:
            ProgressView()
        case .completed:
            AnyView(content())
        case .failure(let error):
            VStack(spacing: 20) {
                Text(error.localizedDescription)
                Button(action: {
                    onRetry()
                }, label: {
                    Text("Retry")
                })
            }
        }
    }
}

struct AsyncContentView2<Content: View>: View {
    
    let viewModel: BaseViewModel
    let content: () -> Content
    let navigate: ()  -> Void
    
    var body: some View {
        ZStack {
            AnyView(content())
            if viewModel.viewState == .loading {
                LoadingOverlayView()
            }
        }.onChange(of: viewModel.viewState) { newState in
            if newState == .completed {
                navigate()
            }
        }.errorAlert(for: viewModel)
    }
}
