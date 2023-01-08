package com.mrcruz.todo.repository

import com.mrcruz.todo.model.ToDo
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

@MicronautTest
class ToDoRepositoryTest {

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
    fun deveListarTodosOsToDosComFiltro() {
        val toDosTrue = toDoRepository.findByFeito(true, Pageable.from(0,20))

        assertEquals(3, toDosTrue.content.size)
        toDosTrue.forEach(){
            assertTrue(it.feito)
        }

        val toDosFalse = toDoRepository.findByFeito(false, Pageable.from(0,20))

        assertEquals(2, toDosFalse.content.size)
        toDosFalse.forEach(){
            assertFalse(it.feito)
        }
    }
}