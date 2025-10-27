package group25.sep.server.controller;

import group25.sep.server.model.Task;
import group25.sep.server.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable String status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    @GetMapping("/subteam/{subteam}")
    public ResponseEntity<List<Task>> getTasksBySubteam(@PathVariable String subteam) {
        return ResponseEntity.ok(taskService.getTasksBySubteam(subteam));
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<Task> updatetaskStatus(@PathVariable Long id, @PathVariable String status) {
        Task updatedtask = taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(updatedtask);
    }

    @PutMapping("/{id}/comments/{comments}")
    public ResponseEntity<Task> updatetaskComments(@PathVariable Long id, @PathVariable String comments) {
        Task updatedtask = taskService.updateTaskComments(id, comments);
        return ResponseEntity.ok(updatedtask);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
