package de.emaarco.example.application.service

import de.emaarco.example.application.port.inbound.CreateTaskUseCase
import de.emaarco.example.application.port.outbound.SaveTaskPort
import de.emaarco.example.domain.Task
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Named
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory

@ApplicationScoped
class CreateTaskService(
    @Named("postgresql") private val saveTaskPort: SaveTaskPort
) : CreateTaskUseCase {

    private val log = LoggerFactory.getLogger(CreateTaskService::class.java)

    @Transactional
    override fun createTask(command: CreateTaskUseCase.CreateTaskCommand) {
        log.info("Creating task with title: {}", command.title)
        val newTask = Task(command.title, command.description)
        saveTaskPort.saveTask(newTask)
        log.info("Successfully created task with ID: {}", newTask.id)
    }
} 