package fr.natixis.core.application;

import fr.natixis.core.application.api.TaskService;
import fr.natixis.core.domain.Task;
import fr.natixis.core.ports.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public Page<Task> getAllTasks(PageRequest pageRequest) {
        return taskRepository.findAll(pageRequest);
    }

    @Override
    public Page<Task> getTasksByCompletion(boolean completed, PageRequest pageRequest) {
        return taskRepository.findByCompleted(completed, pageRequest);
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task addTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> updateTaskStatus(Long id, boolean completed) {
        return taskRepository.updateStatus(id, completed);
    }
}