package fr.natixis.api;

import fr.natixis.core.application.api.TaskService;
import fr.natixis.core.domain.Task;
import fr.natixis.api.dto.StatusUpdate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController controller;

    @Test
    void testGetTasksWithPagination() {
        List<Task> tasks = List.of(
            Task.builder().id(1L).label("Test1").description("Desc1").completed(false).build(),
            Task.builder().id(2L).label("Test2").description("Desc2").completed(true).build()
        );
        Page<Task> page = new PageImpl<>(tasks, PageRequest.of(0, 10), 2);
        when(taskService.getAllTasks(any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<Task>> response = controller.getTasks(null, 0, 10);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    void testGetTasksWithCompletionFilter() {
        List<Task> tasks = List.of(
            Task.builder().id(1L).label("Test1").description("Desc1").completed(true).build()
        );
        Page<Task> page = new PageImpl<>(tasks, PageRequest.of(0, 10), 1);
        when(taskService.getTasksByCompletion(true, PageRequest.of(0, 10))).thenReturn(page);

        ResponseEntity<Page<Task>> response = controller.getTasks(true, 0, 10);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(taskService).getTasksByCompletion(true, PageRequest.of(0, 10));
    }

    @Test
    void testGetEmptyTaskList() {
        Page<Task> emptyPage = new PageImpl<>(Collections.emptyList());
        when(taskService.getAllTasks(any(PageRequest.class))).thenReturn(emptyPage);

        ResponseEntity<Page<Task>> response = controller.getTasks(null, 0, 10);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getTotalElements());
        assertTrue(response.getBody().getContent().isEmpty());
    }

    @Test
    void testGetTasksWithNegativePage() {
        ResponseEntity<Page<Task>> response = controller.getTasks(null, -1, 10);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskService).getAllTasks(PageRequest.of(0, 10)); // Should default to page 0
    }

    @Test
    void testGetTaskByIdFound() {
        Task task = Task.builder()
            .id(1L)
            .label("Test")
            .description("Desc")
            .completed(false)
            .build();
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));
        
        ResponseEntity<Task> response = controller.getTask(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskService.getTaskById(2L)).thenReturn(Optional.empty());
        ResponseEntity<Task> response = controller.getTask(2L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAddTask() {
        Task task = Task.builder()
            .label("Test")
            .description("Desc")
            .completed(false)
            .build();
        Task saved = Task.builder()
            .id(1L)
            .label("Test")
            .description("Desc")
            .completed(false)
            .build();
            
        when(taskService.addTask(task)).thenReturn(saved);
        
        Task result = controller.addTask(task);
        assertNotNull(result.getId());
        assertEquals(saved, result);
    }

    @Test
    void testAddTaskWithNullLabel() {
        Task task = Task.builder()
            .description("Desc")
            .completed(false)
            .build();
            
        Task saved = Task.builder()
            .id(1L)
            .description("Desc")
            .completed(false)
            .build();
            
        when(taskService.addTask(task)).thenReturn(saved);
        
        Task result = controller.addTask(task);
        assertNotNull(result.getId());
        assertNull(result.getLabel());
    }

    @Test
    void testUpdateStatusWithDTO() {
        Task updated = Task.builder()
            .id(1L)
            .label("Test")
            .description("Desc")
            .completed(true)
            .build();
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setCompleted(true);
        
        when(taskService.updateTaskStatus(1L, true)).thenReturn(Optional.of(updated));
        
        ResponseEntity<Task> response = controller.updateStatus(1L, statusUpdate);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isCompleted());
    }

    @Test
    void testUpdateStatusNotFound() {
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setCompleted(true);
        
        when(taskService.updateTaskStatus(999L, true)).thenReturn(Optional.empty());
        
        ResponseEntity<Task> response = controller.updateStatus(999L, statusUpdate);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateStatusWithNullDTO() {
        ResponseEntity<Task> response = controller.updateStatus(1L, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}