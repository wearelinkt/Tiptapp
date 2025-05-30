//
//  ErrorPresentable.swift
//  iosApp
//
//  Created by Ali on 5/30/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import SwiftUI

protocol ErrorPresentable: ObservableObject {
    var viewState: ViewState { get set }
}

extension ErrorPresentable {
    var showErrorBinding: Binding<Bool> {
        Binding(
            get: {
                if case .failure = self.viewState {
                    return true
                }
                return false
            },
            set: { newValue in
                if !newValue {
                    self.viewState = .idle
                }
            }
        )
    }

    var errorMessage: String {
        if case .failure(let error) = viewState {
            return error.localizedDescription
        }
        return ""
    }
}
