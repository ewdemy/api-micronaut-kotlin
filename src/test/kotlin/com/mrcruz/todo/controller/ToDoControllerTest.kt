package com.mrcruz.todo.controller

import com.mrcruz.todo.exception.ErrorMessage
import com.mrcruz.todo.model.ToDo
import com.mrcruz.todo.model.ToDoRequest
import com.mrcruz.todo.repository.ToDoRepository
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

@MicronautTest
internal class ToDoControllerTest {

    @field:Inject
    @field:Client("/api/v1/todo")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var toDoRepository: ToDoRepository

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun deveSalvarUmToDo() {
        val toDo = ToDoRequest("Fazer as compras")
        val response = client.toBlocking().exchange(HttpRequest.POST("/", toDo),ToDo::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertNotNull(response.body())
        assertNotNull(response.body()!!.id)
        assertEquals("Fazer as compras", response.body()!!.descricao)
        assertEquals(false, response.body()!!.feito)
    }

    @Test
    fun deveLancarExcecaoAoSalvarToDoSemDescricao() {
        val toDo = ToDoRequest("")

        var responseErro: ErrorMessage? = null
        val exception: HttpClientResponseException = assertThrows(HttpClientResponseException::class.java){
            responseErro = client.toBlocking().retrieve(HttpRequest.POST("/", toDo),ErrorMessage::class.java)
        }

            assertEquals(HttpStatus.BAD_REQUEST, exception.status)
            responseErro?.message?.let { assertTrue(it.contains("Campo deve ser preenchido")) }
    }


    @Test
    fun listar() {
    }

    @Test
    fun buscar() {
    }

    @Test
    fun atualizar() {
    }

    @Test
    fun deletar() {
    }
}