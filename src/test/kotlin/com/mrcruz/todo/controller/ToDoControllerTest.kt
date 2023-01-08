package com.mrcruz.todo.controller

import com.mrcruz.todo.exception.ErrorMessage
import com.mrcruz.todo.model.ToDo
import com.mrcruz.todo.model.ToDoRequest
import com.mrcruz.todo.model.ToDoRequestUpdate
import com.mrcruz.todo.repository.ToDoRepository
import io.micronaut.data.model.Page
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@MicronautTest
internal class ToDoControllerTest {

    @field:Inject
    @field:Client("/api/v1/todo")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var toDoRepository: ToDoRepository

    lateinit var toDo1: ToDo
    lateinit var toDo2: ToDo
    lateinit var toDo3: ToDo
    lateinit var toDo4: ToDo
    lateinit var toDo5: ToDo

    @BeforeEach
    fun setUp() {

        val t1 = ToDo("ToDo 1")
        t1.feito = true
        val t3 = ToDo("ToDo 3")
        t3.feito = true
        val t5 = ToDo("ToDo 5")
        t5.feito = true


        toDo1 = toDoRepository.save(t1)
        toDo2 = toDoRepository.save(ToDo("ToDo 2"))
        toDo3 = toDoRepository.save(t3)
        toDo4 = toDoRepository.save(ToDo("ToDo 4"))
        toDo5 = toDoRepository.save(t5)

    }

    @AfterEach
    fun tearDown() {
        toDoRepository.deleteAll()
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
    fun deveListarTodosOsToDos() {
        val response = client.toBlocking().exchange("/", Page::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(5,response.body().content.size)
    }

    @Test
    fun deveListarTodosOsToDosComFiltro() {
        val response = client.toBlocking().exchange("/?feito=true", Page::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(3,response.body().content.size)
    }

    @Test
    fun deveBuscarToDoPorId() {
       val response = client.toBlocking().exchange("/${toDo1.id}", ToDo::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(toDo1.id, response.body()!!.id)
        assertEquals("ToDo 1", response.body()!!.descricao)
    }

    @Test
    fun deveLancarExcecaoAoBuscarToDoComIdInexistente() {

        var responseErro: ErrorMessage? = null
        val exception: HttpClientResponseException = assertThrows(HttpClientResponseException::class.java){
            responseErro = client.toBlocking().retrieve("/10", ErrorMessage::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
        responseErro?.message?.let { assertTrue(it.contains("ToDo Não encontrado!")) }
    }

    @Test
    fun deveAtualizarToDo() {
        val toDo = ToDoRequestUpdate("Fazer as compras", true)
        val response = client.toBlocking().exchange(HttpRequest.PUT("/${toDo2.id}", toDo),ToDo::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(toDo2.id, response.body()!!.id)
        assertEquals("Fazer as compras", response.body()!!.descricao)
        assertEquals(true, response.body()!!.feito)
    }

    @Test
    fun deveLancarExcecaoAoAtualizarToDoSemDescricao() {
        val toDo = ToDoRequestUpdate("", true)

        var responseErro: ErrorMessage? = null
        val exception: HttpClientResponseException = assertThrows(HttpClientResponseException::class.java){
            responseErro = client.toBlocking().retrieve(HttpRequest.PUT("/${toDo2.id}", toDo),ErrorMessage::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
        responseErro?.message?.let { assertTrue(it.contains("Campo deve ser preenchido")) }
    }

    @Test
    fun deveLancarExcecaoAoAtualizarToDoComIdInexistente() {
        val toDo = ToDoRequestUpdate("Fazer as compras", true)
        var responseErro: ErrorMessage? = null
        val exception: HttpClientResponseException = assertThrows(HttpClientResponseException::class.java){
            responseErro = client.toBlocking().retrieve(HttpRequest.PUT("/20", toDo), ErrorMessage::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
        responseErro?.message?.let { assertTrue(it.contains("ToDo Não encontrado!")) }
    }

    @Test
    fun deveDeletarToDo() {
        val response = client.toBlocking().exchange(HttpRequest.DELETE("/${toDo1.id}",null), HttpResponse::class.java)

        assertEquals(HttpStatus.NO_CONTENT, response.status)
        assertNull(response.body())

        var responseErro: ErrorMessage? = null
        val exception: HttpClientResponseException = assertThrows(HttpClientResponseException::class.java){
            responseErro = client.toBlocking().retrieve("/${toDo1.id}", ErrorMessage::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
        responseErro?.message?.let { assertTrue(it.contains("ToDo Não encontrado!")) }
    }
    @Test
    fun deveLancarExecaooAoDeletarToDoComIdInexistente() {

        var responseErro: ErrorMessage? = null
        val exception: HttpClientResponseException = assertThrows(HttpClientResponseException::class.java){
            responseErro = client.toBlocking().retrieve(HttpRequest.DELETE("/10",null), ErrorMessage::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
        responseErro?.message?.let { assertTrue(it.contains("ToDo Não encontrado!")) }
    }
}