package dev.azeredo.presentation.company

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import com.dokar.sonner.Toaster
import com.dokar.sonner.listenMany
import com.dokar.sonner.rememberToasterState
import com.mohamedrejeb.calf.io.getName
import com.mohamedrejeb.calf.io.getPath
import com.mohamedrejeb.calf.io.readByteArray
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.coil.KmpFileFetcher
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import dev.azeredo.Constants.BASE_URL
import dev.azeredo.UiMessage
import dev.azeredo.toToast
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.compose.viewmodel.koinViewModel

class CompanyScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<CompanyViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        CompanyScreen(navigator, viewModel, uiState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navigator: Navigator) {
    TopAppBar(title = { Text("Company Profile") }, navigationIcon = {
        IconButton(onClick = { navigator.pop() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
    }, modifier = Modifier.fillMaxWidth().padding(8.dp)
    )
}

@Composable
fun CompanyScreen(
    navigator: Navigator, viewModel: CompanyViewModel, uiState: CompanyViewModel.CompanyUiState
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
    val scope = rememberCoroutineScope()
    val context = com.mohamedrejeb.calf.core.LocalPlatformContext.current

    val pickerLauncher = rememberFilePickerLauncher(type = FilePickerFileType.Image,
        selectionMode = FilePickerSelectionMode.Single,
        onResult = { files ->
            scope.launch {
                if (files.isNotEmpty()) {
                    files.first().getPath(context)
                    files.first().getName(context)
                    viewModel.setPhoto(files.first().readByteArray(context))
                }
            }
        })
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
                CardImage(
                    onImageClick = { pickerLauncher.launch() },
                    foto = uiState.photo,
                    imageUrl = uiState.logoUrl
                )

                TextField(value = uiState.name, onValueChange = {
                    viewModel.onFieldChange(
                        CompanyViewModel.CompanyField.Name, it
                    )
                }, label = { Text("Company Name") }, modifier = Modifier.fillMaxWidth()
                )
                TextField(value = uiState.description, onValueChange = {
                    viewModel.onFieldChange(
                        CompanyViewModel.CompanyField.Description, it
                    )
                }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth()
                )
                TextField(value = uiState.address, onValueChange = {
                    viewModel.onFieldChange(
                        CompanyViewModel.CompanyField.Address, it
                    )
                }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth()
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

@Composable
fun CardImage(
    onImageClick: () -> Unit,
    foto: ByteArray? = null,
    imageUrl: String? = null,
) {
    ElevatedCard(
        modifier = Modifier
            .size(400.dp)
            .clickable { onImageClick() },
    ) {
        when {
            // se nao selecionou nenhum foto e tem a url da imagem
            !imageUrl.isNullOrEmpty() && foto?.isEmpty() ?: true -> {
                AsyncImage( // alterar, isso daqui é gambi pra fazer rapido
                    model = "http://$BASE_URL/api/companies/logos/$imageUrl?timestamp=${Clock.System.now()}",
                    contentDescription = "Company Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    contentScale = ContentScale.FillHeight,
                )
            }

            foto != null && foto.isNotEmpty() -> {
                // Carrega a imagem do ByteArray
                val imageLoader = ImageLoader.Builder(LocalPlatformContext.current)
                    .components { add(KmpFileFetcher.Factory()) }
                    .build()

                AsyncImage(
                    model = foto,
                    contentDescription = "Company Logo",
                    modifier = Modifier
                        .height(400.dp)
                        .width(400.dp),
                    contentScale = ContentScale.Fit,
                    imageLoader = imageLoader
                )
            }

            else -> {
                // Exibe um ícone de placeholder
                Icon(
                    Icons.Filled.Face,
                    contentDescription = "Placeholder",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                )
            }
        }
    }
}
