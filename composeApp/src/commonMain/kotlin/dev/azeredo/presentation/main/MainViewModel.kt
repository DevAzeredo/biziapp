package dev.azeredo.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.azeredo.CompanyManager
import dev.azeredo.EmployeeManager
import dev.azeredo.JobOpportunity
import dev.azeredo.UiMessage
import dev.azeredo.WebSocketManager
import dev.azeredo.repositories.CompanyRepository
import dev.azeredo.repositories.EmployeeRepository
import dev.azeredo.repositories.JobOpportunityRepository
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.milliseconds


class MainViewModel(
    private val webSocketManager: WebSocketManager,
    private val employeeRepository: EmployeeRepository,
    private val companyRepository: CompanyRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            webSocketManager.connect()
            collectWebSocketMessages()
            try {
                val employe = employeeRepository.getEmployee()
                if ((employe.id ?: 0) > 0) {
                    EmployeeManager.updateEmployee(employe)
                }
                val company = companyRepository.getCompany()
                if ((company.id ?: 0) > 0) {
                    CompanyManager.updateCompany(company)
                }
            } catch (e: Exception) {
                addUiMessage(
                    UiMessage.Error(
                        id = Clock.System.now().toEpochMilliseconds(), message = "${e.message}"
                    )
                )
            }
        }
    }

    fun sendAcceptanceResponse() {
//        if (_uiState.value.jobAccepted != null) {
//            viewModelScope.launch {
//                val response = """
//                {
//                    "type": "job_response",
//                    "employeeId": ${_uiState.value.employeeId},
//                    "jobId": $jobId,
//                    "accepted": true
//                }
//            """.trimIndent()
//                webSocketManager.sendMessage(response)
        _uiState.value = _uiState.value.copy(acceptedJob = true)
    }

    private fun collectWebSocketMessages() {
        viewModelScope.launch {
            webSocketManager.session?.incoming?.receiveAsFlow()?.collect { frame ->
                if (frame is Frame.Text) {
                    try {
                        val message = frame.readText()
                        val jobOpportunity = Json.decodeFromString<JobOpportunity>(message)
                        addJobOpportunity(jobOpportunity)
                    } catch (e: Exception) {
                        println("Erro ao interpretar o objeto recebido: ${e.message}")
                    }

                }
            }
        }
    }

    private fun addJobOpportunity(job: JobOpportunity) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(jobOpportunity = job, foundedJob = true)
            addUiMessage(
                UiMessage.FoundedJob(
                    id = Clock.System.now().toEpochMilliseconds(),
                    message = "A job opportunity is available. Do you wish to accept?",
                    action = { sendAcceptanceResponse() }
                )
            )
            delay(16000.milliseconds)
            if (!_uiState.value.acceptedJob) {
                _uiState.value = _uiState.value.copy(jobOpportunity = null, foundedJob = false)
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
        val foundedJob: Boolean = false,
        val acceptedJob: Boolean = false,
        val isSearchingEmployee: Boolean = false,
        val jobOpportunity: JobOpportunity? = null,
        val employeeId: Long? = null,
        val uiMessages: List<UiMessage> = emptyList(),
    )
}