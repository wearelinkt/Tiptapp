package iq.tiptapp

import platform.UIKit.UIViewController

interface NativeMapViewFactory {
    fun createGoogleMapView(
        latitude: Double?,
        longitude: Double?,
        markerSnippet: String,
        onMarkerTapped: (latitude: Double, longitude: Double) -> Unit
    ): UIViewController
}