package com.murad.androiddevtask.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart


/**
 * Created on 04 February 2024, 12:56 PM
 * @author Murad Eliyev
 */




sealed interface Resource<out T> {

    data object Loading : Resource<Nothing>

    data class Error(val message: String? = null) : Resource<Nothing>

    data class Success<T>(val data: T) : Resource<T>

}

fun <T, R> Resource<T>.mapResource(transform: (T) -> R) = when (this) {
    is Resource.Error -> Resource.Error(message)
    Resource.Loading -> Resource.Loading
    is Resource.Success -> Resource.Success(transform(data))
}

fun <T> Flow<T>.asResource(): Flow<Resource<T>> =
    this.map<T, Resource<T>> { Resource.Success(it) }
        .onStart { emit(Resource.Loading) }
        .catch { emit(Resource.Error(it.message)) }

suspend inline fun <T> MutableStateFlow<Resource<T>>.updateWith(crossinline block: suspend () -> T) {
    value = Resource.Loading
    value = try {
        Resource.Success(block())
    } catch (e: Exception) {
        Resource.Error(e.message)
    }
}

suspend fun <T> Flow<Resource<T>>.collectResource(
    onSuccess: (data: T) -> Unit,
    onLoadingChanged: (loading: Boolean) -> Unit = {},
    onError: (message: String?) -> Unit = {}
) {
    collectLatest {
        when (it) {
            is Resource.Error -> {
                onLoadingChanged(false)
                onError(it.message)
            }

            Resource.Loading -> onLoadingChanged(true)
            is Resource.Success -> {
                onLoadingChanged(false)
                onSuccess(it.data)
            }
        }
    }
}
