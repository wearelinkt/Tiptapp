package iq.tiptapp

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.ComposeUIViewController
import iq.tiptapp.di.initKoin

val LocalNativeMapViewFactory = staticCompositionLocalOf<NativeMapViewFactory> {
    error("No view factory provided.")
}

val LocalNativeSearchViewFactory = staticCompositionLocalOf<NativeSearchViewFactory> {
    error("No view factory provided.")
}

fun MainViewController(
    nativeMapViewFactory: NativeMapViewFactory,
    nativeSearchViewFactory: NativeSearchViewFactory,
) = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    CompositionLocalProvider(
        LocalNativeMapViewFactory provides nativeMapViewFactory,
        LocalNativeSearchViewFactory provides nativeSearchViewFactory
    ) {
        TiptappApp()
    }
}