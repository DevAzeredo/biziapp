package dev.azeredo

import androidx.compose.ui.window.ComposeUIViewController
import dev.azeredo.di.initKoin


fun MainViewController() = ComposeUIViewController(configure = {
    initKoin()
}) {

    App()
}