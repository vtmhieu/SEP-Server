package group25.sep.server.service;

import group25.sep.server.model.Task;
import group25.sep.server.model.enums.TaskPriority;
import group25.sep.server.model.enums.TaskStatus;
import group25.sep.server.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;

    // @InjectMocks creates an instance of RecruitmentRequestServiceImpl
    // and injects the mocked repository into it
    @InjectMocks
    private TaskServiceImpl taskService;

    // Test data
    private Task testTask;
    private Task savedTask;
    private Task secondSavedTask;
    private Task thirdSavedTask;
    private Task updatedTask;
    private Task updatedComments;

    /**
     * @BeforeEach runs before each test method
     *             This is where we set up our test data
     */
    @BeforeEach
    void setUp() {
        // Create a test recruitment request
        testTask = Task.builder()
                .eventId(1L)
                .projectReference("AI Summit")
                .description("Manage the food and make sure it's stocked for the event")
                .assignee("Mary")
                .subteam("Food")
                .comments("Doable, with no budget concerns")
                .priority(TaskPriority.CRITICAL)
                .status(TaskStatus.IN_PROGRESS)
                .build();

        // Create a saved version with ID (simulating what comes back from database)
        savedTask = Task.builder()
                .id(1L)
                .eventId(1L)
                .projectReference("AI Summit")
                .description("Manage the food and make sure it's stocked for the event")
                .assignee("Mary")
                .subteam("Food")
                .comments("Doable, with no budget concerns")
                .priority(TaskPriority.CRITICAL)
                .status(TaskStatus.IN_PROGRESS)
                .build();

        secondSavedTask = Task.builder()
                .id(2L)
                .eventId(1L)
                .projectReference("AI Summit")
                .description("Manage the tables and make sure we have enough catering")
                .assignee("Tom")
                .subteam("Food")
                .comments("Have enough tables in the budget for the event")
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.IN_PROGRESS)
                .build();

        thirdSavedTask = Task.builder()
                .id(3L)
                .eventId(2L)
                .projectReference("Developer Conference")
                .description("Need to make sure we have enough cutlery and silverware")
                .assignee("Tom")
                .subteam("Food")
                .comments("")
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.CREATED)
                .build();

        updatedTask = savedTask = Task.builder()
                .id(1L)
                .eventId(1L)
                .projectReference("AI Summit")
                .description("Manage the food and make sure it's stocked for the event")
                .assignee("Mary")
                .subteam("Food")
                .comments("Doable, with no budget concerns")
                .priority(TaskPriority.CRITICAL)
                .status(TaskStatus.NEEDS_MANAGER_REVIEW)
                .build();

        updatedComments = savedTask = Task.builder()
                .id(1L)
                .eventId(1L)
                .projectReference("AI Summit")
                .description("Manage the food and make sure it's stocked for the event")
                .assignee("Mary")
                .subteam("Food")
                .comments("It seems we may not have enough cooks, might need to put in a hire request")
                .priority(TaskPriority.CRITICAL)
                .status(TaskStatus.IN_PROGRESS)
                .build();
    }

    @Test
    void createTask_WithValidRequest_ShouldReturnSavedTask() {
        // ARRANGE 
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // ACT (
        Task result = taskService.createTask(testTask);

        //ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getProjectReference()).isEqualTo("AI Summit");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        verify(taskRepository, times(1)).save(testTask);
    }

    @Test void getTaskById_InCaseTaskIdExists_ShouldReturnTask() {
        // ARRANGE 
        Long validTaskID = 1L;
        when(taskRepository.findById(validTaskID)).thenReturn(Optional.of(savedTask));

        //ACT 
        Task result = taskService.getTaskById(validTaskID);

        //ASSERT

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getProjectReference()).isEqualTo("AI Summit");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        verify(taskRepository, times(1)).findById(validTaskID);

    }

    @Test void getTaskById_InCaseTaskIdDoesntExist_ShouldThrowException() {
        // ARRANGE
        Long invalidID = 2L;
        when(taskRepository.findById(not(eq(1L)))).thenThrow(new RuntimeException("Task not found with id"));

        //ACT/ASSERT
        assertThrows(RuntimeException.class, () -> taskService.getTaskById(invalidID));
    }

    @Test void getTasksByStatus_InCaseTaskStatusExists_ShouldReturnTaskList() {
        // ARRANGE 
        String validstatus = "in_progress";
        List<Task> returnedTasks = new ArrayList<>();
        returnedTasks.add(savedTask);
        returnedTasks.add(secondSavedTask);

        when(taskRepository.findByStatus(TaskStatus.valueOf(validstatus.toUpperCase()))).thenReturn(returnedTasks);

        //ACT 
        List<Task> result = taskService.getTasksByStatus(validstatus);

        //ASSERT

        assertThat(result).isNotNull();
        assertThat(result.size() == 2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(0).getProjectReference()).isEqualTo("AI Summit");
        assertThat(result.get(1).getProjectReference()).isEqualTo("AI Summit");
        assertThat(result.get(0).getPriority()).isEqualTo(TaskPriority.CRITICAL);
        assertThat(result.get(1).getPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(result.get(0).getAssignee()).isEqualTo("Mary");
        assertThat(result.get(1).getAssignee()).isEqualTo("Tom");
        verify(taskRepository, times(1)).findByStatus(TaskStatus.valueOf(validstatus.toUpperCase()));

    }

    @Test void getTaskByStatus_InCaseTaskStatusNotAppearing_ShuoldThrowException() {
        // ARRANGE 
        String notAppearingStatus = "created";
        when (taskRepository.findByStatus(TaskStatus.valueOf(notAppearingStatus.toUpperCase()))).thenThrow(new RuntimeException("No tasks found with status"));

        //ACT/Assert
        assertThrows(RuntimeException.class, () -> taskService.getTasksByStatus(notAppearingStatus));
    }

    @Test void getTaskByStatus_InCaseTaskStatusDoesntExist_ShouldThrowException() {
        // ARRANGE 
        String invalidStatus = "finished";

        //ACT/Assert
        assertThrows(RuntimeException.class, () -> taskService.getTasksByStatus(invalidStatus));
    }

    @Test void getTasksBySubteam_InCaseTaskSubteamExists_ShouldReturnTaskList() {
        // ARRANGE 
        String validsubteam = "food";
        List<Task> returnedTasks = new ArrayList<>();
        returnedTasks.add(savedTask);
        returnedTasks.add(secondSavedTask);

        when(taskRepository.findBySubteam(validsubteam)).thenReturn(returnedTasks);

        //ACT 
        List<Task> result = taskService.getTasksBySubteam(validsubteam);

        //ASSERT

        assertThat(result).isNotNull();
        assertThat(result.size() == 2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(0).getProjectReference()).isEqualTo("AI Summit");
        assertThat(result.get(1).getProjectReference()).isEqualTo("AI Summit");
        assertThat(result.get(0).getPriority()).isEqualTo(TaskPriority.CRITICAL);
        assertThat(result.get(1).getPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(result.get(0).getAssignee()).isEqualTo("Mary");
        assertThat(result.get(1).getAssignee()).isEqualTo("Tom");
        verify(taskRepository, times(1)).findBySubteam(validsubteam);
    }

    @Test void getTaskByStatus_InCaseTaskSubteamNotAppearing_ShouldThrowException() {
        // ARRANGE 
        String notAppearingSubteam = "music";
        when (taskRepository.findBySubteam(notAppearingSubteam)).thenThrow(new RuntimeException("No tasks found under subteam"));

        //ACT/Assert
        assertThrows(RuntimeException.class, () -> taskService.getTasksBySubteam(notAppearingSubteam));
    }

    @Test void getTaskByStatus_InCaseTaskSubteamDoesntExist_ShouldThrowException() {
        // ARRANGE 
        String notAppearingSubteam = null;

        //ACT/Assert
        assertThrows(RuntimeException.class, () -> taskService.getTasksBySubteam(notAppearingSubteam));
    }

    @Test void getAllTasks_ShouldReturnAllTasks() {
        // ARRANGE
        List<Task> returnedTasks = new ArrayList<>();
        returnedTasks.add(savedTask);
        returnedTasks.add(secondSavedTask);
        returnedTasks.add(thirdSavedTask);
        when(taskRepository.findAll()).thenReturn(returnedTasks);

        //ACT 
        List<Task> result = taskService.getAllTasks();

        //ASSERT

        assertThat(result).isNotNull();
        assertThat(result.size() == 2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(2).getId()).isEqualTo(3L);
        assertThat(result.get(0).getProjectReference()).isEqualTo("AI Summit");
        assertThat(result.get(1).getProjectReference()).isEqualTo("AI Summit");
        assertThat(result.get(2).getProjectReference()).isEqualTo("Developer Conference");
        assertThat(result.get(0).getPriority()).isEqualTo(TaskPriority.CRITICAL);
        assertThat(result.get(1).getPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(result.get(2).getPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(result.get(0).getAssignee()).isEqualTo("Mary");
        assertThat(result.get(1).getAssignee()).isEqualTo("Tom");
        assertThat(result.get(2).getAssignee()).isEqualTo("Tom");
        verify(taskRepository, times(1)).findAll();
    }

    @Test 
    void updateTaskStatusInCaseOfValidArguments_ShouldUpdateTaskStatus() {
        //ARRANGE
        Long validID = 1L;
        String validStatus = "needs_manager_review";

        when(taskRepository.findById(validID)).thenReturn(Optional.of(savedTask));
        when(taskRepository.save(argThat(task -> task.getStatus() == TaskStatus.NEEDS_MANAGER_REVIEW))).thenReturn(updatedTask);

        //ACT
        Task result = taskService.updateTaskStatus(validID, validStatus);

        //ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getProjectReference()).isEqualTo("AI Summit");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.NEEDS_MANAGER_REVIEW);
        assertThat(result.getPriority()).isEqualTo((TaskPriority.CRITICAL));
        verify(taskRepository, times(1)).findById(validID);
        verify(taskRepository, times(1)).save(argThat(task -> task.getStatus() == TaskStatus.NEEDS_MANAGER_REVIEW));
    }

    @Test void updateTaskStatus_InCaseTaskIdDoesntExist_ShouldThrowException() {
        // ARRANGE
        Long invalidID = 2L;
        String validStatus = "in_progress";
        when(taskRepository.findById(not(eq(1L)))).thenThrow(new RuntimeException("Task not found with id"));

        //ACT/ASSERT
        assertThrows(RuntimeException.class, () -> taskService.updateTaskStatus(invalidID, validStatus));
    }

    @Test void updateTaskStatus_InCaseOfInvalidStatus_ShouldThrowException() {
        // ARRANGE
        Long validID = 1L;
        String invalidStatus = "finished";
        when(taskRepository.findById(validID)).thenReturn(Optional.of(savedTask));

        //ACT/ASSERT
        assertThrows(RuntimeException.class, () -> taskService.updateTaskStatus(validID, invalidStatus));
    }

    @Test void updateTaskCommentsInCaseOfValidArguments_ShouldUpdateTaskComments() {
        //ARRANGE
        Long validID = 1L;
        String validComments = "It seems we may not have enough cooks, might need to put in a hire request";
        when(taskRepository.findById(validID)).thenReturn(Optional.of(savedTask));
        when(taskRepository.save(argThat(task -> task.getComments() == validComments))).thenReturn(updatedComments);

        //ACT
        Task result = taskService.updateTaskComments(validID, validComments);

        //ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getProjectReference()).isEqualTo("AI Summit");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(result.getPriority()).isEqualTo((TaskPriority.CRITICAL));
        assertThat(result.getComments()).isEqualTo(validComments);
        verify(taskRepository, times(1)).findById(validID);
        verify(taskRepository, times(1)).save(updatedComments);
    }

    @Test void updateTaskComments_InCaseTaskIdDoesntExist_ShouldThrowException() {
        // ARRANGE
        Long invalidID = 2L;
        String validComments = "It seems we may not have enough cooks, might need to put in a hire request";
        when(taskRepository.findById(not(eq(1L)))).thenThrow(new RuntimeException("Task not found with id"));

        //ACT/ASSERT
        assertThrows(RuntimeException.class, () -> taskService.updateTaskComments(invalidID, validComments));
    }
}
