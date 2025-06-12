package de.emaarco.example.application.port.outbound

import de.emaarco.example.domain.Task

interface LoadTasksPort {
    fun loadTasks(): List<Task>
} 