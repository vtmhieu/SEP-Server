package group25.sep.server.service;

import group25.sep.server.model.Task;
import group25.sep.server.model.enums.TaskStatus;
import group25.sep.server.repository.TaskRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService{

     private final TaskRepository taskRepository;

     public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
     }

     @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + id));
    }
    @Override
    public List<Task> getTasksByStatus(String status) {
        try {
            TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
            List<Task> tasks = taskRepository.findByStatus(taskStatus);

            if (tasks.isEmpty()) {
                throw new RuntimeException("No tasks found with status: " + status);
            }

            return tasks;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid task status: " + status, e);
        }
    }

    @Override
    public List<Task> getTasksBySubteam(String subteam) {
        try {
            List<Task> tasks = taskRepository.findBySubteam(subteam);

            if (tasks.isEmpty()) {
                throw new RuntimeException("No tasks found under subteam: " + subteam);
            }

            return tasks;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid subteam: " + subteam, e);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task updateTaskStatus(Long id, String status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        try {
            TaskStatus newStatus = TaskStatus.valueOf(status.toUpperCase());
            task.setStatus(newStatus);
            return taskRepository.save(task);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid task status: " + status, e);
        }
    }
    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

}
