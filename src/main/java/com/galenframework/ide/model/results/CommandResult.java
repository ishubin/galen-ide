/*******************************************************************************
* Copyright 2016 Ivan Shubin http://galenframework.com
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*   http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
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
