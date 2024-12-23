package dev.azeredo.presentation.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.azeredo.Employee
import dev.azeredo.UiMessage
import dev.azeredo.repositories.EmployeeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock


class EmployeeViewModel(private val repository: EmployeeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(EmployeeUiState())
    val uiState: StateFlow<EmployeeUiState> get() = _uiState.asStateFlow()
    fun onSubmit() {
        _uiState.value = _uiState.value.copy(isSubmitting = true)
        viewModelScope.launch {
            val uiState = _uiState.value
            val employee = Employee(
                fullName = uiState.fullName,
                dateOfBirth = uiState.dateOfBirth,
                gender = uiState.gender,
                email = uiState.email,
                phone = uiState.phone,
                residentialAddress = uiState.residentialAddress,
                isAvailable = uiState.isAvailable,
                latitude = 0.0,
                longitude = 0.0,
                rating = uiState.rating
            )
            try {
                repository.createOrUpdateEmployee(employee)
                addUiMessage(
                    UiMessage.Success(
                        id = Clock.System.now().toEpochMilliseconds(), message = "Sent successfully"
                    )
                )
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

    fun onFieldChange(field: EmployeeField, value: String) {
        _uiState.value = when (field) {
            EmployeeField.FullName -> _uiState.value.copy(fullName = value)
            EmployeeField.DateOfBirth -> _uiState.value.copy(dateOfBirth = value)
            EmployeeField.Gender -> _uiState.value.copy(gender = value)
            EmployeeField.Email -> _uiState.value.copy(email = value)
            EmployeeField.Phone -> _uiState.value.copy(phone = value)
            EmployeeField.ResidentialAddress -> _uiState.value.copy(residentialAddress = value)
            EmployeeField.Rating -> _uiState.value.copy(rating = value.toDoubleOrNull() ?: 0.0)
        }
    }

    fun toggleAvailability() {
        _uiState.value = _uiState.value.copy(isAvailable = !_uiState.value.isAvailable)
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

    data class EmployeeUiState(
        val fullName: String = "",
        val dateOfBirth: String = "",
        val gender: String = "",
        val email: String = "",
        val phone: String = "",
        val residentialAddress: String = "",
        val isAvailable: Boolean = true,
        val rating: Double = 0.0,
        val isSubmitting: Boolean = false,
        val sent: Boolean = false,
        val uiMessages: List<UiMessage> = emptyList()
    )

    enum class EmployeeField {
        FullName, DateOfBirth, Gender, Email, Phone, ResidentialAddress, Rating
    }
}