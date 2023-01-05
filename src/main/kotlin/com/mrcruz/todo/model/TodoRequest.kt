package com.mrcruz.todo.model

import io.micronaut.core.annotation.Introspected

@Introspected
data class TodoRequest(val descricao: String)
