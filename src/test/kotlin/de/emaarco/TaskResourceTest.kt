package de.emaarco

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
open class TaskResourceTest {
    @Test
    fun testEndpointToLoadTasks() {
        given()
            .`when`().get("/tasks")
            .then()
            .statusCode(200)
            .body(`is`("[]"))
    }
} 