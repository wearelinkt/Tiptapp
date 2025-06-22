//
//  RequestProtocol.swift
//  iosApp
//
//  Created by Ali on 6/12/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation

protocol RequestProtocol {
    var httpMethod: HTTPMethod { get }
    var url: URL? { get }
    func request() throws -> URLRequest
}

protocol RequestPostProtocol: RequestProtocol {
    var body: Data? { get }
}
