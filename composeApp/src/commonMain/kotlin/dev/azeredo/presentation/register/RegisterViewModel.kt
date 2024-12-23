package dev.azeredo.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.azeredo.Employee
import dev.azeredo.NewUser
import dev.azeredo.api.AuthManager
import dev.azeredo.repositories.AuthRepository
import dev.azeredo.repositories.EmployeeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> get() = _uiState.asStateFlow()

    fun onSubmit() {
        viewModelScope.launch {
            _uiState.value =  _uiState.value.copy(isLoading = true)
            val user = NewUser(login = _uiState.value.login, password = _uiState.value.password)
            try {
                AuthManager.saveToken(repository.register(user))
                if (AuthManager.getToken()?.isNotEmpty() == true) {
                    _uiState.value =  _uiState.value.copy(isRegistred = true)
                }
                _uiState.value =  _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(error = "Erro ao criar/atualizar o Employee")
            }
        }
    }

    fun onFieldChange(field: RegisterField, value: String) {
        _uiState.value =
            when (field) {
                RegisterField.Login -> _uiState.value.copy(login = value)
                RegisterField.Password -> _uiState.value.copy(password = value)
            }
    }


    data class RegisterUiState(
        val login: String = "",
        val password: String = "",
        val isLoading:Boolean = false,
        val isRegistred:Boolean = false,
    )
    enum class RegisterField {
        Login, Password
    }

}