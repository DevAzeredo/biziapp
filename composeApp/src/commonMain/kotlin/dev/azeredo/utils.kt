package dev.azeredo

import com.dokar.sonner.Toast
import com.dokar.sonner.ToastType
import kotlin.time.Duration.Companion.milliseconds

object Constants {
    const val BASE_URL = "192.168.1.143:8080"
}

sealed class UiMessage(val id: Long, val message: String, val action: () -> Unit = {}) {
    class Success(id: Long, message: String) : UiMessage(id, message)
    class Error(id: Long, message: String) : UiMessage(id, message)
    class FoundedJob(id: Long, message: String, action: () -> Unit) : UiMessage(id, message, action)
}

fun UiMessage.toToast(): Toast = when (this) {
    is UiMessage.Error -> Toast(id = id, message = message, type = ToastType.Error)
    is UiMessage.Success -> Toast(id = id, message = message, type = ToastType.Success)
    is UiMessage.FoundedJob -> Toast(
        id = id,
        message = message,
        type = ToastType.Info,
        duration = 15000.milliseconds,
        action = action
    )
}


