package dev.azeredo

import com.dokar.sonner.Toast
import com.dokar.sonner.ToastType

object Constants {
    const val BASE_URL = "8080-idx-bizi-back-1736449039718.cluster-m7tpz3bmgjgoqrktlvd4ykrc2m.cloudworkstations.dev"

}
//"http://35.208.151.171:8080/logos/
sealed class UiMessage(val id: Long, val message: String) {
    class Success(id: Long, message: String) : UiMessage(id, message)
    class Error(id: Long, message: String) : UiMessage(id, message)
}
fun UiMessage.toToast(): Toast = when (this) {
    is UiMessage.Error -> Toast(id = id, message = message, type = ToastType.Error)
    is UiMessage.Success -> Toast(id = id, message = message, type = ToastType.Success)
}


