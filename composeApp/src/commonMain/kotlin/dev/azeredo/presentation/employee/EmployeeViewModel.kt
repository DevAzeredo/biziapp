package dev.azeredo.presentation.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.azeredo.Employee
import dev.azeredo.repositories.EmployeeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class EmployeeViewModel(private val repository: EmployeeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(EmployeeUiState())
    val uiState: StateFlow<EmployeeUiState> get() = _uiState.asStateFlow()
    fun onSubmit() {
        
        viewModelScope.launch {
            val uiState = _uiState.value
            val employee = Employee(
                id = 0L,
                fullName = uiState.fullName,
                dateOfBirth = uiState.dateOfBirth,
                gender = uiState.gender,
                email = uiState.email,
                phone = uiState.phone,
                residentialAddress = uiState.residentialAddress,
                isAvailable = uiState.isAvailable,
                lastKnownLocation = null, 
                rating = uiState.rating
            )

            try {
            repository.createOrUpdateEmployee(employee)
            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(error = "Erro ao criar/atualizar o Employee")
            }
        }
    }

    fun onFieldChange(field: EmployeeField, value: String) {
        _uiState.value =
            when (field) {
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

    data class EmployeeUiState(
        val fullName: String = "",
        val dateOfBirth: String = "",
        val gender: String = "",
        val email: String = "",
        val phone: String = "",
        val residentialAddress: String = "",
        val isAvailable: Boolean = true,
        val rating: Double = 0.0
    )

    enum class EmployeeField {
        FullName, DateOfBirth, Gender, Email, Phone, ResidentialAddress, Rating
    }
}