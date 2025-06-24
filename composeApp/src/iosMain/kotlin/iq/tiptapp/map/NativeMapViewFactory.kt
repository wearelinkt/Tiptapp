package iq.tiptapp.map

import platform.UIKit.UIViewController

interface NativeMapViewFactory {
    fun createGoogleMapView(
        latitude: Double?,
        longitude: Double?,
        onMarkerTapped: (latitude: Double, longitude: Double) -> Unit
    ): UIViewController
}