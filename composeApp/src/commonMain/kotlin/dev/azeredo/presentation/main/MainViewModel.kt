package dev.azeredo.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.azeredo.JobOpportunity
import dev.azeredo.UiMessage
import dev.azeredo.WebSocketManager
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock


class MainViewModel(private val webSocketManager: WebSocketManager) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            webSocketManager.connect()
            collectWebSocketMessages()
        }
    }

    fun sendAcceptanceResponse(jobId: Long) {
        if (_uiState.value.jobAccepted != null) {
            viewModelScope.launch {
                val response = """
                {
                    "type": "job_response",
                    "employeeId": ${_uiState.value.employeeId},
                    "jobId": $jobId,
                    "accepted": true
                }
            """.trimIndent()
                webSocketManager.sendMessage(response)
            }
        }
    }

    private fun collectWebSocketMessages() {
        viewModelScope.launch {
            // Coleta frames diretamente
            webSocketManager.session?.incoming?.receiveAsFlow()?.collect { frame ->
                if (frame is Frame.Text) {
                    val message = frame.readText()
                    println("Mensagem recebida: $message")
                    addUiMessage(
                        UiMessage.Success(
                            id = Clock.System.now().toEpochMilliseconds(),
                            message = message
                        )
                    )
                }
            }
        }
    }

    private fun addUiMessage(message: UiMessage) {
        _uiState.value = _uiState.value.copy(uiMessages = _uiState.value.uiMessages + message)
    }

    fun removeUiMessageById(id: Long) {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(uiMessages = _uiState.value.uiMessages.filterNot { msg -> msg.id == id })
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            webSocketManager.disconnect()
        }
    }

    data class MainUiState(
        val nome: String = "",
        val isSearchingJob: Boolean = false,
        val isSearchingEmployee: Boolean = false,
        val jobAccepted: JobOpportunity? = null,
        val employeeId: Long? = null,
        val uiMessages: List<UiMessage> = emptyList()
    )
}