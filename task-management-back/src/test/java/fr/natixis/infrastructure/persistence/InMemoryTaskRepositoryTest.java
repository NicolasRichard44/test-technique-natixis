package fr.natixis.infrastructure.persistence;

import fr.natixis.core.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskRepositoryTest {

    private InMemoryTaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();
        repository.init();
    }

    @Test
    void testInit() {
        Page<Task> allTasks = repository.findAll(PageRequest.of(0, 10));
        assertEquals(2, allTasks.getTotalElements());
    }

    @Test
    void testFindAll() {
        Page<Task> result = repository.findAll(PageRequest.of(0, 10));
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertFalse(result.getContent().isEmpty());
    }

    @Test
    void testFindByCompleted() {
        // Ajouter une tâche complétée
        Task completedTask = Task.builder()
                .label("Completed Task")
                .description("This is completed")
                .completed(true)
                .build();
        repository.save(completedTask);

        Page<Task> completedTasks = repository.findByCompleted(true, PageRequest.of(0, 10));
        assertEquals(1, completedTasks.getTotalElements());
        assertTrue(completedTasks.getContent().get(0).isCompleted());

        Page<Task> incompleteTasks = repository.findByCompleted(false, PageRequest.of(0, 10));
        assertEquals(2, incompleteTasks.getTotalElements());
        assertFalse(incompleteTasks.getContent().get(0).isCompleted());
    }

    @Test
    void testFindById() {
        Task task = repository.save(Task.builder()
                .label("Test Task")
                .description("Test Description")
                .completed(false)
                .build());

        Optional<Task> found = repository.findById(task.getId());
        assertTrue(found.isPresent());
        assertEquals(task.getLabel(), found.get().getLabel());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Task> notFound = repository.findById(999L);
        assertTrue(notFound.isEmpty());
    }

    @Test
    void testSave() {
        Task task = Task.builder()
                .label("New Task")
                .description("New Description")
                .completed(false)
                .build();

        Task saved = repository.save(task);
        assertNotNull(saved.getId());
        assertEquals(task.getLabel(), saved.getLabel());

        // Test de mise à jour
        saved.setLabel("Updated Task");
        Task updated = repository.save(saved);
        assertEquals("Updated Task", updated.getLabel());
        assertEquals(saved.getId(), updated.getId());
    }

    @Test
    void testUpdateStatus() {
        Task task = repository.save(Task.builder()
                .label("Test Task")
                .description("Test Description")
                .completed(false)
                .build());

        Optional<Task> updated = repository.updateStatus(task.getId(), true);
        assertTrue(updated.isPresent());
        assertTrue(updated.get().isCompleted());
    }

    @Test
    void testUpdateStatusNotFound() {
        Optional<Task> notFound = repository.updateStatus(999L, true);
        assertTrue(notFound.isEmpty());
    }

    @Test
    void testPagination() {
        // Ajouter plus de tâches pour tester la pagination
        for (int i = 0; i < 5; i++) {
            repository.save(Task.builder()
                    .label("Task " + i)
                    .description("Description " + i)
                    .completed(false)
                    .build());
        }

        PageRequest firstPage = PageRequest.of(0, 3);
        Page<Task> firstPageResult = repository.findAll(firstPage);
        assertEquals(3, firstPageResult.getContent().size());
        assertTrue(firstPageResult.hasNext());
        assertFalse(firstPageResult.hasPrevious());

        PageRequest secondPage = PageRequest.of(1, 3);
        Page<Task> secondPageResult = repository.findAll(secondPage);
        assertEquals(3, secondPageResult.getContent().size());
        assertTrue(secondPageResult.hasNext());
        assertTrue(secondPageResult.hasPrevious());
    }
}