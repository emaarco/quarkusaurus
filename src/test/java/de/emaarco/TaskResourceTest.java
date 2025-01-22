package de.emaarco;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class TaskResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
                .when().get("/tasks")
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

}