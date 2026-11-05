package com.poulastaa.common.database

import com.poulastaa.common.domain.Log
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.common.domain.result.DataError
import com.poulastaa.common.domain.result.ErrorResponse
import kotlin.coroutines.cancellation.CancellationException

@PublishedApi
internal const val DATABASE_CALL_TAG = "DatabaseCall"

/**
 * Runs a local database operation and wraps the outcome in [AppResult].
 *
 * Cancellation is rethrown so coroutine cancellation still behaves correctly; any other failure is
 * logged and mapped to [DataError.General.SOMETHING_WENT_WRONG].
 */
suspend inline fun <RESPONSE> safeDbCall(
    block: suspend () -> RESPONSE,
): AppResult<RESPONSE, DataError> = try {
    AppResult.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    Log.e(DATABASE_CALL_TAG, e.message.orEmpty(), e)
    AppResult.Error(
        cause = e,
        error = ErrorResponse(error = DataError.General.SOMETHING_WENT_WRONG),
    )
}
