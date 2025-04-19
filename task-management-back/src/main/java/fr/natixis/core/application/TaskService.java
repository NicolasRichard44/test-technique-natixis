package fr.natixis.core.application;

import fr.natixis.core.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Optional;

public interface TaskService {
    Page<Task> getAllTasks(PageRequest pageRequest);
    Page<Task> getTasksByCompletion(boolean completed, PageRequest pageRequest);
    Optional<Task> getTaskById(Long id);
    Task addTask(Task task);
    Optional<Task> updateTaskStatus(Long id, boolean completed);
}