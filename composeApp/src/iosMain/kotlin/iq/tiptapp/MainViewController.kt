package iq.tiptapp

import androidx.compose.ui.window.ComposeUIViewController
import iq.tiptapp.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { TiptappApp() }