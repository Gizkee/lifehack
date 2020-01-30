package ru.lifehackstudio.testapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CompanyDetailsViewModelFactory(val id: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CompanyDetailsViewModel(id) as T
    }
}