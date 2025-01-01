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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json


class MainViewModel(private val webSocketManager: WebSocketManager,private val employeeRepository: EmployeeRepository, private val companyRepository: CompanyRepository, private val jobRepository: JobOpportunityRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            webSocketManager.connect()
            collectWebSocketMessages()
            val employe = employeeRepository.getEmployee()
            if ((employe.id ?: 0) > 0) {
                EmployeeManager.updateEmployee(employe)
            }
            val company = companyRepository.getCompany()
            if ((company.id ?: 0) > 0) {
                CompanyManager.updateCompany(company)
            }
            try {
            val jobRe = jobRepository.getJobOpportunityById(1)
                if  (jobRe != null) {
                    addJobOpportunity(jobRe)
                }
            } catch (e: Exception) {
                addUiMessage(
                    UiMessage.Error(
                        id = Clock.System.now().toEpochMilliseconds(), message = "${e.message}"
                    )
                )
            }

          //  _uiState.value = _uiState.value.copy(jobAccepted = jobRe, foundedJob = true)
        }
    }

//
//    fun sendAcceptanceResponse(jobId: Long) {
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
//            }
//        }
//    }

    private fun collectWebSocketMessages() {
        viewModelScope.launch {
            webSocketManager.session?.incoming?.receiveAsFlow()?.collect { frame ->
                if (frame is Frame.Text) {
                    val message = frame.readText()
                    println("Nova oportunidade recebida")
                    addUiMessage(
                        UiMessage.Success(
                            id = Clock.System.now().toEpochMilliseconds(),
                            message = message
                        )
                    )
                    try {
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
            _uiState.value = _uiState.value.copy(jobAccepted = job, foundedJob = true)
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
        val isSearchingEmployee: Boolean = false,
        val jobAccepted: JobOpportunity? = null,
        val employeeId: Long? = null,
        val uiMessages: List<UiMessage> = emptyList()
    )
}