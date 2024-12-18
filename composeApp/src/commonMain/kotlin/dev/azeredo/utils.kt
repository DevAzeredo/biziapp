package dev.azeredo

import com.dokar.sonner.Toast
import com.dokar.sonner.ToastType

object Constants {
    const val BASE_URL = "biizi.onrender.com"
}
sealed class UiMessage(val id: Long, val message: String) {
    class Success(id: Long, message: String) : UiMessage(id, message)
    class Error(id: Long, message: String) : UiMessage(id, message)
}
fun UiMessage.toToast(): Toast = when (this) {
    is UiMessage.Error -> Toast(id = id, message = message, type = ToastType.Error)
    is UiMessage.Success -> Toast(id = id, message = message, type = ToastType.Success)
}
