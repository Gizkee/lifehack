package ru.lifehackstudio.testapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.lifehackstudio.testapp.data.Result.*
import ru.lifehackstudio.testapp.data.CompaniesRepository
import ru.lifehackstudio.testapp.data.CompaniesRepositoryImpl
import ru.lifehackstudio.testapp.model.CompanyDetails

class CompanyDetailsViewModel(id: Int) : ViewModel() {

    private val repository: CompaniesRepository

    val companyDetails = MutableLiveData<CompanyDetails>()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    init {
        repository = CompaniesRepositoryImpl()
        loadCompanyDetails(id)
    }

    private fun loadCompanyDetails(id: Int) {
        loading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.getCompanyDetails(id)
            withContext(Dispatchers.Main) {
                when (result) {
                    is Success -> {
                        loading.postValue(false)
                        companyDetails.postValue(result.data)
                    }
                    is Failure -> {
                        errorMessage.postValue(result.exception.localizedMessage)
                    }
                }
            }
        }
    }

}