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
package com.galenframework.ide.tests.integration.mocks.stubs;

import com.galenframework.ide.model.results.*;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.results.TaskResultService;

import java.util.List;

import static java.util.Collections.emptyList;

public class DefaultTestResultServiceStub implements TaskResultService {

    @Override
    public TaskResultsOverview getTestResultsOverview() {
        return new TaskResultsOverview(emptyList());
    }

    @Override
    public void clearAllTestResults() {
    }

    @Override
    public TaskResult getTaskResult(String taskId) {
        return new TaskResult(taskId, "some task", emptyList());
    }

    @Override
    public TaskResult registerTaskResult(String taskId, String name, List<CommandResult> commandBasicResults) {
        return new TaskResult(taskId, name, commandBasicResults);
    }

    @Override
    public void notifyCommandCompleted(String taskId, String commandId, CommandExecutionResult commandExecutionResult) {
    }

    @Override
    public void notifyTaskCompleted(String taskId, ExecutionStatus taskStatus) {
    }

    @Override
    public void notifyTaskStarted(String taskId) {
    }

    @Override
    public void notifyCommandStarted(String taskId, String commandId) {
    }


    @Override
    public ServiceProvider getServiceProvider() {
        return null;
    }

}
