package iq.tiptapp.map

import platform.UIKit.UIViewController

interface NativeViewFactory {
    fun createGoogleMapView(
        latitude: Double?,
        longitude: Double?,
        onMarkerTapped: (latitude: Double, longitude: Double) -> Unit
    ): UIViewController
}