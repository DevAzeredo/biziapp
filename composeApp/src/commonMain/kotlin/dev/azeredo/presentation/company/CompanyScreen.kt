package dev.azeredo.presentation.company

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.azeredo.Company
import org.koin.compose.viewmodel.koinViewModel

class CompanyScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<CompanyViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        CompanyScreen(viewModel, {}, uiState)
    }
}

@Composable
fun CompanyScreen(
    viewModel: CompanyViewModel = CompanyViewModel(),
    onSave: (Company) -> Unit,
    uiState: CompanyUiState
) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = uiState.name,
                onValueChange = { viewModel.onFieldChange(CompanyField.Name, it) },
                label = { Text("Company Name") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = uiState.description,
                onValueChange = { viewModel.onFieldChange(CompanyField.Description, it) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = uiState.address,
                onValueChange = { viewModel.onFieldChange(CompanyField.Address, it) },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = uiState.logoUrl ?: "",
                onValueChange = { viewModel.onFieldChange(CompanyField.LogoUrl, it) },
                label = { Text("Logo URL") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {

            }) {
                Text("Submit")
            }
        }
    }
}
