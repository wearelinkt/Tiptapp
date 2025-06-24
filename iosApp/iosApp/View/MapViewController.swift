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

class IOSMapNativeViewFactory: NativeMapViewFactory {
    func createGoogleMapView(latitude: KotlinDouble?, longitude: KotlinDouble?, onMarkerTapped: @escaping (KotlinDouble, KotlinDouble) -> Void) -> UIViewController {
        return MapViewController(latitude: latitude!.doubleValue, longitude: longitude!.doubleValue, onMarkerTapped: onMarkerTapped)
    }
    
    static var shared = IOSMapNativeViewFactory()
}

class MapViewController: UIViewController, GMSMapViewDelegate {
    let latitude: Double
    let longitude: Double
    let onMarkerTapped: (KotlinDouble, KotlinDouble) -> Void
    var marker: GMSMarker!
    
    init(latitude: Double, longitude: Double, onMarkerTapped: @escaping (KotlinDouble, KotlinDouble) -> Void) {
        self.latitude = latitude
        self.longitude = longitude
        self.onMarkerTapped = onMarkerTapped
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        DispatchQueue.main.async {
            let camera = GMSCameraPosition.camera(withLatitude: self.latitude, longitude: self.longitude, zoom: 15.0)
            let mapView = GMSMapView(frame: self.view.bounds, camera: camera)
            mapView.delegate = self
            self.view.addSubview(mapView)
            
            self.marker = GMSMarker()
            self.marker.position = CLLocationCoordinate2D(latitude: self.latitude, longitude: self.longitude)
            self.marker.title = "Selected Location"
            self.marker.snippet = "Lat: \(self.marker.position.latitude), Lng: \(self.marker.position.longitude)"
            self.marker.map = mapView
        }
    }
    
    // Marker click
    func mapView(_ mapView: GMSMapView, didTap marker: GMSMarker) -> Bool {
        let lat = KotlinDouble(value: marker.position.latitude)
        let lng = KotlinDouble(value: marker.position.longitude)
        onMarkerTapped(lat, lng)
        return true
    }
    
    // Map tap
    func mapView(_ mapView: GMSMapView, didTapAt coordinate: CLLocationCoordinate2D) {
        mapView.animate(toLocation: coordinate) // Animate to new location
        // marker will be updated in idle callback
        let lat = KotlinDouble(value: coordinate.latitude)
        let lng = KotlinDouble(value: coordinate.longitude)
        onMarkerTapped(lat, lng)
    }
    
    // Called when map stops moving (e.g. after scroll or tap)
    func mapView(_ mapView: GMSMapView, idleAt position: GMSCameraPosition) {
        marker.position = position.target
        marker.snippet = "Lat: \(position.target.latitude), Lng: \(position.target.longitude)"
        
        let lat = KotlinDouble(value: position.target.latitude)
        let lng = KotlinDouble(value: position.target.longitude)
        onMarkerTapped(lat, lng)
    }
}


