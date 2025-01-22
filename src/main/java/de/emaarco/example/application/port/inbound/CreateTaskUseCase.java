package de.emaarco.example.application.port.inbound;

public interface CreateTaskUseCase {
    void createTask(final CreateTaskCommand command);
    record CreateTaskCommand(String title, String description) {
    }
}
