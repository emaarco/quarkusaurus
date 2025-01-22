package de.emaarco.example.application.service;

import de.emaarco.example.application.port.inbound.CreateTaskUseCase;
import de.emaarco.example.application.port.outbound.SaveTaskPort;
import de.emaarco.example.domain.Task;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class CreateTaskService implements CreateTaskUseCase {

    private final SaveTaskPort saveTaskPort;
    private final Logger log = LoggerFactory.getLogger(CreateTaskService.class);

    public CreateTaskService(SaveTaskPort saveTaskPort) {
        this.saveTaskPort = saveTaskPort;
    }

    @Override
    public void createTask(final CreateTaskCommand command) {
        this.log.info("Creating task with title: {}", command.title());
        final Task newTask = new Task(command.title(), command.description());
        saveTaskPort.saveTask(newTask);
    }
}
