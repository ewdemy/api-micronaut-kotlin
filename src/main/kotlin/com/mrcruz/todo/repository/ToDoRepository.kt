package com.mrcruz.todo.repository

import com.mrcruz.todo.model.ToDo
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ToDoRepository: JpaRepository<ToDo, Long> {
}