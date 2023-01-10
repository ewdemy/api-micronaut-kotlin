package com.mrcruz.todo.service

import com.mrcruz.todo.model.ToDo
import com.mrcruz.todo.model.ToDoRequest
import com.mrcruz.todo.repository.ToDoRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*
import javax.persistence.EntityNotFoundException
import javax.validation.ConstraintViolationException

@MicronautTest
internal class ToDoServiceTest {

    @Inject
    lateinit var  toDoService: ToDoService

    @Inject
    lateinit var toDoRepository: ToDoRepository


    @Test
    fun deveSalvarToDo() {
        val toDo = ToDo("ToDo 1")
        toDo.id = 1L

        Mockito.`when`(toDoRepository.save(any())).thenReturn(toDo)
        val toDoMock = toDoService.salvar(ToDoRequest("ToDo 1"))

        assertEquals(toDo.id, toDoMock.id)
        assertEquals(toDo.descricao, toDoMock.descricao)
        assertFalse(toDoMock.feito)
    }

    @Test
    fun deveLancarExcecaoAosalvarToDoSemDescricao() {
        Mockito.`when`(toDoRepository.save(any())).thenThrow(ConstraintViolationException::class.java)
        assertThrows(ConstraintViolationException::class.java){
            toDoService.salvar(ToDoRequest(""))
        }

    }


    @Test
    fun deveListarToDos() {
        val toDo1 = ToDo("ToDo 1")
        toDo1.id = 1L
        val toDo2 = ToDo("ToDo 2")
        toDo2.id = 2L

       val toDos = arrayListOf(toDo1,toDo2)
        val pageable = Pageable.from(0,10)

        Mockito.`when`(toDoRepository.findAll(any())).thenReturn(Page.of(toDos, pageable, 2))
        val toDosMock = toDoService.listar(null,pageable)

        assertNotNull(toDosMock!!.content)
        assertEquals(2, toDosMock.content.size)
    }

    @Test
    fun deveListarToDosComFiltro() {
        val toDo1 = ToDo("ToDo 1")
        toDo1.id = 1L
        toDo1.feito = true

        val toDos = arrayListOf(toDo1)
        val pageable = Pageable.from(0,10)

        Mockito.`when`(toDoRepository.findByFeito(true, pageable)).thenReturn(Page.of(toDos, pageable, 1))
        val toDosMock = toDoService.listar(true,pageable)

        assertNotNull(toDosMock!!.content)
        assertEquals(1, toDosMock.content.size)
        assertTrue(toDosMock.content[0].feito)
    }

    @Test
    fun deveBuscarToDo() {
        val toDo = ToDo("ToDo 1")
        toDo.id = 1L

        Mockito.`when`(toDoRepository.findById(any())).thenReturn(Optional.of(toDo))
        val toDoMock = toDoService.buscar(1L)

        assertEquals(toDo.id, toDoMock!!.id)
        assertEquals(toDo.descricao, toDoMock.descricao)
        assertFalse(toDoMock.feito)
    }

    @Test
    fun deveLancarExcecaoAoBuscarToDoComIdInexistente() {
        Mockito.`when`(toDoRepository.findById(any())).thenReturn(Optional.empty())
        assertThrows(EntityNotFoundException::class.java){
            toDoService.buscar(10L)
        }

    }

    @Test
    fun atualizar() {
    }

    @Test
    fun deletar() {

    }

    @MockBean(ToDoRepository::class)
    fun toDoRepository(): ToDoRepository {
        return Mockito.mock(ToDoRepository::class.java)
    }
}