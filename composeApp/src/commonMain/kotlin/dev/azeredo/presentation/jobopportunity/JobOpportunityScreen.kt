package dev.azeredo.presentation.jobopportunity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dokar.sonner.Toaster
import com.dokar.sonner.listenMany
import com.dokar.sonner.rememberToasterState
import dev.azeredo.UiMessage
import dev.azeredo.presentation.company.TopBar
import dev.azeredo.toToast
import kotlinx.coroutines.flow.map
import org.koin.compose.viewmodel.koinViewModel

class JobOpportunityScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<JobOpportunityViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        JobOpportunityRegistration(viewModel, uiState, navigator)
    }
}

@Composable
fun JobOpportunityRegistration(
    viewModel: JobOpportunityViewModel,
    uiState: JobOpportunityViewModel.JobOpportunityUiState,
    navigator: Navigator
) {
    val toaster = rememberToasterState(
        onToastDismissed = { viewModel.removeUiMessageById(it.id as Long) },
    )
    LaunchedEffect(viewModel, toaster) {
        val toastsFlow = viewModel.uiState.map { it.uiMessages.map(UiMessage::toToast) }
        toaster.listenMany(toastsFlow)
    }
    Toaster(state = toaster, richColors = true)
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = { TopBar(navigator) },
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues).imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth().verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(value = uiState.title, onValueChange = {
                    viewModel.onFieldChange(
                        JobOpportunityViewModel.JobOpportunityField.Title, it
                    )
                }, label = { Text("Title") })
                TextField(value = uiState.description, onValueChange = {
                    viewModel.onFieldChange(
                        JobOpportunityViewModel.JobOpportunityField.Description, it
                    )
                }, label = { Text("Description") })
                TextField(value = uiState.category, onValueChange = {
                    viewModel.onFieldChange(
                        JobOpportunityViewModel.JobOpportunityField.Category, it
                    )
                }, label = { Text("Category") })
                TextField(value = uiState.address, onValueChange = {
                    viewModel.onFieldChange(
                        JobOpportunityViewModel.JobOpportunityField.Address, it
                    )
                }, label = { Text("Address") })
                TextField(value = uiState.startDateTime, onValueChange = {
                    viewModel.onFieldChange(
                        JobOpportunityViewModel.JobOpportunityField.StartDateTime, it
                    )
                }, label = { Text("Start Time") })
                TextField(
                    value = uiState.durationInHours,
                    onValueChange = {
                        viewModel.onFieldChange(
                            JobOpportunityViewModel.JobOpportunityField.DurationInHours, it
                        )
                    },
                    label = { Text("End Time") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                TextField(
                    value = uiState.payRate,
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                            viewModel.onFieldChange(
                                JobOpportunityViewModel.JobOpportunityField.PayRate,
                                newValue
                            )
                        }
                    },
                    label = { Text("Pay Rate") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Button(onClick = { viewModel.onSubmit() }, enabled = !uiState.isSubmitting) {
                    if (uiState.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp), strokeWidth = 2.dp
                        )
                    } else {
                        Text("Submit")
                    }
                }
                Spacer(modifier = Modifier.height(38.dp))
            }
        }
    }
}
