package ru.lifehackstudio.testapp.data

import ru.lifehackstudio.testapp.model.Company
import ru.lifehackstudio.testapp.model.CompanyDetails

interface CompaniesRepository {

    suspend fun getCompanies(): Result<List<Company>>

    suspend fun getCompanyDetails(id: Int): Result<CompanyDetails>

}