import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dokar.sonner.Toaster
import com.dokar.sonner.listenMany
import com.dokar.sonner.rememberToasterState
import dev.azeredo.UiMessage
import dev.azeredo.presentation.employee.EmployeeViewModel
import dev.azeredo.presentation.login.LoginViewModel
import dev.azeredo.presentation.main.MainScreen
import dev.azeredo.presentation.register.RegisterScreen
import dev.azeredo.toToast
import kotlinx.coroutines.flow.map
import org.koin.compose.viewmodel.koinViewModel

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<LoginViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        if (uiState.loged) {
            navigator.push(MainScreen())
        }
        LoginScreenContent(navigator, uiState, viewModel)
    }
}

@Composable
fun LoginScreenContent(
    navigator: Navigator, uiState: LoginViewModel.LoginUiState, viewModel: LoginViewModel
) {
    val toaster = rememberToasterState(
        onToastDismissed = { viewModel.removeUiMessageById(it.id as Long) },
    )
    LaunchedEffect(viewModel, toaster) {
        val toastsFlow = viewModel.uiState.map { it.uiMessages.map(UiMessage::toToast) }
        toaster.listenMany(toastsFlow)
    }
    Toaster(state = toaster, richColors = true)
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Text(
                text = "Login",
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = uiState.login, onValueChange = {
                viewModel.onFieldChange(
                    LoginViewModel.LoginField.Login, it
                )
            }, label = { Text("Login") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = uiState.password,
                onValueChange = {
                    viewModel.onFieldChange(
                        LoginViewModel.LoginField.Password, it
                    )
                },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.onSubmit()
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log In")
            }
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material.TextButton(onClick = {
                navigator.push(RegisterScreen())
            }) {
                Text("Don't have an account? Register here")
            }
        }
    }
}
