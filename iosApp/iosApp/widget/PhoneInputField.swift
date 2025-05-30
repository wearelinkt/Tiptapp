//
//  PhoneInputField.swift
//  iosApp
//
//  Created by Ali on 5/26/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct PhoneInputField: View {
    @Binding var phoneNumber: String

    var body: some View {
        HStack {
            Text("+98")
                .padding(.leading, 12)
                .foregroundColor(.gray)

            TextField("Phone number", text: $phoneNumber)
                .keyboardType(.numberPad)
                .padding(12)
        }
        .background(Color(.systemGray6))
        .cornerRadius(10)
        .padding(.horizontal)
    }
}
