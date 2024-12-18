package dev.azeredo.presentation

import androidx.lifecycle.ViewModel
import dev.azeredo.JobOpportunity
import dev.azeredo.JobStatus
import dev.azeredo.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class JobOpportunityViewModel : ViewModel() {

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
            JobOpportunityField.DurationInHours -> _uiState.value.copy(
                durationInHours =  ""
            )

            JobOpportunityField.PayRate -> _uiState.value.copy(
                payRate = ""
            )
        }
    }

    fun setLocation(latitude: Double, longitude: Double) {
        _uiState.value = _uiState.value.copy(location = Location(latitude, longitude))
    }

    fun submit(onSubmit: (JobOpportunity) -> Unit) {
        val uiState = _uiState.value
        val jobOpportunity = JobOpportunity(
            id = 0L,
            title = uiState.title,
            description = uiState.description,
            companyName = uiState.companyName,
            companyLogoUrl = uiState.companyLogoUrl,
            category = uiState.category,
            address = uiState.address,
            location = uiState.location ?: Location(0.0, 0.0),
            startDateTime = uiState.startDateTime,
            durationInHours = 1,
            payRate = 1.0,
            status = JobStatus.OPEN
        )
        onSubmit(jobOpportunity)
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
    val payRate:String = ""
)

enum class JobOpportunityField {
    Title, Description, CompanyName, CompanyLogoUrl, Category, Address, StartDateTime, DurationInHours, PayRate
}
