package dev.azeredo.presentation.jobopportunity

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
import org.koin.compose.viewmodel.koinViewModel

class JobOpportunityScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<JobOpportunityViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        JobOpportunityRegistration(viewModel, uiState)
    }
}

@Composable
fun JobOpportunityRegistration(viewModel: JobOpportunityViewModel, uiState: JobOpportunityUiState) {

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
            TextField(value = uiState.title, onValueChange = {
                viewModel.onFieldChange(
                    JobOpportunityField.Title, it
                )
            }, label = { Text("Title") })
            TextField(value = uiState.description, onValueChange = {
                viewModel.onFieldChange(
                    JobOpportunityField.Description, it
                )
            }, label = { Text("Description") })
            TextField(value = uiState.category, onValueChange = {
                viewModel.onFieldChange(
                    JobOpportunityField.Category, it
                )
            }, label = { Text("Category") })
            TextField(value = uiState.address, onValueChange = {
                viewModel.onFieldChange(
                    JobOpportunityField.Address, it
                )
            }, label = { Text("Address") })
            TextField(value = uiState.startDateTime, onValueChange = {
                viewModel.onFieldChange(
                    JobOpportunityField.StartDateTime, it
                )
            }, label = { Text("Start Time") })
            TextField(value = uiState.durationInHours, onValueChange = {
                viewModel.onFieldChange(
                    JobOpportunityField.DurationInHours, it
                )
            }, label = { Text("End Time") })

            TextField(value = uiState.payRate, onValueChange = {
                viewModel.onFieldChange(
                    JobOpportunityField.PayRate, it
                )
            }, label = { Text("Pay Rate") })


            Button(onClick = {

            }) {
                Text("Submit")
            }
        }
    }
}
