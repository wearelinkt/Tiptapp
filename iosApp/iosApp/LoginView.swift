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
    @State private var showCompose = false
    @State private var phoneNumber: String = ""

    var body: some View {
        VStack {
            if showCompose {
                ComposeView()
            } else {
                VStack {
                    PhoneInputField(phoneNumber: $phoneNumber)
                    
                    Spacer()
                    
                    Button(action: {
                        showCompose = true
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
            }
        }
        
        var fullPhoneNumber: String {
            "+98\(phoneNumber)"
        }
    }
}
