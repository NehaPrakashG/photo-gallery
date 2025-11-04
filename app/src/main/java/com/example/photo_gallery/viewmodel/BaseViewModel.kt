package com.example.photo_gallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _globalErrorFlow = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1 // Just a small space for quick error delivery
    )

    val globalErrorFlow: SharedFlow<String> = _globalErrorFlow.asSharedFlow()

    protected fun reportGlobalError(message: String) {
        viewModelScope.launch {
            _globalErrorFlow.emit(message)
        }
    }
}