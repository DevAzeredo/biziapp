package dev.azeredo.repositories

import dev.azeredo.Constants.BASE_URL
import dev.azeredo.Employee
import dev.azeredo.api.AuthManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.utils.io.core.use
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class EmployeeRepository(private val httpClient: HttpClient) {
    suspend fun createOrUpdateEmployee(employee: Employee): Employee {
        val response: Employee = withContext(Dispatchers.IO) {
            httpClient.post("http://$BASE_URL/api/employees") {
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
            val resp = httpClient.get("http://$BASE_URL/api/employees") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${AuthManager.getToken()}")
                }
            }
            if (resp.status.isSuccess()) {
                resp.body()
            } else {
                Employee(
                    fullName = "",
                    dateOfBirth = "",
                    gender = "",
                    email = "",
                    phone = "",
                    residentialAddress = "",
                    isAvailable = false,
                    latitude = 0.0,
                    longitude = 0.0,
                    rating = 0.0
                )
            }

        }
    }
}