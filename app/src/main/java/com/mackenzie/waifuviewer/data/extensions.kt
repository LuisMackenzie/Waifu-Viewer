package com.mackenzie.waifuviewer.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import retrofit2.HttpException
import com.mackenzie.waifuviewer.domain.Error
import java.io.IOException

fun Throwable.toError(): Error = when (this) {
    is IOException -> Error.Connectivity
    is HttpException -> Error.Server(code())
    else -> Error.Unknown(message ?: "")
}

suspend fun <T> tryCall(action: suspend () -> T): Either<Error, T> = try {
    action().right()
} catch (e: Exception) {
    e.toError().left()
}

suspend fun <T> trySave(action: suspend () -> T): T? = try {
    action()
} catch (e: Exception) {
    e.toError()
    null
}

suspend fun <T> tryGet(action: suspend () -> T): Error? = try {
    action()
    null
} catch (e: Exception) {
    e.toError()
}
