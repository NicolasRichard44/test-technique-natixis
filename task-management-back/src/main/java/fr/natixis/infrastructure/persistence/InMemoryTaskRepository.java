package fr.natixis.infrastructure.persistence;

import fr.natixis.core.domain.Task;
import fr.natixis.core.ports.TaskRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTaskRepository implements TaskRepository {
    private final Map<Long, Task> tasks = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();

    @PostConstruct
    public void init() {
        save(Task.builder().label("Learn Spring").description("Do the Spring Boot tutorial").completed(false).build());
        save(Task.builder().label("Build API").description("Build the todo list API").completed(false).build());
    }

    @Override
    public Page<Task> findAll(PageRequest pageRequest) {
        var taskList = new ArrayList<>(tasks.values());
        return getPage(taskList, pageRequest);
    }

    @Override
    public Page<Task> findByCompleted(boolean completed, PageRequest pageRequest) {
        var filteredTasks = tasks.values().stream()
                .filter(task -> task.isCompleted() == completed)
                .toList();
        return getPage(filteredTasks, pageRequest);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(counter.incrementAndGet());
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> updateStatus(Long id, boolean completed) {
        return findById(id).map(task -> {
            task.setCompleted(completed);
            return save(task);
        });
    }

    private Page<Task> getPage(java.util.List<Task> taskList, PageRequest pageRequest) {
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), taskList.size());
        var pageContent = taskList.subList(start, end);
        return new PageImpl<>(pageContent, pageRequest, taskList.size());
    }
}