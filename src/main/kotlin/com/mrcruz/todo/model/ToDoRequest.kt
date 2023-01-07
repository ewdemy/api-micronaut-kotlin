package com.mrcruz.todo.model

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class ToDoRequest(@field:NotBlank(message = "Campo deve ser preenchido!") val descricao: String)
