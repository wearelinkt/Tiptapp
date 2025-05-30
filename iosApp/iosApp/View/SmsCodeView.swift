//
//  SmsCodeView.swift
//  iosApp
//
//  Created by Ali on 5/30/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct SMSCodeView: View {
    @State private var showCompose = false
    @State private var code: [String] = Array(repeating: "", count: 6)
    @FocusState private var focusedIndex: Int?
    @ObservedObject var viewModel: PhoneAuthViewModel
    
    var body: some View {
        if showCompose {
            ComposeView()
        } else {
            ZStack {
                VStack {
                    VStack {
                        HStack(spacing: 12) {
                            ForEach(0..<6, id: \.self) { index in
                                TextField("", text: Binding<String>(
                                    get: {
                                        return code[index]
                                    },
                                    set: { newValue in
                                        // Only allow 1 digit and digits only
                                        if newValue.count <= 1,
                                           newValue.allSatisfy(\.isNumber) || newValue.isEmpty {
                                            code[index] = newValue
                                            
                                            // Auto-focus next field or verify if last
                                            if newValue.count == 1 {
                                                if index < 5 {
                                                    focusedIndex = index + 1
                                                } else if isCodeComplete {
                                                    focusedIndex = nil
                                                    viewModel.verifySms(verificationCode: code.joined())
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
                
                if viewModel.viewState == .loading {
                    LoadingOverlayView()
                }
            }
            .onChange(of: viewModel.viewState) { newState in
                if newState == .completed {
                    showCompose = true
                }
            }
            .errorAlert(for: viewModel)
        }
    }
    
    private var isCodeComplete: Bool {
        !code.contains(where: { $0.isEmpty })
    }
}
