package com.galenframework.ide.model.results;

import java.util.Date;

public class CommandResult extends CommandExecutionResult {
    private String commandId;
    private String name;
    private Date startedDate;
    private Date finishedDate;

    public CommandResult(String commandId, String name, ExecutionStatus status) {
        this.commandId = commandId;
        this.name = name;
        this.setStatus(status);
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    public Date getStartedDate() {
        return startedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void copyData(CommandExecutionResult commandExecutionResult) {
        setData(commandExecutionResult.getData());
        setStatus(commandExecutionResult.getStatus());
        setErrorMessage(commandExecutionResult.getErrorMessage());
        setExternalReport(commandExecutionResult.getExternalReport());
    }
}
