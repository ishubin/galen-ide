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
package com.galenframework.ide.services.results;

import com.galenframework.ide.model.results.*;
import com.galenframework.ide.util.SynchronizedStorage;
import com.galenframework.ide.services.ServiceProvider;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class TaskResultServiceImpl implements TaskResultService {

    private final ServiceProvider serviceProvider;
    private final SynchronizedStorage<TaskResult> taskResults;

    public TaskResultServiceImpl(ServiceProvider serviceProvider, SynchronizedStorage<TaskResult> taskResults) {
        this.serviceProvider = serviceProvider;
        this.taskResults = taskResults;
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    @Override
    public TaskResultsOverview getTestResultsOverview() {
        return new TaskResultsOverview(taskResults.get());
    }

    @Override
    public void clearAllTestResults() {
        this.taskResults.clear();
    }

    @Override
    public TaskResult registerTaskResult(String taskId, String name, List<CommandResult> commandResults) {
        TaskResult taskResult = new TaskResult(taskId, name, commandResults);
        taskResults.add(taskResult);
        return taskResult;
    }

    @Override
    public void notifyTaskCompleted(String taskId, ExecutionStatus taskStatus) {
        withTask(taskId, (task) -> {
            task.setFinishedDate(new Date());
            task.setStatus(taskStatus);
        });
    }

    @Override
    public void notifyTaskStarted(String taskId) {
        withTask(taskId, (task) -> {
            task.setStartedDate(new Date());
            task.setStatus(ExecutionStatus.running);
        });
    }

    @Override
    public void notifyCommandStarted(String taskId, String commandId) {
        withCommandInTask(taskId, commandId, (command) -> {
            command.setStartedDate(new Date());
            command.setStatus(ExecutionStatus.running);
        });
    }

    @Override
    public void notifyCommandCompleted(String taskId, String commandId, CommandExecutionResult commandExecutionResult) {
        withCommandInTask(taskId, commandId, (command) -> {
            command.setFinishedDate(new Date());
            command.setStatus(commandExecutionResult.getStatus());
            command.copyData(commandExecutionResult);
        });
    }

    @Override
    public TaskResult getTaskResult(String taskId) {
        Optional<TaskResult> taskResultOption = taskResults.stream().filter(tr -> tr.getTaskId().equals(taskId)).findFirst();

        if (taskResultOption.isPresent()) {
            return taskResultOption.get();
        } else {
            return null;
        }
    }

    private void withTask(String taskId, Consumer<TaskResult> consumer) {
        taskResults.stream().filter((task) -> task.getTaskId().equals(taskId)).findFirst().ifPresent(consumer::accept);
    }

    private void withCommandInTask(String taskId, String commandId, Consumer<CommandResult> consumer) {
        withTask(taskId, task -> {
            if (task.getCommands() != null) {
                task.getCommands().stream().filter(command ->
                    command.getCommandId().equals(commandId)).findFirst().ifPresent(consumer::accept
                );
            }
        });
    }
}
