package ru.lifehackstudio.testapp.data

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.lifehackstudio.testapp.data.Result.Failure
import ru.lifehackstudio.testapp.data.Result.Success
import ru.lifehackstudio.testapp.model.Company
import ru.lifehackstudio.testapp.model.CompanyDetails


class CompaniesRepositoryImpl :
    CompaniesRepository {

    companion object {
        const val BASE_URL = "http://megakohz.bget.ru/test_task/"
    }

    var gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val service = retrofit.create(CompaniesService::class.java)

    override suspend fun getCompanies(): Result<List<Company>> {
        val response = service.getCompanies().execute()
        return if (response.isSuccessful && response.body() != null) {
            Success(response.body()!!)
        } else {
            Failure(Exception(response.errorBody().toString()))
        }
    }

    override suspend fun getCompanyDetails(id: Int): Result<CompanyDetails> {
        val response = service.getCompanyDetails(id).execute()
        return if (response.isSuccessful && response.body() != null) {
            Success(response.body()!![0])
        } else {
            Failure(Exception(response.errorBody().toString()))
        }
    }

}