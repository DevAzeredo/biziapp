package dev.azeredo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    val id: Long = 0,
    val fullName: String,
    val dateOfBirth: String,
    val gender: String,
    val email: String,
    val phone: String,
    val residentialAddress: String,
    val isAvailable: Boolean,
    val latitude: Double,
    val longitude: Double,
    val rating: Double
)

@Serializable
data class Company(
    val id: Long? = null,
    val name: String = "",
    val description: String = "",
    val address: String = "",
     val logoUrl: String? = null
)

@Serializable
data class JobOpportunity(
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
   val startDateTime: String,
   val durationInHours: Int,
    val payRate: Double,
    val status: String,
    val company: Company
)

@Serializable
data class Location(
    val latitude: Double, val longitude: Double
)

@Serializable
data class NewUser(
    val login: String, val password: String
)

@Serializable
enum class JobStatus {
   OPEN,
     PENDING,
   COMPLETED,
    CANCELLED
}

// SIGLETONS
object EmployeeManager {
    var currentEmployee: Employee? = null
        private set

    fun updateEmployee(employee: Employee) {
        currentEmployee = employee
    }

    fun clearEmployee() {
        currentEmployee = null
    }
}

object CompanyManager {
    var currentCompany: Company? = null
        private set

    fun updateCompany(company: Company) {
        currentCompany = company
    }

    fun clearCompany() {
        currentCompany = null
    }
}