package fr.natixis.service;

import fr.natixis.core.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {
    private final Map<Long, Task> tasks = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();

    @PostConstruct
    public void init() {
        addTask(new Task(null, "Learn Spring", "Do the Spring Boot tutorial", false));
        addTask(new Task(null, "Build API", "Build the todo list API", false));
    }

    public Page<Task> getAllTasks(PageRequest pageRequest) {
        List<Task> taskList = new ArrayList<>(tasks.values());
        return getPage(taskList, pageRequest);
    }

    public Page<Task> getTasksByCompletion(boolean completed, PageRequest pageRequest) {
        List<Task> filteredTasks = tasks.values().stream()
                .filter(task -> task.isCompleted() == completed)
                .toList();
        return getPage(filteredTasks, pageRequest);
    }

    public Optional<Task> getTaskById(Long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public Task addTask(Task task) {
        long id = counter.incrementAndGet();
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

    public Optional<Task> updateTaskStatus(Long id, boolean completed) {
        Task task = tasks.get(id);
        if (task != null) {
            task.setCompleted(completed);
            return Optional.of(task);
        }
        return Optional.empty();
    }

    private Page<Task> getPage(List<Task> taskList, PageRequest pageRequest) {
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), taskList.size());
        
        List<Task> pageContent = taskList.subList(start, end);
        return new PageImpl<>(pageContent, pageRequest, taskList.size());
    }
}