package ru.lifehackstudio.testapp.data

import java.lang.Exception

sealed class Result<out R> {
    class Success<out T>(val data: T) : Result<T>()
    class Failure(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}