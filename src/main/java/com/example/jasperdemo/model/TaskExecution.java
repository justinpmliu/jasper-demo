package com.example.jasperdemo.model;

import lombok.Data;

import java.util.Date;

@Data
public class TaskExecution {
    private Long taskExecutionId;
    private String taskName;
    private Date startTime;
    private Date endTime;
    private Integer exitCode;
}
