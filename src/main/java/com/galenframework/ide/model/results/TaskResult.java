package com.galenframework.ide.model.results;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TaskResult {
    private String name;
    private String taskId;
    private ExecutionStatus status = ExecutionStatus.planned;
    private List<CommandResult> commands = new LinkedList<>();
    private Date startedDate;
    private Date finishedDate;

    public TaskResult(String taskId, String name, List<CommandResult> commandResults) {
        this.taskId = taskId;
        this.name = name;
        this.commands = commandResults;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<CommandResult> getCommands() {
        return commands;
    }

    public void setCommands(List<CommandResult> commands) {
        this.commands = commands;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public Date getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }
}
