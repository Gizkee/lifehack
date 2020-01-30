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
import ru.lifehackstudio.testapp.model.Company

class CompaniesViewModel : ViewModel() {
    private val repository: CompaniesRepository

    val companies = MutableLiveData<List<Company>>()
    val loading = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    init {
        repository = CompaniesRepositoryImpl()
        loadCompanies()
    }

    private fun loadCompanies() {
        loading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.getCompanies()
            withContext(Dispatchers.Main) {
                when (result) {
                    is Success -> {
                        loading.postValue(false)
                        companies.postValue(result.data)
                    }
                    is Failure -> {
                        companies.postValue(emptyList())
                        message.postValue(result.exception.localizedMessage)
                    }
                }
            }
        }
    }
}