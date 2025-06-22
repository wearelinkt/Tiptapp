//
//  TiptappRequest.swift
//  iosApp
//
//  Created by Ali on 6/12/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation

struct TiptappPostRequest: RequestPostProtocol {
    let httpMethod: HTTPMethod = .POST
    let url: URL?
    let body: Data?
    
    init(path: TiptappEndpoint.Paths, body: Data?) {
        let endpoint = TiptappEndpoint.tiptapp(path: path)
        self.url = DefaultURLFactory.createURL(from: endpoint)
        self.body = body
    }

    func request() throws -> URLRequest {
        guard let url else {
            throw URLError(.badURL)
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = httpMethod.rawValue
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = body
        request.cachePolicy = .returnCacheDataElseLoad
        return request
    }
}
