package de.emaarco.example.application.port.outbound;

import de.emaarco.example.domain.Task;

import java.util.List;

public interface LoadTasksPort {
    List<Task> loadTasks();
}
