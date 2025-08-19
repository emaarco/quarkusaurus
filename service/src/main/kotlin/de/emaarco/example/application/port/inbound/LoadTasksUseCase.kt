package de.emaarco.example.application.port.inbound

import de.emaarco.example.domain.Task

interface LoadTasksUseCase {
    fun loadTasks(): List<Task>
} 