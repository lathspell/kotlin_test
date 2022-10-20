package de.lathspell.test.rest

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.net.URI

@ControllerAdvice
class WebMvcExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val body = ex.body
        body.type = URI.create("/problem/bad-request")
        body.detail = "Expected parameter: " + ex.parameterName
        return handleExceptionInternal(ex, body, headers, status, request)
    }

}
