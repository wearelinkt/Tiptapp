package iq.tiptapp

import platform.UIKit.UIViewController

interface NativeSearchViewFactory {
    fun createSearchView(
        onPlaceSelected: (latitude: Double, longitude: Double) -> Unit,
        onDone: () -> Unit
    ): UIViewController
}