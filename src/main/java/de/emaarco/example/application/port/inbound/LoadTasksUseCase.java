package de.emaarco.example.application.port.inbound;

import de.emaarco.example.domain.Task;

import java.util.List;

public interface LoadTasksUseCase {
    List<Task> loadTasks();
}
