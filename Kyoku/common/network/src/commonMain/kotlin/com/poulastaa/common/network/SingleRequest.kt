package com.poulastaa.common.network

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

class SingleRequest<RESPONSE> {
    private val mutex = Mutex()
    private var inProgress: CompletableDeferred<Result<RESPONSE>>? = null
    private var duplicateCount: Int = 0

    val iActive = mutex.isLocked || inProgress != null

    suspend fun run(
        reStart: Boolean,
        context: CoroutineContext,
        block: suspend () -> RESPONSE,
    ) = request(reStart, context, block).onFailure {
        // print out error
    }.getOrNull()

    private suspend fun request(
        reStart: Boolean,
        context: CoroutineContext,
        block: suspend () -> RESPONSE,
    ) = withContext(context) {
        mutex.lock()

        if (reStart) {
            inProgress?.cancel()
            inProgress = null
        }

        val job = inProgress

        if (job !== null) {
            duplicateCount++
            mutex.unlock()

            // the explicit join() makes the “wait for producer, then clean up waiter count, then read result” sequence clearer
            job.join()

            mutex.withLock {
                duplicateCount--
                if (duplicateCount == 0) inProgress = null

                return@withContext job.await()
            }
        }

        val deferredResult = CompletableDeferred<Result<RESPONSE>>()
        inProgress = deferredResult
        mutex.unlock()

        safeRun { block() }.let { deferredResult.complete(it) }
        val result = deferredResult.await()

        mutex.withLock {
            if (duplicateCount == 0) inProgress = null
        }

        result
    }

    private inline fun <R> safeRun(block: () -> R): Result<R> {
        return try {
            Result.success(block())
        } catch (t: TimeoutCancellationException) {
            Result.failure(t)
        } catch (c: CancellationException) {
            throw c
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }
}