package iq.tiptapp.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import iq.tiptapp.LocalNativeSearchViewFactory

@Composable
actual fun SearchComponent(onPlaceSelected: (Double, Double) -> Unit, onDone: () -> Unit) {
    val factory = LocalNativeSearchViewFactory.current
    UIKitViewController(
        modifier = Modifier.fillMaxSize(),
        factory = {
            factory.createSearchView(
                { lat, lng ->
                    onPlaceSelected.invoke(lat, lng)
                }, onDone = onDone
            )
        }
    )
}