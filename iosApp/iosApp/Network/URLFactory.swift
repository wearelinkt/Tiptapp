//
//  URLFactory.swift
//  iosApp
//
//  Created by Ali on 6/12/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation

protocol URLFactory {
    static func createURL(from endpoint: Endpoint) -> URL?
}

struct DefaultURLFactory: URLFactory {
    
    static func createURL(from endpoint: Endpoint) -> URL? {
        var urlComponents = URLComponents()
        urlComponents.scheme = endpoint.scheme.rawValue
        urlComponents.host = endpoint.host
        urlComponents.path = endpoint.path
        return urlComponents.url
    }
}
