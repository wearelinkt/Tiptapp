//
//  LoadingOverlayView.swift
//  iosApp
//
//  Created by Ali on 5/30/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct LoadingOverlayView: View {
    var body: some View {
        ZStack {
            Color.black.opacity(0.4)
                .ignoresSafeArea()
            
            ProgressView()
                .padding()
                .background(Color.white)
                .cornerRadius(12)
                .shadow(radius: 4)
                .tint(.blue)
        }
    }
}
