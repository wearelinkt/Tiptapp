//
//  BaseViewModel.swift
//  iosApp
//
//  Created by Ali on 5/30/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation

class BaseViewModel: ObservableObject, ErrorPresentable {
    @Published var viewState: ViewState = .idle
}
