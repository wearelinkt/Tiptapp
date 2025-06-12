//
//  Endpoint.swift
//  iosApp
//
//  Created by Ali on 6/12/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation

protocol Endpoint {
    typealias Host = String
    typealias Path = String
    var scheme: Scheme { get }
    var host: Host { get }
    var path: Path { get }
}

enum TiptappEndpoint: Endpoint {
    
    case tiptapp(path: TiptappEndpoint.Paths)
    
    var scheme: Scheme {
        return .https
    }
    
    var host: Host {
        switch self {
        case .tiptapp: "logistics-app-backend-2ay0.onrender.com"
        }
    }
    
    var path: Path {
        switch self {
        case .tiptapp(path: let path):
            return path.path
        }
    }
}

extension TiptappEndpoint {
    
    enum Paths {
        case register
        case userExist
        
        var api: String {
            return "api"
        }
        
        var register: String {
            return "users"
        }
        
        var exist: String {
            return "users/exists"
        }
        
        var path: String {
            switch self {
            case .register: return "/\(api)/\(register)/"
            case .userExist: return "/\(api)/\(exist)/"
            }
        }
    }
}
