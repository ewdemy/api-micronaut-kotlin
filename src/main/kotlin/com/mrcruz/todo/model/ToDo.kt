package com.mrcruz.todo.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class ToDo(var descricao: String) {
    @Id
    @GeneratedValue
    var id: Long? = null
    var feito: Boolean = false
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    val criadoEm: LocalDateTime = LocalDateTime.now()
}