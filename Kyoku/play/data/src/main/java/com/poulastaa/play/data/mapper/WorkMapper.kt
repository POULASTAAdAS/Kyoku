package com.poulastaa.play.data.mapper

import androidx.work.ListenableWorker
import com.poulastaa.core.domain.utils.DataError

fun DataError.Network.toWorkResult() = when (this) {
    DataError.Network.UNAUTHORISED,
    DataError.Network.EMAIL_NOT_VERIFIED,
    DataError.Network.PASSWORD_DOES_NOT_MATCH,
    DataError.Network.SERIALISATION,
    DataError.Network.UNKNOWN,
    DataError.Network.CONFLICT,
    -> ListenableWorker.Result.failure()

    DataError.Network.NOT_FOUND,
    DataError.Network.NO_INTERNET,
    DataError.Network.SERVER_ERROR,
    -> ListenableWorker.Result.retry()
}