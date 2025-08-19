package de.emaarco.example.application.service

import de.emaarco.example.application.port.inbound.LoadTasksUseCase
import de.emaarco.example.application.port.outbound.LoadTasksPort
import de.emaarco.example.domain.Task
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Named
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory

@ApplicationScoped
class LoadTasksService(
    @Named("postgresql") private val taskRepository: LoadTasksPort
) : LoadTasksUseCase {

    private val log = LoggerFactory.getLogger(LoadTasksService::class.java)

    @Transactional(Transactional.TxType.SUPPORTS)
    override fun loadTasks(): List<Task> {
        log.info("Loading all tasks")
        val tasks = taskRepository.loadTasks()
        log.info("Successfully loaded {} tasks", tasks.size)
        return tasks
    }
} 