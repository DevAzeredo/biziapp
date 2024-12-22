package dev.azeredo.presentation.company

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CompanyViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(CompanyUiState())
    val uiState: StateFlow<CompanyUiState> = _uiState

    fun onFieldChange(field: CompanyField, value: String) {
        _uiState.value = _uiState.value.copy(
            name = if (field == CompanyField.Name) value else _uiState.value.name,
            description = if (field == CompanyField.Description) value else _uiState.value.description,
            address = if (field == CompanyField.Address) value else _uiState.value.address,
            logoUrl = if (field == CompanyField.LogoUrl) value else _uiState.value.logoUrl
        )
    }
}

data class CompanyUiState(
    val name: String = "",
    val description: String = "",
    val logoUrl: String? = null,
    val address: String = "",
)


enum class CompanyField {
    Name,
    Description,
    Address,
    LogoUrl
}
