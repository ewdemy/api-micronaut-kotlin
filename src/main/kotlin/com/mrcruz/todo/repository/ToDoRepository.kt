package com.mrcruz.todo.repository

import com.mrcruz.todo.model.ToDo
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

@Repository
interface ToDoRepository: JpaRepository<ToDo, Long> {

    fun findByFeito(feito: Boolean, pageable: Pageable): Page<ToDo>
}