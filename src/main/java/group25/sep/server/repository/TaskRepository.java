package group25.sep.server.repository;

import group25.sep.server.model.Task;
import group25.sep.server.model.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByStatusIn(List<String> statuses);
    List<Task> findBySubteam(String subteam);
}
