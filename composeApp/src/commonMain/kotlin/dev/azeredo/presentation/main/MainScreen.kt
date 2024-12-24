package dev.azeredo.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.dokar.sonner.Toaster
import com.dokar.sonner.listenMany
import com.dokar.sonner.rememberToasterState
import dev.azeredo.JobOpportunity
import dev.azeredo.UiMessage
import dev.azeredo.presentation.company.CompanyScreen
import dev.azeredo.presentation.employee.EmployeeScreen
import dev.azeredo.presentation.jobopportunity.JobOpportunityScreen
import dev.azeredo.toToast
import kotlinx.coroutines.flow.map
import org.koin.compose.viewmodel.koinViewModel

class MainScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<MainViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        MainScreen(navigator, viewModel, uiState)
    }
}

@Composable
fun MainScreen(navigator: Navigator, viewModel: MainViewModel, uiState: MainViewModel.MainUiState) {
    val toaster = rememberToasterState(
        onToastDismissed = { viewModel.removeUiMessageById(it.id as Long) },
    )
    LaunchedEffect(viewModel, toaster) {
        val toastsFlow = viewModel.uiState.map { it.uiMessages.map(UiMessage::toToast) }
        toaster.listenMany(toastsFlow)
    }
    Toaster(state = toaster, richColors = true)
    Scaffold(
        floatingActionButton = { ProductFabMenu(navigator) },
    ) {
        Box(Modifier.fillMaxSize()) {
            Box(modifier = Modifier.background(Color.Gray)) {
                when {
                    uiState.isSearchingJob -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = Color.White, strokeWidth = 4.dp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Looking for a job...", color = Color.White)
                        }
                    }

                    uiState.foundedJob && (uiState.jobAccepted?.id ?:0)>0 -> {
                        JobItem(uiState.jobAccepted!!)
                    }

                    else -> {
                        Text(
                            "There are no opportunities available",
                            Modifier.align(Alignment.Center),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JobItem(job: JobOpportunity) {
    Column(
        modifier = Modifier.padding(8.dp).background(Color.LightGray).padding(8.dp)
    ) {
        job.companyLogoUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = "Company Logo",
                modifier = Modifier.height(64.dp),
                contentScale = ContentScale.Fit
            )
        }
        Text(text = job.title, color = Color.Black)
        Text(text = job.companyName ?: "Unknown Company", color = Color.Black)
        Text(text = job.description, color = Color.Black)
    }
}

@Composable
fun ProductFabMenu(navigator: Navigator) {
    var isFabMenuExpanded by remember { mutableStateOf(false) }
    Box {
        FloatingActionButton(onClick = { isFabMenuExpanded = !isFabMenuExpanded },
            content = { Icon(Icons.Default.Menu, contentDescription = "Options") })

        DropdownMenu(expanded = isFabMenuExpanded,
            onDismissRequest = { isFabMenuExpanded = false }) {
            DropdownMenuItem(text = { Text("Employee") }, onClick = {
                isFabMenuExpanded = false
                navigator.push(EmployeeScreen())
            }, leadingIcon = { Icon(Icons.Default.Add, contentDescription = "Employee Profile") })
            DropdownMenuItem(text = { Text("Company") }, onClick = {
                isFabMenuExpanded = false
                navigator.push(CompanyScreen())
            }, leadingIcon = {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Company Profile",
                    modifier = Modifier.rotate(180f)
                )
            })

            DropdownMenuItem(text = { Text("New Job Opportunity") }, onClick = {
                isFabMenuExpanded = false
                navigator.push(JobOpportunityScreen())
            }, leadingIcon = { Icon(Icons.Default.Info, contentDescription = "Outbound") })
        }
    }
}
