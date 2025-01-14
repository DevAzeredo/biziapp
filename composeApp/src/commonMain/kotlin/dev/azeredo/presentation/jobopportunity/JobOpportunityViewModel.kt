package dev.azeredo.presentation.jobopportunity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.azeredo.Company
import dev.azeredo.CompanyManager
import dev.azeredo.JobOpportunity
import dev.azeredo.JobStatus
import dev.azeredo.Location
import dev.azeredo.UiMessage
import dev.azeredo.repositories.CompanyRepository
import dev.azeredo.repositories.JobOpportunityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class JobOpportunityViewModel(private val repository: JobOpportunityRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(JobOpportunityUiState())
    val uiState: StateFlow<JobOpportunityUiState> get() = _uiState.asStateFlow()


    fun onFieldChange(field: JobOpportunityField, value: String) {
        _uiState.value = when (field) {
            JobOpportunityField.Title -> _uiState.value.copy(title = value)
            JobOpportunityField.Description -> _uiState.value.copy(description = value)
            JobOpportunityField.CompanyName -> _uiState.value.copy(companyName = value)
            JobOpportunityField.CompanyLogoUrl -> _uiState.value.copy(companyLogoUrl = value)
            JobOpportunityField.Category -> _uiState.value.copy(category = value)
            JobOpportunityField.Address -> _uiState.value.copy(address = value)
            JobOpportunityField.StartDateTime -> _uiState.value.copy(startDateTime = value)
            JobOpportunityField.DurationInHours -> _uiState.value.copy(durationInHours = value)
            JobOpportunityField.PayRate -> _uiState.value.copy(payRate = value)
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

    fun onSubmit() {
        _uiState.value = _uiState.value.copy(isSubmitting = true)

         viewModelScope.launch {
             try {
            val jobOpportunity = JobOpportunity(
                title = uiState.value.title,
                description = uiState.value.description,
                company = CompanyManager.currentCompany ?: Company(
                    id = 0,
                    logoUrl = "",
                    address = "",
                    description = "",
                    name = ""
                ),
                category = uiState.value.category,
                address = uiState.value.address,
                startDateTime = uiState.value.startDateTime,
                durationInHours = uiState.value.durationInHours.toInt(),
                payRate = uiState.value.payRate.toDouble(),
                status = JobStatus.OPEN.toString(),
                latitude = 0.0,
                longitude = 0.0
            )


                repository.createJobOpportunity(jobOpportunity)
                addUiMessage(
                    UiMessage.Success(
                        id = Clock.System.now().toEpochMilliseconds(), message = "Sent successfully"
                    )
                )
                _uiState.value = _uiState.value.copy(isSubmitting = false, sent = true)
            }
             catch (e: Exception) {
                 addUiMessage(
                     UiMessage.Error(
                         id = Clock.System.now().toEpochMilliseconds(), message = "${e.message}"
                     )
                 )
             }
        }
    }


    data class JobOpportunityUiState(
        val title: String = "",
        val description: String = "",
        val companyName: String = "",
        val companyLogoUrl: String? = null,
        val category: String = "",
        val address: String = "",
        val location: Location? = null,
        val startDateTime: String = "",
        val durationInHours: String = "",
        val payRate: String = "",
        val isSubmitting: Boolean = false,
        val sent: Boolean = false,
        val uiMessages: List<UiMessage> = emptyList()
    )

    enum class JobOpportunityField {
        Title, Description, CompanyName, CompanyLogoUrl, Category, Address, StartDateTime, DurationInHours, PayRate
    }
}
