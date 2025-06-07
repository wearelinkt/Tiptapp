//
//  MapView.swift
//  iosApp
//
//  Created by Ali on 6/7/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import GoogleMaps
import SwiftUI
import ComposeApp

class IOSNativeViewFactory: NativeViewFactory {
    func createGoogleMapView(onMarkerTapped: @escaping (KotlinDouble, KotlinDouble) -> Void) -> UIViewController {
        return MapViewController(onMarkerTapped: onMarkerTapped)
    }
    
    static var shared = IOSNativeViewFactory()
}

class MapViewController: UIViewController, GMSMapViewDelegate {
    var mapView: GMSMapView!
    var marker: GMSMarker!
    let onMarkerTapped: (KotlinDouble, KotlinDouble) -> Void

    init(onMarkerTapped: @escaping (KotlinDouble, KotlinDouble) -> Void) {
        self.onMarkerTapped = onMarkerTapped
        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        let camera = GMSCameraPosition.camera(withLatitude: 35.6997, longitude: 51.3380, zoom: 15.0)
        mapView = GMSMapView(frame: self.view.bounds, camera: camera)
        mapView.delegate = self
        self.view.addSubview(mapView)

        marker = GMSMarker()
        marker.position = CLLocationCoordinate2D(latitude: 35.6997, longitude: 51.3380)
        marker.title = "Indiranagar"
        marker.snippet = "Bengaluru"
        marker.map = mapView
    }

    func mapView(_ mapView: GMSMapView, didTap marker: GMSMarker) -> Bool {
        let lat = KotlinDouble(value: marker.position.latitude)
        let lng = KotlinDouble(value: marker.position.longitude)
        onMarkerTapped(lat, lng)
        return true
    }

    func mapView(_ mapView: GMSMapView, didTapAt coordinate: CLLocationCoordinate2D) {
        marker.position = coordinate // Move the marker
        let lat = KotlinDouble(value: coordinate.latitude)
        let lng = KotlinDouble(value: coordinate.longitude)
        onMarkerTapped(lat, lng)     // Notify Compose
    }
}

