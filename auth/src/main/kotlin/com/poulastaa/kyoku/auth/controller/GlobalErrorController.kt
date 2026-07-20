package com.poulastaa.kyoku.auth.controller

import com.poulastaa.kyoku.auth.model.response.ResponseStatus
import com.poulastaa.kyoku.auth.model.response.ResponseWrapper
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GlobalErrorController : ErrorController {
    @RequestMapping("/error")
    fun handleError(request: HttpServletRequest): ResponseEntity<ResponseWrapper<Unit>> =
        ResponseWrapper<Unit>(
            status = request.errorHttpStatus().toResponseStatus(),
        ).toResponseEntity()

    private fun HttpServletRequest.errorHttpStatus(): HttpStatus {
        val statusCode = when (val value = getAttribute(RequestDispatcher.ERROR_STATUS_CODE)) {
            is Int -> value
            is String -> value.toIntOrNull()
            else -> null
        }

        return statusCode?.let(HttpStatus::resolve) ?: HttpStatus.INTERNAL_SERVER_ERROR
    }

    private fun HttpStatus.toResponseStatus() = when (this) {
        HttpStatus.BAD_REQUEST -> ResponseStatus.INVALID_REQUEST_BODY
        HttpStatus.UNAUTHORIZED -> ResponseStatus.UNAUTHORIZED
        HttpStatus.NOT_FOUND -> ResponseStatus.NOT_FOUND
        HttpStatus.METHOD_NOT_ALLOWED -> ResponseStatus.METHOD_NOT_ALLOWED
        HttpStatus.TOO_MANY_REQUESTS -> ResponseStatus.RATE_LIMITED
        HttpStatus.SERVICE_UNAVAILABLE -> ResponseStatus.SERVICE_UNAVAILABLE
        else -> ResponseStatus.INTERNAL_SERVER_ERROR
    }
}
