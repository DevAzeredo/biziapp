package dev.azeredo.repositories

import dev.azeredo.Constants.BASE_URL
import dev.azeredo.Employee
import dev.azeredo.api.AuthManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.core.use
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class EmployeeRepository(private val httpClient: HttpClient) {
    suspend fun createOrUpdateEmployee(employee: Employee): Employee {
        val response: Employee = withContext(Dispatchers.IO) {
            httpClient.post("https://$BASE_URL/employees") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                    append(HttpHeaders.Authorization, "Bearer ${AuthManager.getToken()}")
                }
                setBody(employee)
            }.body()
        }

        return response
    }

    suspend fun getEmployee(): Employee {
        return withContext(Dispatchers.IO) {
            httpClient.get("https://$BASE_URL/employees") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${AuthManager.getToken()}")
                }
            }.body()
        }
    }
}