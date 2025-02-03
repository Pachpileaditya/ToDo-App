package com.todoapp.MyApp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todoapp.MyApp.entity.Task;

@Repository
public interface TaskRepo extends JpaRepository<Task, Integer>
{
    List<Task> findByCompleted(boolean completed);
}
