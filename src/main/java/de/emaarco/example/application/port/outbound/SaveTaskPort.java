package de.emaarco.example.application.port.outbound;

import de.emaarco.example.domain.Task;

public interface SaveTaskPort {
    void saveTask(final Task task);
}
