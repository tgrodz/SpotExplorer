package com.spotexplorer.presentation.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseViewModel() : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("BaseViewModel", "Coroutine exception: ${exception.localizedMessage}", exception)
    }

    /**
     * Executes the given [action] in the background using the specified [dispatcher]
     * (defaults to Dispatchers.IO instead of the default main thread dispatcher.).
     * Errors are caught and logged by the ViewModel's handler.
     */
    fun launchInBackground(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        action: suspend () -> Unit
    ) {
        viewModelScope.launch(dispatcher + coroutineExceptionHandler) {
            action()
        }
    }

}