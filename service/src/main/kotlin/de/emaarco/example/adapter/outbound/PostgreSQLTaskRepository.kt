package de.emaarco.example.adapter.outbound

import de.emaarco.example.application.port.outbound.LoadTasksPort
import de.emaarco.example.application.port.outbound.SaveTaskPort
import de.emaarco.example.domain.Task
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Named
import org.slf4j.LoggerFactory

@ApplicationScoped
@Named("postgresql")
class PostgreSQLTaskRepository : PanacheRepository<Task>, SaveTaskPort, LoadTasksPort {

    private val log = LoggerFactory.getLogger(PostgreSQLTaskRepository::class.java)

    override fun saveTask(task: Task) {
        try {
            log.info("Saving task with ID: {} and title: {}", task.id, task.title)
            persist(task)
            log.info("Successfully saved task with ID: {}", task.id)
        } catch (e: Exception) {
            log.error("Failed to save task with ID: {}", task.id, e)
            throw RuntimeException("Failed to save task", e)
        }
    }

    override fun loadTasks(): List<Task> {
        return try {
            log.info("Loading all tasks from database")
            val tasks = listAll()
            log.info("Successfully loaded {} tasks", tasks.size)
            tasks
        } catch (e: Exception) {
            log.error("Failed to load tasks from database", e)
            throw RuntimeException("Failed to load tasks", e)
        }
    }

    fun findByTitle(title: String): List<Task> {
        return try {
            log.info("Finding tasks with title containing: {}", title)
            list("title ILIKE ?1", "%$title%")
        } catch (e: Exception) {
            log.error("Failed to find tasks by title: {}", title, e)
            throw RuntimeException("Failed to find tasks by title", e)
        }
    }

    fun deleteTask(taskId: String): Boolean {
        return try {
            log.info("Deleting task with ID: {}", taskId)
            val task = find("id", taskId).firstResult()
            val deleted = if (task != null) {
                delete(task)
                true
            } else {
                log.warn("Task with ID: {} not found for deletion", taskId)
                false
            }
            if (deleted) {
                log.info("Successfully deleted task with ID: {}", taskId)
            }
            deleted
        } catch (e: Exception) {
            log.error("Failed to delete task with ID: {}", taskId, e)
            throw RuntimeException("Failed to delete task", e)
        }
    }

    fun findByIdString(taskId: String): Task? {
        return try {
            log.info("Finding task with ID: {}", taskId)
            find("id", taskId).firstResult()
        } catch (e: Exception) {
            log.error("Failed to find task with ID: {}", taskId, e)
            null
        }
    }
}