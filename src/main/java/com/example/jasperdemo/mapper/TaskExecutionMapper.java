package com.example.jasperdemo.mapper;

import com.example.jasperdemo.model.TaskExecution;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskExecutionMapper {
    List<TaskExecution> findAll();
}
