package fr.natixis.core.ports;

import fr.natixis.core.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Optional;

public interface TaskRepository {
    Page<Task> findAll(PageRequest pageRequest);
    Page<Task> findByCompleted(boolean completed, PageRequest pageRequest);
    Optional<Task> findById(Long id);
    Task save(Task task);
    Optional<Task> updateStatus(Long id, boolean completed);
}