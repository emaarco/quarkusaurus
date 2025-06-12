package de.emaarco.example.adapter.inbound

import de.emaarco.example.application.port.inbound.LoadTasksUseCase
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
class LoadTasksController {

    @Inject
    lateinit var loadTasksUseCase: LoadTasksUseCase

    @GET
    fun loadTasks(): Response {
        val tasks = loadTasksUseCase.loadTasks()
        return Response.ok(tasks).build()
    }
} 