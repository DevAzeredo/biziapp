package dev.azeredo.di

import dev.azeredo.WebSocketManager
import dev.azeredo.presentation.company.CompanyViewModel
import dev.azeredo.presentation.employee.EmployeeViewModel
import dev.azeredo.presentation.jobopportunity.JobOpportunityViewModel
import dev.azeredo.presentation.register.RegisterViewModel
import dev.azeredo.presentation.main.MainViewModel
import dev.azeredo.api.HttpClientProvider
import dev.azeredo.presentation.login.LoginViewModel
import dev.azeredo.repositories.AuthRepository
import dev.azeredo.repositories.EmployeeRepository
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        modules(
            appModule
        )
        config?.invoke(this)
    }
}

val appModule = module {
    viewModelOf(::EmployeeViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::JobOpportunityViewModel)
    viewModelOf(::CompanyViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    single<HttpClient> { HttpClientProvider.client }
    single { EmployeeRepository(get()) }
    single { AuthRepository(get()) }
    single { WebSocketManager(get()) }
}

/*val domainModule = module {
    // product
    factory { getAllProducts(get()) }
    factory { AddProduct(get()) }
    factory { removeProduct(get()) }
    factory { UpdateProduct(get()) }
    factory { GetProductById(get()) }
    // product image
    factory { RemoveProductImageUseCase(get()) }
    factory { AddProductImageUseCase(get()) }
    // category
    factory { AddCategory(get()) }
    factory { GetAllCategories(get()) }
    // stockMovement
    factory { SaveStockMovements(get()) }
    factory { SaveMovements(get()) }
}*/