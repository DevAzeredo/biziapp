package dev.azeredo.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
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
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

class EmployeeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<EmployeeViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        EmployeeScreen(viewModel, uiState)
    }
}

@Composable
fun Image() {
    Box(
        modifier = Modifier.size(150.dp).clickable {
            // TODO abrir selecionador de imagem
        }, contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Filled.Face,
            contentDescription = "Employee Image",
            modifier = Modifier.fillMaxSize().padding(16.dp)
        )
    }
}

@Preview
@Composable
fun EmployeeScreen(
    viewModel: EmployeeViewModel,
    uiState: EmployeeViewModel.EmployeeUiState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image()
            TextField(
                value = uiState.fullName,
                onValueChange = {
                    viewModel.onFieldChange(
                        EmployeeViewModel.EmployeeField.FullName,
                        it
                    )
                },
                label = { Text("Full Name") }
            )
            TextField(
                value = uiState.dateOfBirth,
                onValueChange = {
                    viewModel.onFieldChange(
                        EmployeeViewModel.EmployeeField.DateOfBirth,
                        it
                    )
                },
                label = { Text("Date of Birth (yyyy-MM-dd)") }
            )
            TextField(
                value = uiState.gender,
                onValueChange = {
                    viewModel.onFieldChange(
                        EmployeeViewModel.EmployeeField.Gender,
                        it
                    )
                },
                label = { Text("Gender") }
            )
            TextField(
                value = uiState.email,
                onValueChange = {
                    viewModel.onFieldChange(
                        EmployeeViewModel.EmployeeField.Email,
                        it
                    )
                },
                label = { Text("Email") }
            )
            TextField(
                value = uiState.phone,
                onValueChange = {
                    viewModel.onFieldChange(
                        EmployeeViewModel.EmployeeField.Phone,
                        it
                    )
                },
                label = { Text("Phone") }
            )
            TextField(
                value = uiState.residentialAddress,
                onValueChange = {
                    viewModel.onFieldChange(
                        EmployeeViewModel.EmployeeField.ResidentialAddress,
                        it
                    )
                },
                label = { Text("Residential Address") }
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Is Available:")
                Switch(
                    checked = uiState.isAvailable,
                    onCheckedChange = { viewModel.toggleAvailability() }
                )
            }
            Button(onClick = { viewModel.onSubmit() }) {
                Text("Submit")
            }
        }
    }
}