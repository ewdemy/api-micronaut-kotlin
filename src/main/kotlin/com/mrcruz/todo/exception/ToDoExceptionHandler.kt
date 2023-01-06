package com.mrcruz.todo.exception

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Error
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton
import javax.persistence.EntityNotFoundException

@Singleton
@Requires(classes = [EntityNotFoundException::class, ExceptionHandler::class, Exception::class])
class ToDoExceptionHandler: ExceptionHandler<Exception, HttpResponse<*>> {

    @Error(global = true, exception = Exception::class)
    override fun handle(request: HttpRequest<*>?, exception: Exception?): HttpResponse<ErrorMessage> {
        var err = ErrorMessage()
        err.path = request?.path
        when(exception){
            is EntityNotFoundException -> {
                err.message = exception.message
                err.status = 404
                return HttpResponse.notFound(err)
            }
        }
        err.message = exception?.message
        err.status = 500
        return HttpResponse.serverError(err)
    }
}