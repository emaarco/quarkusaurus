package de.emaarco.example.adapter.inbound

import de.emaarco.example.application.port.inbound.CreateTaskUseCase
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class CreateTaskController {

    @Inject
    lateinit var createTaskUseCase: CreateTaskUseCase

    @POST
    fun createTask(request: TaskRequest): Response {
        val command = CreateTaskUseCase.CreateTaskCommand(request.title, request.description)
        createTaskUseCase.createTask(command)
        return Response.ok(command).build()
    }

    data class TaskRequest(
        val title: String,
        val description: String
    )
} 