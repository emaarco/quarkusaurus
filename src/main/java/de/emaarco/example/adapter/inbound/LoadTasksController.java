package de.emaarco.example.adapter.inbound;

import de.emaarco.example.application.port.inbound.CreateTaskUseCase;
import de.emaarco.example.application.port.inbound.CreateTaskUseCase.CreateTaskCommand;
import de.emaarco.example.application.port.inbound.LoadTasksUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
public class LoadTasksController {

    @Inject
    LoadTasksUseCase loadTasksUseCase;

    @GET
    public Response loadTasks() {
        final var tasks = loadTasksUseCase.loadTasks();
        return Response.ok(tasks).build();
    }
}
