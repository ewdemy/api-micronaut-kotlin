package com.mrcruz.todo.service

import com.mrcruz.todo.model.ToDo
import com.mrcruz.todo.model.ToDoRequest
import com.mrcruz.todo.repository.ToDoRepository
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import javax.persistence.EntityNotFoundException
import javax.validation.ConstraintViolationException

@MicronautTest
internal class ToDoServiceTest {

    @Inject
    lateinit var  toDoService: ToDoService

    @Inject
    lateinit var toDoRepository: ToDoRepository


    @Test
    fun salvar() {
        val toDo = ToDo("ToDo 1")
        toDo.id = 1L

        Mockito.`when`(toDoRepository.save(any())).thenReturn(toDo)
        val toDoMock = toDoService.salvar(ToDoRequest("ToDo 1"))

        assertEquals(toDo.id, toDoMock.id)
        assertEquals(toDo.descricao, toDoMock.descricao)
        assertFalse(toDoMock.feito)
    }

    @Test
    fun salvarException() {
        Mockito.`when`(toDoRepository.save(any())).thenThrow(ConstraintViolationException::class.java)
        assertThrows(ConstraintViolationException::class.java){
            toDoService.salvar(ToDoRequest(""))
        }

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

    @MockBean(ToDoRepository::class)
    fun toDoRepository(): ToDoRepository {
        return Mockito.mock(ToDoRepository::class.java)
    }
}