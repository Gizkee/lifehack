package ru.lifehackstudio.testapp.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.lifehackstudio.testapp.model.Company
import ru.lifehackstudio.testapp.model.CompanyDetails

interface CompaniesService {

    @GET("test.php")
    fun getCompanies(): Call<List<Company>>

    @GET("test.php")
    fun getCompanyDetails(@Query("id") id: Int): Call<List<CompanyDetails>>
}