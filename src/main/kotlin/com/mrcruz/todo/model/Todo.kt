package com.mrcruz.todo.model

import java.time.LocalDateTime

class Todo(val descricao: String) {
    var id: Long? = null
    var feito: Boolean = false
    val criadoEm: LocalDateTime = LocalDateTime.now()
}