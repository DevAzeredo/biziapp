package dev.azeredo.repositories

import dev.azeredo.Company
import dev.azeredo.Constants.BASE_URL
import dev.azeredo.Employee
import dev.azeredo.api.AuthManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class CompanyRepository(private val httpClient: HttpClient) {
    suspend fun createOrUpdateCompany(company: Company): Company {
        return withContext(Dispatchers.IO) {
            httpClient.post("http://$BASE_URL/companies") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                    append(HttpHeaders.Authorization, "Bearer ${AuthManager.getToken()}")
                }
                contentType(ContentType.Application.Json)
                setBody(company)
            }.body()
        }
    }

    suspend fun getCompany(): Company {
        return withContext(Dispatchers.IO) {
            try {
                httpClient.get("http://$BASE_URL/companies") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer ${AuthManager.getToken()}")
                    }
                }.body<Company>()
            } catch (e: Exception) {
//                if (e.response.status.value == 404) {
                    // Se a resposta for 404, retorna uma Company default
                     Company(
                        logoUrl = "",
                        id = null,
                        name = "",
                        address = "",
                        description = "",
                    )

            }
        }
    }

    suspend fun uploadCompanyLogo(imageBytes: ByteArray): String {
        return withContext(Dispatchers.IO) {
            val multipartBody = MultiPartFormDataContent(
                formData {
                    append("logo", imageBytes, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=logo.png")
                        append(HttpHeaders.ContentType, "image/png")
                    })
                }
            )

            httpClient.post("http://$BASE_URL/companies/upload-logo") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${AuthManager.getToken()}")
                }
                setBody(multipartBody)
            }.body()

        }
    }
}
