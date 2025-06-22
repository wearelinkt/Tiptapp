//
//  NetworkService.swift
//  iosApp
//
//  Created by Ali on 6/12/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation

protocol NetworkServiceProtocol {
    func perform(request: RequestProtocol) async throws -> Data
    func perform(request: RequestProtocol) async throws -> URLResponse
    func perform<T: Decodable>(request: RequestProtocol) async throws -> T
    func performWithResponse(request: RequestProtocol) async throws -> HTTPURLResponse
}

final class NetworkService: NetworkServiceProtocol {
    
    private let session: URLSession
    
    private static var urlCache: URLCache {
        let cacheSizeMemory = 20 * 1024 * 1024 // 20 MB
        let cacheSizeDisk = 100 * 1024 * 1024 // 100 MB
        let cache = URLCache(memoryCapacity: cacheSizeMemory, diskCapacity: cacheSizeDisk, diskPath: "URLCacheDirectory")
        return cache
    }
 
    init(session: URLSession? = nil) {
        let config = URLSessionConfiguration.default
        config.urlCache = NetworkService.urlCache
        config.requestCachePolicy = .reloadRevalidatingCacheData
        self.session = session ?? URLSession(configuration: config)
    }
    
    func perform(request: RequestProtocol) async throws -> Data {
        return try await session.data(for: request.request()).0
    }
    
    func perform(request: RequestProtocol) async throws -> URLResponse {
        return try await session.data(for: request.request()).1
    }
    
    func perform<T: Decodable>(request: RequestProtocol) async throws -> T {
        let data: Data = try await perform(request: request)
        let decoder = JSONDecoder()
        let result = try decoder.decode(T.self, from: data)
        return result
    }
    
    func performWithResponse(request: RequestProtocol) async throws -> HTTPURLResponse {
        let response: URLResponse = try await perform(request: request)
        guard let httpResponse = response as? HTTPURLResponse else {
            throw URLError(.badServerResponse)
        }
        return httpResponse
    }
}
