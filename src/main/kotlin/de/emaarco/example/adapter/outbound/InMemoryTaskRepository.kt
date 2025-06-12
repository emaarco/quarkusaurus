package de.emaarco.example.adapter.outbound

import de.emaarco.example.application.port.outbound.LoadTasksPort
import de.emaarco.example.application.port.outbound.SaveTaskPort
import de.emaarco.example.domain.Task
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class InMemoryTaskRepository : SaveTaskPort, LoadTasksPort {

    private val taskList = mutableListOf<Task>()

    override fun loadTasks(): List<Task> = taskList

    override fun saveTask(task: Task) {
        taskList.add(task)
    }
} 