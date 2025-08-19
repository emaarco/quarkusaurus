package de.emaarco.example.domain

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tasks")
data class Task(
    @Id
    @Column(name = "id", nullable = false)
    val id: String,
    
    @Column(name = "title", nullable = false)
    val title: String,
    
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    val description: String,
    
    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null,
    
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null
) {
    constructor(title: String, description: String) : this(
        id = UUID.randomUUID().toString(),
        title = title,
        description = description,
        createdAt = null,
        updatedAt = null
    )
    
    // JPA requires a no-arg constructor
    constructor() : this("", "", "", null, null)
} 