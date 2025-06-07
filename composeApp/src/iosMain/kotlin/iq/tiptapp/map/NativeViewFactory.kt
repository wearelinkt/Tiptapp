package iq.tiptapp.map

import platform.UIKit.UIViewController

interface NativeViewFactory {
    fun createGoogleMapView(
        onMarkerTapped: (latitude: Double, longitude: Double) -> Unit
    ): UIViewController
}