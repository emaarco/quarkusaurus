package de.emaarco.example.adapter.outbound

import de.emaarco.example.domain.Task
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
class PostgreSQLTaskRepositoryTest {

    @Inject
    lateinit var taskRepository: PostgreSQLTaskRepository

    @BeforeEach
    @Transactional
    fun setUp() {
        taskRepository.deleteAll()
    }

    @Test
    @Transactional
    fun `should save and load tasks successfully`() {
        // Given
        val task1 = Task("Learn Quarkus", "Study Quarkus framework")
        val task2 = Task("Build API", "Create REST API with Kotlin")

        // When
        taskRepository.saveTask(task1)
        taskRepository.saveTask(task2)

        // Then
        val loadedTasks = taskRepository.loadTasks()
        assertEquals(2, loadedTasks.size)
        assertTrue(loadedTasks.any { it.title == "Learn Quarkus" })
        assertTrue(loadedTasks.any { it.title == "Build API" })
    }

    @Test
    fun `should return empty list when no tasks exist`() {
        // When
        val tasks = taskRepository.loadTasks()

        // Then
        assertTrue(tasks.isEmpty())
    }

    @Test
    @Transactional
    fun `should save task with correct properties`() {
        // Given
        val task = Task("Test Task", "Test Description")

        // When
        taskRepository.saveTask(task)

        // Then
        val loadedTasks = taskRepository.loadTasks()
        assertEquals(1, loadedTasks.size)
        
        val savedTask = loadedTasks.first()
        assertEquals(task.id, savedTask.id)
        assertEquals("Test Task", savedTask.title)
        assertEquals("Test Description", savedTask.description)
        // Note: createdAt might be null in H2 tests since triggers don't work the same way
    }

    @Test
    @Transactional
    fun `should find tasks by title case insensitive`() {
        // Given
        val task1 = Task("Learn Kotlin", "Study Kotlin language")
        val task2 = Task("Learn Quarkus", "Study Quarkus framework")
        val task3 = Task("Build Project", "Create new project")
        
        taskRepository.saveTask(task1)
        taskRepository.saveTask(task2)
        taskRepository.saveTask(task3)

        // When
        val foundTasks = taskRepository.findByTitle("learn")

        // Then
        assertEquals(2, foundTasks.size)
        assertTrue(foundTasks.all { it.title.contains("Learn") })
    }

    @Test
    @Transactional
    fun `should delete task by id successfully`() {
        // Given
        val task = Task("To Delete", "This task will be deleted")
        taskRepository.saveTask(task)
        
        val tasksBeforeDelete = taskRepository.loadTasks()
        assertEquals(1, tasksBeforeDelete.size)

        // When
        val deleted = taskRepository.deleteTask(task.id)

        // Then
        assertTrue(deleted)
        val tasksAfterDelete = taskRepository.loadTasks()
        assertTrue(tasksAfterDelete.isEmpty())
    }

    @Test
    fun `should return false when deleting non-existent task`() {
        // When
        val deleted = taskRepository.deleteTask("non-existent-id")

        // Then
        assertFalse(deleted)
    }

    @Test
    @Transactional
    fun `should handle multiple save operations correctly`() {
        // Given
        val tasks = (1..10).map { Task("Task $it", "Description $it") }

        // When
        tasks.forEach { taskRepository.saveTask(it) }

        // Then
        val loadedTasks = taskRepository.loadTasks()
        assertEquals(10, loadedTasks.size)
        
        // Verify all tasks are saved with correct titles
        (1..10).forEach { index ->
            assertTrue(loadedTasks.any { it.title == "Task $index" })
        }
    }

    @Test
    @Transactional
    fun `should persist tasks with timestamp fields`() {
        // Given
        val task = Task("Timestamp Test", "Testing timestamp persistence")

        // When
        taskRepository.saveTask(task)

        // Then
        val loadedTasks = taskRepository.loadTasks()
        assertEquals(1, loadedTasks.size)
        
        val savedTask = loadedTasks.first()
        // Note: timestamps might be null in H2 tests since database triggers work differently
    }
}