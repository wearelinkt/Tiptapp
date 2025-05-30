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
    @State private var phoneNumber: String = ""
    @Binding var navigationPath: [NavigationPath]
    
    var body: some View {
        ZStack {
            VStack {
                PhoneInputField(phoneNumber: $phoneNumber)
                
                Spacer()
                
                Button(action: {
                    //viewModel.testCurlToEmulator()
                    viewModel.sendSmsVerification(phoneNumber: "+98\(phoneNumber)")
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
                .padding(.horizontal)
                .padding(.bottom, 20)
            }
            .padding()
            .navigationBarTitle("Tiptapp", displayMode: .inline)
            
            if viewModel.viewState == .loading {
                LoadingOverlayView()
            }
        }.onChange(of: viewModel.viewState) { newState in
            if newState == .completed {
                navigationPath.append(.smsVerify)
            }
        }.errorAlert(for: viewModel)
    }
}











