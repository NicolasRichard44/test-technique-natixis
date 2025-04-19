package fr.natixis.core.application;

import fr.natixis.core.domain.Task;
import fr.natixis.core.ports.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl(taskRepository);
    }

    @Test
    void testGetAllTasksWithPagination() {
        List<Task> tasks = List.of(
                Task.builder().id(1L).label("Task 1").description("Desc 1").completed(false).build(),
                Task.builder().id(2L).label("Task 2").description("Desc 2").completed(true).build());
        PageImpl<Task> page = new PageImpl<>(tasks);
        when(taskRepository.findAll(any(PageRequest.class))).thenReturn(page);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Task> result = taskService.getAllTasks(pageRequest);

        assertEquals(2, result.getTotalElements());
        assertEquals(tasks, result.getContent());
    }

    @Test
    void testGetTasksByCompletionWithPagination() {
        List<Task> tasks = List.of(
                Task.builder().id(1L).label("Task 1").description("Desc 1").completed(true).build());
        PageImpl<Task> page = new PageImpl<>(tasks);
        when(taskRepository.findByCompleted(true, PageRequest.of(0, 5))).thenReturn(page);

        Page<Task> result = taskService.getTasksByCompletion(true, PageRequest.of(0, 5));

        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).isCompleted());
    }

    @Test
    void testGetTaskById() {
        Task task = Task.builder()
                .id(1L)
                .label("Test Task")
                .description("Test Description")
                .completed(false)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals(task, result.get());
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<Task> result = taskService.getTaskById(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateStatus() {
        Task task = Task.builder()
                .id(1L)
                .label("Test")
                .description("Description")
                .completed(false)
                .build();

        when(taskRepository.updateStatus(1L, true))
                .thenReturn(Optional.of(task));

        Optional<Task> result = taskService.updateTaskStatus(1L, true);

        assertTrue(result.isPresent());
    }

    @Test
    void testUpdateStatusNonExistentTask() {
        when(taskRepository.updateStatus(999L, true))
                .thenReturn(Optional.empty());

        Optional<Task> result = taskService.updateTaskStatus(999L, true);

        assertTrue(result.isEmpty());
    }

    @Test
    void testAddTask() {
        Task taskToAdd = Task.builder()
                .label("Test Task")
                .description("Test Description")
                .completed(false)
                .build();

        Task savedTask = Task.builder()
                .id(1L)
                .label("Test Task")
                .description("Test Description")
                .completed(false)
                .build();

        when(taskRepository.save(taskToAdd)).thenReturn(savedTask);

        Task result = taskService.addTask(taskToAdd);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(taskToAdd.getLabel(), result.getLabel());
        assertEquals(taskToAdd.getDescription(), result.getDescription());
        assertEquals(taskToAdd.isCompleted(), result.isCompleted());
    }
}