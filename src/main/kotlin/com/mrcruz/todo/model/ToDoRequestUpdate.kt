package com.mrcruz.todo.model

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class ToDoRequestUpdate(@field:NotBlank(message = "Campo deve ser preenchido!") val descricao: String, val feito: Boolean)