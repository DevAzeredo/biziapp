package dev.azeredo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.azeredo.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "biziapp",
    ) {
        App()
    }
}