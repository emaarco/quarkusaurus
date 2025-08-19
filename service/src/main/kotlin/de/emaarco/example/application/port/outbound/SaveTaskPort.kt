package de.emaarco.example.application.port.outbound

import de.emaarco.example.domain.Task

interface SaveTaskPort {
    fun saveTask(task: Task)
} 