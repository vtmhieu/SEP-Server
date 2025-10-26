package group25.sep.server.service;

import java.util.List;

import group25.sep.server.model.Task;

public interface TaskService {
    Task createTask(Task task);
    Task getTaskById(Long id);

    List<Task> getTasksByStatus(String status);
    List<Task> getTasksBySubteam(String subteam);
    List<Task> getAllTasks();
    Task updateTaskStatus(Long id, String status);
    Task updateTaskComments(Long id, String comments);
    void deleteTask(Long id);
}
