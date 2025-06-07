package iq.tiptapp

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.ComposeUIViewController
import iq.tiptapp.di.initKoin
import iq.tiptapp.map.NativeViewFactory

val LocalNativeViewFactory = staticCompositionLocalOf<NativeViewFactory> {
    error("No view factory provided.")
}

fun MainViewController(
    nativeViewFactory: NativeViewFactory
) = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) {
        TiptappApp()
    }
}