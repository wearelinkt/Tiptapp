//
//  TiptappGetRequest.swift
//  iosApp
//
//  Created by Ali on 6/22/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation

struct TiptappGetRequest: RequestProtocol {
    let httpMethod: HTTPMethod = .GET
    let url: URL?
    
    init(path: TiptappEndpoint.Paths) {
        let endpoint = TiptappEndpoint.tiptapp(path: path)
        self.url = DefaultURLFactory.createURL(from: endpoint)
    }

    func request() throws -> URLRequest {
        guard let url else {
            throw URLError(.badURL)
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = httpMethod.rawValue
        request.cachePolicy = .returnCacheDataElseLoad
        return request
    }
}
