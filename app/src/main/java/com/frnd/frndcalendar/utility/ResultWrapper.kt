package com.frnd.frndcalendar.utility

import androidx.annotation.Keep

@Keep
sealed class ResultWrapper<out T> {
    @Keep
    data class Success<out T>(val value: T) : ResultWrapper<T>()

    @Keep
    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null) : ResultWrapper<Nothing>()

    @Keep
    object NetworkError : ResultWrapper<Nothing>()

    @Keep
    object UserTokenNotFound : ResultWrapper<Nothing>()
}

@Keep
data class ErrorResponse(var success: Boolean? = null, var error: String? = null)