package com.todoapp.MyApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todoapp.MyApp.entity.Task;
import com.todoapp.MyApp.service.TaskService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService theTaskService;

    // get all tasks
    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() {

        List<Task> theTasks = theTaskService.getAllTask();

        return ResponseEntity.ok(theTasks);
    }

    // get task by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable int id) {
        try {
            Task theTask = theTaskService.getTaskById(id);
            return ResponseEntity.ok(theTask);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // get all completed task
    @GetMapping("/completed")
    public ResponseEntity<List<Task>> getCompletedTasks() {
        List<Task> completedTasks = theTaskService.getAllCompletedTasks();

        if (completedTasks.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(completedTasks);
    }

    // get all incompleted task
    @GetMapping("/incompleted")
    public ResponseEntity<List<Task>>  getInCompletedTasks() {
        List<Task> incompletedTasks = theTaskService.getAllInCompletedTasks();

        if(incompletedTasks.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(incompletedTasks);
    }
    

    // add task
    @PostMapping("/")
    public ResponseEntity<Task> addTask(@RequestBody Task theTask) {

        Task tempTask = theTaskService.addTask(theTask);

        return ResponseEntity.ok(tempTask);
    }

    // update task
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody Task updatedTask) {
        try {
            if (!theTaskService.taskExists(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Task with ID " + id + " not found.");
            }

            // Fetch existing task
            Task existingTask = theTaskService.getTaskById(id);

            // Update fields
            existingTask.setTask(updatedTask.getTask());
            existingTask.setCompleted(updatedTask.isCompleted());

            // Save updated task
            Task savedTask = theTaskService.addTask(existingTask);

            return ResponseEntity.ok(savedTask);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating task: " + e.getMessage());
        }
    }

    // delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable int id) {
        if (!theTaskService.taskExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with ID " + id + " not found.");
        }

        theTaskService.deleteTask(id);
        return ResponseEntity.ok("Task with ID " + id + " deleted successfully.");
    }

}
