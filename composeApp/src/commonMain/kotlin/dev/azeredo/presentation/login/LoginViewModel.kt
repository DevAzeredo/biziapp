package dev.azeredo.presentation.login

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


class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> get() = _uiState.asStateFlow()

    fun onSubmit() {
        viewModelScope.launch {
            _uiState.value =  _uiState.value.copy(isLoading = true)
            val user = NewUser(login = _uiState.value.login, password = _uiState.value.password)
            try {
                AuthManager.saveToken(repository.login(user))
                if (AuthManager.getToken()?.isNotEmpty() == true) {
                    _uiState.value =  _uiState.value.copy(loged = true)
                }
                _uiState.value =  _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(error = "Erro ao criar/atualizar o Employee")
            }
        }
    }

    fun onFieldChange(field: LoginField, value: String) {
        _uiState.value =
            when (field) {
                LoginField.Login -> _uiState.value.copy(login = value)
                LoginField.Password -> _uiState.value.copy(password = value)
            }
    }


    data class LoginUiState(
        val login: String = "",
        val password: String = "",
        val loged:Boolean = false,
        val isLoading:Boolean = false,
    )
    enum class LoginField {
        Login, Password
    }

}