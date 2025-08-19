package de.emaarco.example.application.port.inbound

interface CreateTaskUseCase {
    fun createTask(command: CreateTaskCommand)

    data class CreateTaskCommand(
        val title: String,
        val description: String
    )
} 