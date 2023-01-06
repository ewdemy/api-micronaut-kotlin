package com.mrcruz.todo.model

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class ToDoRequest(@field:NotBlank val descricao: String)
