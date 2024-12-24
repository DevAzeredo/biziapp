package dev.azeredo.presentation.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.azeredo.Company
import dev.azeredo.CompanyManager
import dev.azeredo.Employee
import dev.azeredo.EmployeeManager
import dev.azeredo.UiMessage
import dev.azeredo.repositories.CompanyRepository
import dev.azeredo.repositories.EmployeeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class CompanyViewModel(private val repository: CompanyRepository): ViewModel() {
    private val _uiState = MutableStateFlow(CompanyUiState())
    val uiState: StateFlow<CompanyUiState> = _uiState

    init {
        val company = CompanyManager.currentCompany
        if (company != null) {
            _uiState.value = _uiState.value.copy(
                name = company.name,
                description = company.description,
                logoUrl = company.logoUrl ?: "",
                address = company.address,
            )
        }
    }

    fun onFieldChange(field: CompanyField, value: String) {
        _uiState.value = _uiState.value.copy(
            name = if (field == CompanyField.Name) value else _uiState.value.name,
            description = if (field == CompanyField.Description) value else _uiState.value.description,
            address = if (field == CompanyField.Address) value else _uiState.value.address,
            logoUrl = if (field == CompanyField.LogoUrl) value else _uiState.value.logoUrl,
        )
    }
    fun setPhoto(value:ByteArray){
        _uiState.value = _uiState.value.copy(photo = value)
    }

    fun onSubmit() {
        _uiState.value = _uiState.value.copy(isSubmitting = true)
        viewModelScope.launch {
            val uiState = _uiState.value
            val company = Company(
                name = uiState.name,
                description = uiState.description,
                address = uiState.address,
                logoUrl = uiState.logoUrl
            )
            try {
                val companyUpdated = repository.createOrUpdateCompany(company)
                addUiMessage(
                    UiMessage.Success(
                        id = Clock.System.now().toEpochMilliseconds(), message = "Sent successfully"
                    )
                )
                CompanyManager.updateCompany(companyUpdated)
                _uiState.value = _uiState.value.copy(isSubmitting = false, sent = true)
            } catch (e: Exception) {
                addUiMessage(
                    UiMessage.Error(
                        id = Clock.System.now().toEpochMilliseconds(), message = "${e.message}"
                    )
                )
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

    data class CompanyUiState(
        val name: String = "",
        val description: String = "",
        val logoUrl: String? = null,
        val address: String = "",
        val isSubmitting: Boolean = false,
        val sent: Boolean = false,
        val photo: ByteArray = ByteArray(0),
        val uiMessages: List<UiMessage> = emptyList()
    )


    enum class CompanyField {
        Name,
        Description,
        Address,
        LogoUrl,
        Photo
    }
}