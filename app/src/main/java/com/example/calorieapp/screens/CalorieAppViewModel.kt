package com.example.calorieapp.screens


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorieapp.common.snackbar.SnackbarManager
import com.example.calorieapp.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.example.calorieapp.model.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * A base ViewModel class for the Calorie App providing common functionality for coroutine launch
 * and error handling across feature-specific ViewModels.
 *
 * This ViewModel extends [ViewModel] and integrates coroutine handling with automatic exception
 * capture, user feedback via Snackbar, and error logging. It is designed to be inherited by other
 * ViewModels in the application to promote code reuse and consistent error handling.
 *
 * @property logService The logging service used to record non-fatal errors and debugging information.
 *
 * @see ViewModel for Android ViewModel fundamentals.
 * @see SnackbarManager for how snackbar messages are displayed.
 * @see LogService for error logging implementation details.
 */
open class CalorieAppViewModel(private val logService: LogService) : ViewModel() {

    /**
     * Launches a coroutine in the ViewModelScope with built-in exception handling and logging.
     *
     * This method provides a safe way to execute suspend functions while automatically handling
     * exceptions through a unified error handling mechanism. By default, errors will trigger:
     * 1. A Snackbar notification to inform the user
     * 2. Error logging through the [LogService]
     *
     * @param snackbar When true (default), shows a Snackbar message for exceptions. Set to false
     *        to suppress Snackbar notifications while still logging errors.
     * @param block The suspend function to execute within the coroutine scope.
     *
     * @see CoroutineExceptionHandler for details on coroutine exception handling.
     * @see viewModelScope for coroutine scope lifecycle management.
     */
    fun launchCatching(snackbar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (snackbar) {
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                }
                logService.logNonFatalCrash(throwable)
            },
            block = block
        )

}
