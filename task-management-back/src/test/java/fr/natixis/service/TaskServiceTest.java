package fr.natixis.service;

import fr.natixis.core.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskService.init(); // Initialize with default tasks
    }

    @Test
    void getAllTasksShouldReturnAllTasksWithPagination() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Task> result = taskService.getAllTasks(pageRequest);
        
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getContent().size());
    }

    @Test
    void getTasksByCompletion_WithIncompleteTasks_ShouldReturnOnlyIncompleteTasks() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Task> result = taskService.getTasksByCompletion(false, pageRequest);
        
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().noneMatch(Task::isCompleted));
    }

    @Test
    void getTasksByCompletion_WithCompletedTasks_ShouldReturnOnlyCompletedTasks() {
        // Mark one task as completed
        taskService.updateTaskStatus(1L, true);
        
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Task> result = taskService.getTasksByCompletion(true, pageRequest);
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().stream().allMatch(Task::isCompleted));
    }

    @Test
    void getTaskById_WithExistingId_ShouldReturnTask() {
        var result = taskService.getTaskById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getTaskById_WithNonExistingId_ShouldReturnEmpty() {
        var result = taskService.getTaskById(999L);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void updateTaskStatus_WithExistingId_ShouldUpdateTaskAndReturnIt() {
        var result = taskService.updateTaskStatus(1L, true);
        
        assertTrue(result.isPresent());
        assertTrue(result.get().isCompleted());
    }

    @Test
    void updateTaskStatus_WithNonExistingId_ShouldReturnEmpty() {
        var result = taskService.updateTaskStatus(999L, true);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void addTaskShouldAssignIdAndSaveTask() {
        Task newTask = new Task(null, "New Task", "Description", false);
        Task result = taskService.addTask(newTask);
        
        assertNotNull(result.getId());
        assertEquals("New Task", result.getLabel());
        assertEquals("Description", result.getDescription());
        assertFalse(result.isCompleted());
    }
}