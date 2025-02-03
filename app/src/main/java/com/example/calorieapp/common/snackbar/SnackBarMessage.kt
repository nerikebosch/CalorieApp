package com.example.calorieapp.common.snackbar

import android.content.res.Resources
import androidx.annotation.StringRes
import com.example.calorieapp.R

/**
 * Sealed class representing different types of snackbar messages.
 */
sealed class SnackbarMessage {
    /**
     * Represents a snackbar message with a plain string.
     * @param message The message to be displayed.
     */
    class StringSnackbar(val message: String) : SnackbarMessage()

    /**
     * Represents a snackbar message using a string resource ID.
     * @param message The resource ID of the message.
     */
    class ResourceSnackbar(@StringRes val message: Int) : SnackbarMessage()

    companion object {
        /**
         * Converts a [SnackbarMessage] to a displayable string.
         * @param resources The resources instance to fetch string resources.
         * @return The string representation of the snackbar message.
         */
        fun SnackbarMessage.toMessage(resources: Resources): String {
            return when (this) {
                is StringSnackbar -> this.message
                is ResourceSnackbar -> resources.getString(this.message)
            }
        }

        /**
         * Converts a [Throwable] to a [SnackbarMessage].
         * If the throwable contains a non-empty message, it is used as a string snackbar message;
         * otherwise, a generic error message is used.
         * @return The corresponding [SnackbarMessage] instance.
         */
        fun Throwable.toSnackbarMessage(): SnackbarMessage {
            val message = this.message.orEmpty()
            return if (message.isNotBlank()) StringSnackbar(message)
            else ResourceSnackbar(R.string.generic_error)
        }
    }
}
