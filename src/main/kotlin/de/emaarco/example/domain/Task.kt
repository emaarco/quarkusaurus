package de.emaarco.example.domain

import java.util.UUID

data class Task(
    val id: String,
    val title: String,
    val description: String
) {
    constructor(title: String, description: String) : this(
        id = UUID.randomUUID().toString(),
        title = title,
        description = description
    )
} 