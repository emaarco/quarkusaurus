package de.emaarco.example.adapter.outbound;

import de.emaarco.example.application.port.outbound.LoadTasksPort;
import de.emaarco.example.application.port.outbound.SaveTaskPort;
import de.emaarco.example.domain.Task;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class InMemoryTaskRepository implements SaveTaskPort, LoadTasksPort {

    private final List<Task> taskList = new ArrayList<>();

    @Override
    public List<Task> loadTasks() {
        return taskList;
    }

    @Override
    public void saveTask(Task task) {
        taskList.add(task);
    }
}
