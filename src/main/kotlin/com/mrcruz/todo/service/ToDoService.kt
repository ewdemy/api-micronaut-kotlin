package com.mrcruz.todo.service

import com.mrcruz.todo.model.ToDo
import com.mrcruz.todo.model.ToDoRequest
import com.mrcruz.todo.model.ToDoRequestUpdate
import com.mrcruz.todo.repository.ToDoRepository
import jakarta.inject.Singleton
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Singleton
open class ToDoService(private val toDoRepository: ToDoRepository) {
    fun salvar(toDoRequest: ToDoRequest): ToDo {
        val toDo = ToDo(toDoRequest.descricao)
        return toDoRepository.save(toDo)
    }

    fun listar(): List<ToDo>? {
        return toDoRepository.findAll()
    }

    fun buscar(id: Long): ToDo? {
        return toDoRepository.findById(id).orElseThrow{EntityNotFoundException("ToDo Não encontrado!")}
    }

    @Transactional
    open fun atualizar(id: Long, toDo: ToDoRequestUpdate): ToDo? {
        val toDoDb = buscar(id)
        toDoDb?.descricao = toDo.descricao
        toDoDb?.feito = toDo.feito
        return toDoDb?.let { toDoRepository.update(it) }

    }

    fun deletar(id: Long): Any {
        val toDoDb = buscar(id)
        return toDoRepository.delete(toDoDb!!)
    }


}