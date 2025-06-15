//
//  PhoneAuthViewModel.swift
//  iosApp
//
//  Created by Ali on 5/27/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import FirebaseAuth

class PhoneAuthViewModel: BaseViewModel {
    private let networkService: NetworkServiceProtocol
    private let verificationIDKey = "authVerificationID"
    private let userIdKey = "userID"
    
    @Published var phoneNumber: String = ""
    @Published var userId: String = ""
    @Published var isUserExist: Bool = false
    
    init(networkService: NetworkServiceProtocol) {
        self.networkService = networkService
    }
    
    /*func testCurlToEmulator() {
     let url = URL(string: "http://127.0.0.1:9099")!
     let task = URLSession.shared.dataTask(with: url) { data, response, error in
     if let error = error {
     print("❌ Error reaching emulator: \(error)")
     } else if let data = data, let str = String(data: data, encoding: .utf8) {
     print("✅ Response from emulator:\n\(str)")
     }
     }
     task.resume()
     }*/
    
    func sendSmsVerification(phoneNumber: String)  {
        self.viewState = .loading
        PhoneAuthProvider.provider().verifyPhoneNumber(phoneNumber, uiDelegate: nil) { verificationID, error in
            if let error = error {
                self.viewState = .failure(error: error)
                return
            }
            guard let verificationID = verificationID else {
                let error = NSError(domain: "PhoneAuth", code: -1, userInfo: [NSLocalizedDescriptionKey: "Verification ID not received"])
                self.viewState = .failure(error: error)
                return
            }
            UserDefaults.standard.set(verificationID, forKey: self.verificationIDKey)
            self.viewState = .completed
        }
    }
    
    func verifySms(verificationCode: String) {
        self.viewState = .loading
        guard let verificationID = UserDefaults.standard.string(forKey: verificationIDKey) else {
            let error = NSError(domain: "PhoneAuth", code: -1, userInfo: [NSLocalizedDescriptionKey: "Missing verification ID."])
            self.viewState = .failure(error: error)
            return
        }
        print("verificationId: ",verificationID)
        
        let credential = PhoneAuthProvider.provider().credential(
            withVerificationID: verificationID,
            verificationCode: verificationCode
        )
        
        Auth.auth().signIn(with: credential) { authResult, error in
            if let error = error {
                self.viewState = .failure(error: error)
            } else if let user = authResult?.user {
                print("userId: ",user)
                self.userId = user.uid
            } else {
                let error = NSError(domain: "PhoneAuth", code: -2, userInfo: [NSLocalizedDescriptionKey: "Unknown error. No user returned."])
                self.viewState = .failure(error: error)
            }
        }
    }
    
    func registerUser() async {
        let body = RegisterRequest(id: userId, phoneNumber: "+98\(phoneNumber)")
        do {
            let request = TiptappRequest(path: .register, body: try JSONEncoder().encode(body))
            let response = try await networkService.performWithResponse(request: request)
            print("Status code: \(response.statusCode)")
            if response.statusCode == 201 {
                UserDefaults.standard.set(userId, forKey: self.userIdKey)
            }
            self.viewState = .completed
        } catch {
            viewState = .failure(error: error)
        }
    }
    
    func userExist() async {
        guard let userId = UserDefaults.standard.string(forKey: userIdKey) else {
            self.isUserExist = false
            return
        }
        let body = UserExistRequest(id: userId)
        do {
            let request = TiptappRequest(path: .userExist, body: try JSONEncoder().encode(body))
            let response = try await networkService.performWithResponse(request: request)
            print("Status code: \(response.statusCode)")
            if response.statusCode == 200 {
                self.isUserExist = true
            }
        } catch {
            print("Error: \(error.localizedDescription)")
            self.isUserExist = false
        }
    }
}
