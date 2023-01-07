package com.mrcruz.todo.controller

import com.mrcruz.todo.model.ToDo
import com.mrcruz.todo.model.ToDoRequest
import com.mrcruz.todo.model.ToDoRequestUpdate
import com.mrcruz.todo.service.ToDoService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
import io.micronaut.validation.Validated
import javax.validation.Valid

@Validated
@Controller("/api/v1/todo")
class ToDoController(private val toDoService: ToDoService) {

    @Post
    fun salvar(@Body @Valid toDoRequest: ToDoRequest): HttpResponse<ToDo> {
        return HttpResponse.created(toDoService.salvar(toDoRequest))
    }

    @Get
    fun listar(@QueryValue feito: Boolean?, pageable: Pageable): HttpResponse<Page<ToDo>>{
        println(feito)
        return HttpResponse.ok(toDoService.listar(feito, pageable))
    }

    @Get("/{id}")
    fun buscar(@PathVariable id: Long): HttpResponse<ToDo> {
        return HttpResponse.ok(toDoService.buscar(id))
    }

    @Put("/{id}")
    fun atualizar(@PathVariable id: Long, @Body @Valid toDo: ToDoRequestUpdate): HttpResponse<ToDo> {
        println(toDo)
        return HttpResponse.ok(toDoService.atualizar(id, toDo))
    }

    @Delete("/{id}")
    fun deletar(@PathVariable id: Long): HttpResponse<Unit> {
        toDoService.deletar(id)
        return HttpResponse.noContent()
    }
}