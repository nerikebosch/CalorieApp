package com.example.calorieapp.common.snackbar

import androidx.annotation.StringRes
import com.example.calorieapp.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton object that manages snackbar messages.
 * It provides functions to show and clear snackbar messages.
 */
object SnackbarManager {
    /**
     * A state flow holding the current snackbar message.
     */
    private val messages: MutableStateFlow<SnackbarMessage?> = MutableStateFlow(null)

    /**
     * Exposes the snackbar messages as an immutable state flow.
     */
    val snackbarMessages: StateFlow<SnackbarMessage?>
        get() = messages.asStateFlow()

    /**
     * Displays a snackbar message using a string resource ID.
     * @param message The string resource ID of the message.
     */
    fun showMessage(@StringRes message: Int) {
        messages.value = SnackbarMessage.ResourceSnackbar(message)
    }

    /**
     * Displays a snackbar message using a [SnackbarMessage] instance.
     * @param message The snackbar message to display.
     */
    fun showMessage(message: SnackbarMessage) {
        messages.value = message
    }

    /**
     * Displays a snackbar message from a throwable.
     * @param throwable The throwable to convert into a snackbar message.
     */
    fun showMessage(throwable: Throwable) {
        messages.value = throwable.toSnackbarMessage()
    }

    /**
     * Clears the current snackbar state, removing any visible messages.
     */
    fun clearSnackbarState() {
        messages.value = null
    }
}
