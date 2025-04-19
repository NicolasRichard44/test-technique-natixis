package fr.natixis.core.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void shouldCreateValidTask() {
        Task task = Task.builder()
                .id(1L)
                .label("Test Task")
                .description("Test Description")
                .completed(true)
                .build();

        assertEquals(1L, task.getId());
        assertEquals("Test Task", task.getLabel());
        assertEquals("Test Description", task.getDescription());
        assertTrue(task.isCompleted());
    }
}