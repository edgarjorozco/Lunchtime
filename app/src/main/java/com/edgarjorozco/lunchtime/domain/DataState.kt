package com.edgarjorozco.lunchtime.domain

sealed class DataState<out T> {
    data class Success<T>(val data : T?): DataState<T>()
    data class Error(val exception: Exception): DataState<Nothing>()
    object Loading: DataState<Nothing>()

    fun toData(): T? = when (this) {
        is Success -> this.data
        else -> null
    }

    fun isLoading(): Boolean? = if (this is Loading) true else null
    fun toErrorMessage(): String? = if (this is Error) this.exception.message else null

}
