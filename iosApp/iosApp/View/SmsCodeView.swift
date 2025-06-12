//
//  SmsCodeView.swift
//  iosApp
//
//  Created by Ali on 5/30/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct SMSCodeView: View {
    @ObservedObject var viewModel: PhoneAuthViewModel
    @Binding var navigationPath: [NavigationPath]
    
    @State private var code: [String] = Array(repeating: "", count: 6)
    @FocusState private var focusedIndex: Int?
    @State private var lastSubmittedCode: String = ""
    @State private var autofillCode: String = ""
    
    private var isCodeComplete: Bool {
        !code.contains { $0.isEmpty }
    }
    
    var body: some View {
        AsyncContentView2(viewModel: viewModel) {
            VStack {
                HStack(spacing: 12) {
                    ForEach(0..<6, id: \.self) { index in
                        TextField("", text: Binding<String>(
                            get: {
                                return code[index]
                            },
                            set: { newValue in
                                if newValue.count == 1 {
                                    code[index] = newValue
                                    // Only auto-advance if user typed something and not when setting focus manually
                                    if index < 5 && focusedIndex == index {
                                        focusedIndex = index + 1
                                    }
                                } else {
                                    code[index] = newValue
                                }

                                // Trigger verification only when all fields are filled and it's a new code
                                let currentCode = code.joined()
                                if isCodeComplete && currentCode != lastSubmittedCode {
                                    focusedIndex = nil
                                    lastSubmittedCode = currentCode
                                    viewModel.verifySms(verificationCode: currentCode)
                                }
                            }
                        ))
                        .keyboardType(.numberPad)
                        .textContentType(.oneTimeCode)
                        .frame(width: 44, height: 55)
                        .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                        .multilineTextAlignment(.center)
                        .focused($focusedIndex, equals: index)
                    }
                }.onAppear {
                    focusedIndex = 0
                }.padding(.top, 50)
                
                // Hidden Autofill Field
                TextField("", text: $autofillCode)
                    .textContentType(.oneTimeCode)
                    .keyboardType(.numberPad)
                    .frame(width: 1, height: 1)
                    .opacity(0.01)
                    .disabled(false)
                    .allowsHitTesting(false)
                    .onChange(of: autofillCode) { newCode in
                        let trimmed = String(newCode.prefix(6))
                        for (i, char) in trimmed.enumerated() where i < code.count {
                            code[i] = String(char)
                        }
                        if trimmed.count == 6 && trimmed != lastSubmittedCode {
                            lastSubmittedCode = trimmed
                            focusedIndex = nil
                            viewModel.verifySms(verificationCode: trimmed)
                        }
                    }
                
                Spacer()
            }.navigationBarTitle("Enter Code", displayMode: .inline)
            
        } navigate: {
            navigationPath.append(.composeView)
        }.onChange(of: viewModel.userId) { userId in
            Task {
                await viewModel.registerUser()
            }
        }
    }
}
