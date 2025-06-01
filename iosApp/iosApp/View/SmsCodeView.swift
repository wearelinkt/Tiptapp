//
//  SmsCodeView.swift
//  iosApp
//
//  Created by Ali on 5/30/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct SMSCodeView: View {
    @State private var code: [String] = Array(repeating: "", count: 6)
    @FocusState private var focusedIndex: Int?
    @ObservedObject var viewModel: PhoneAuthViewModel
    @Binding var navigationPath: [NavigationPath]
    
    var body: some View {
        AsyncContentView2(viewModel: viewModel) {
            VStack {
                VStack {
                    HStack(spacing: 12) {
                        ForEach(0..<6, id: \.self) { index in
                            TextField("", text: Binding<String>(
                                get: {
                                    return code[index]
                                },
                                set: { newValue in
                                    if newValue.count <= 1,
                                       newValue.allSatisfy(\.isNumber) || newValue.isEmpty {
                                        code[index] = newValue
                                        
                                        if newValue.count == 1 {
                                            if index < 5 {
                                                focusedIndex = index + 1
                                            }
                                            
                                            let currentCode = code.joined()
                                            if isCodeComplete && currentCode != viewModel.lastSubmittedCode {
                                                focusedIndex = nil
                                                viewModel.lastSubmittedCode = currentCode
                                                viewModel.verifySms(verificationCode: currentCode)
                                            }
                                        }
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
                    }
                    .onAppear {
                        focusedIndex = 0
                    }
                    .padding(.top, 40)
                }
                Spacer()
            }
            .navigationBarTitle("Enter Code", displayMode: .inline)
        } navigate: {
            navigationPath.append(.composeView)
        }
    }
    
    private var isCodeComplete: Bool {
        !code.contains(where: { $0.isEmpty })
    }
}
