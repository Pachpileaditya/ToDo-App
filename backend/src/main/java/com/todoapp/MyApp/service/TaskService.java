package com.todoapp.MyApp.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.todoapp.MyApp.entity.Task;
import com.todoapp.MyApp.repo.TaskRepo;

import jakarta.transaction.Transactional;

@Service
public class TaskService {

    private TaskRepo theTaskRepo;

    TaskService(TaskRepo tempTaskRepo) {
        this.theTaskRepo = tempTaskRepo;
    }

    public List<Task> getAllTask() {
        return theTaskRepo.findAll();
    }

    public Task getTaskById(int theId) {
        return theTaskRepo.findById(theId)
                .orElseThrow(() -> new RuntimeException("Task with " + theId + " not found"));
    }

    // add and update
    @Transactional
    public Task addTask(Task theTask) {
        Task tempTask = theTaskRepo.save(theTask);
        return tempTask;
    }

    @Transactional
    public void deleteTask(int theId) {
        if (!theTaskRepo.existsById(theId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + theId + " not found.");
        }
        theTaskRepo.deleteById(theId);
    }

    public boolean taskExists(int theId) {
        return theTaskRepo.existsById(theId);
    }

    public List<Task> getAllCompletedTasks() {
        return theTaskRepo.findByCompleted(true);
    }

    public List<Task> getAllInCompletedTasks() {
        return theTaskRepo.findByCompleted(false);
    }

}
