package de.emaarco.example.application.service;

import de.emaarco.example.application.port.inbound.LoadTasksUseCase;
import de.emaarco.example.application.port.outbound.LoadTasksPort;
import de.emaarco.example.domain.Task;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class LoadTasksService implements LoadTasksUseCase {

    private final LoadTasksPort taskRepository;
    private final Logger log = LoggerFactory.getLogger(LoadTasksService.class);

    public LoadTasksService(LoadTasksPort taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> loadTasks() {
        this.log.info("Loading all tasks");
        return taskRepository.loadTasks();
    }
}
