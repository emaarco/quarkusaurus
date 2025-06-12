package de.emaarco.example.application.service

import de.emaarco.example.application.port.inbound.LoadTasksUseCase
import de.emaarco.example.application.port.outbound.LoadTasksPort
import de.emaarco.example.domain.Task
import jakarta.enterprise.context.ApplicationScoped
import org.slf4j.LoggerFactory

@ApplicationScoped
class LoadTasksService(
    private val taskRepository: LoadTasksPort
) : LoadTasksUseCase {

    private val log = LoggerFactory.getLogger(LoadTasksService::class.java)

    override fun loadTasks(): List<Task> {
        log.info("Loading all tasks")
        return taskRepository.loadTasks()
    }
} 