package de.emaarco.example.adapter.inbound;

import de.emaarco.example.application.port.inbound.CreateTaskUseCase;
import de.emaarco.example.application.port.inbound.CreateTaskUseCase.CreateTaskCommand;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CreateTaskController {

    @Inject
    CreateTaskUseCase createTaskUseCase;

    @POST
    public Response createTask(final TaskRequest request) {
        final var command = new CreateTaskCommand(request.title(), request.description());
        createTaskUseCase.createTask(command);
        return Response.ok(command).build();
    }

    public record TaskRequest(String title, String description) {
    }
}
