//
//  LoginView.swift
//  iosApp
//
//  Created by Ali on 5/26/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct LoginView: View {
    @ObservedObject var viewModel: PhoneAuthViewModel
    @Binding var navigationPath: [NavigationPath]
    
    private var isValidPhone: Bool {
        viewModel.phoneNumber.count >= 9 && viewModel.phoneNumber.count <= 10
    }
    
    var body: some View {
        AsyncContentView2(viewModel: viewModel) {
            VStack {
                PhoneInputField(phoneNumber: $viewModel.phoneNumber)
                
                Spacer()
                
                Button(action: {
                    viewModel.sendSmsVerification(phoneNumber: "+98\(viewModel.phoneNumber)")
                }) {
                    Text("Continue")
                        .font(.headline)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(36)
                        .shadow(radius: 2)
                }
                .disabled(!isValidPhone)
                .opacity(isValidPhone ? 1 : 0.5)
                .padding(.horizontal)
                .padding(.bottom, 20)
            }
            .padding()
            .navigationBarTitle("Tiptapp", displayMode: .inline)
        } navigate: {
            navigationPath.append(.smsVerify)
        }
    }
}











