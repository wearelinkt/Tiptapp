//
//  AppDelegate.swift
//  iosApp
//
//  Created by Ali on 5/31/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import UIKit
import Firebase
import UserNotifications
import GoogleMaps

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {

    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        if let apiKey = Bundle.main.infoDictionary?["MAPS_API_KEY"] as? String {
            GMSServices.provideAPIKey(apiKey)
        }
        FirebaseApp.configure()
        //Auth.auth().useEmulator(withHost: "127.0.0.1", port: 9099)
        
        UNUserNotificationCenter.current().delegate = self
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
            print("Notification permission granted: \(granted)")
        }

        application.registerForRemoteNotifications()
        return true
    }

    func application(_ application: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Auth.auth().setAPNSToken(deviceToken, type: .sandbox) // or .prod for production
    }

    func application(_ application: UIApplication,
                     didReceiveRemoteNotification userInfo: [AnyHashable: Any],
                     fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        
        if Auth.auth().canHandleNotification(userInfo) {
            completionHandler(.noData)
            return
        }

        // Handle your own notifications here if needed
        completionHandler(.newData)
    }
}

