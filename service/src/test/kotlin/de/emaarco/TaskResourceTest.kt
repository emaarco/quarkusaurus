package de.emaarco

import de.emaarco.example.adapter.outbound.PostgreSQLTaskRepository
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.hamcrest.CoreMatchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
open class TaskResourceTest {

    @Inject
    lateinit var taskRepository: PostgreSQLTaskRepository

    @BeforeEach
    @Transactional
    fun setUp() {
        taskRepository.deleteAll()
    }

    @Test
    fun testEndpointToLoadEmptyTasks() {
        given()
            .`when`().get("/tasks")
            .then()
            .statusCode(200)
            .body(`is`("[]"))
    }

    @Test
    fun testCreateAndLoadTasks() {
        // Create a task
        val taskRequest = """
            {
                "title": "Test Task",
                "description": "This is a test task"
            }
        """.trimIndent()

        given()
            .contentType(ContentType.JSON)
            .body(taskRequest)
            .`when`().post("/tasks")
            .then()
            .statusCode(200)

        // Verify the task is loaded
        given()
            .`when`().get("/tasks")
            .then()
            .statusCode(200)
            .body("size()", `is`(1))
            .body("[0].title", `is`("Test Task"))
            .body("[0].description", `is`("This is a test task"))
            .body("[0].id", notNullValue())
    }

    @Test
    fun testCreateMultipleTasks() {
        // Create first task
        val task1 = """
            {
                "title": "Task 1",
                "description": "First task"
            }
        """.trimIndent()

        // Create second task
        val task2 = """
            {
                "title": "Task 2", 
                "description": "Second task"
            }
        """.trimIndent()

        given()
            .contentType(ContentType.JSON)
            .body(task1)
            .`when`().post("/tasks")
            .then()
            .statusCode(200)

        given()
            .contentType(ContentType.JSON)
            .body(task2)
            .`when`().post("/tasks")
            .then()
            .statusCode(200)

        // Verify both tasks are loaded
        given()
            .`when`().get("/tasks")
            .then()
            .statusCode(200)
            .body("size()", `is`(2))
            .body("title", hasItems("Task 1", "Task 2"))
            .body("description", hasItems("First task", "Second task"))
    }

    @Test
    fun testCreateTaskWithEmptyTitle() {
        val taskRequest = """
            {
                "title": "",
                "description": "Task with empty title"
            }
        """.trimIndent()

        given()
            .contentType(ContentType.JSON)
            .body(taskRequest)
            .`when`().post("/tasks")
            .then()
            .statusCode(200)

        // Verify the task is still created (business logic validation would be handled at service layer)
        given()
            .`when`().get("/tasks")
            .then()
            .statusCode(200)
            .body("size()", `is`(1))
            .body("[0].title", `is`(""))
    }
} 