package com.mrcruz.todo.model

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class ToDoRequestUpdate(@field:NotBlank val descricao: String, val feito: Boolean)