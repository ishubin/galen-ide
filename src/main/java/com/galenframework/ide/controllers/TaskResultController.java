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
package com.galenframework.ide.controllers;

import com.galenframework.ide.model.results.CommandResult;
import com.galenframework.ide.model.results.TaskResult;
import com.galenframework.ide.services.results.TaskResultService;
import static com.galenframework.ide.util.JsonTransformer.toJson;
import static spark.Spark.*;

public class TaskResultController {
    private static final String APPLICATION_JSON = "application/json";
    private final TaskResultService taskResultService;

    public TaskResultController(TaskResultService taskResultService) {
        this.taskResultService = taskResultService;
        initRoutes();
    }

    private void initRoutes() {
        get("/api/results", (request, response) -> {
            response.header("Content-Type", APPLICATION_JSON);
            return taskResultService.getTestResultsOverview();
        }, toJson());

        get("/api/results/:taskId", (req, res) -> {
            String taskId = req.params("taskId");
            res.header("Content-Type", APPLICATION_JSON);

            TaskResult taskResult = taskResultService.getTaskResult(taskId);
            if (taskResult != null) {
                return taskResult;
            } else {
                throw new RuntimeException("Task doesn't exist: " + taskId);
            }
        }, toJson());

        get("/api/results/:taskId/commands/:commandId", (req, res) -> {
            String taskId = req.params("taskId");
            String commandId = req.params("commandId");
            res.header("Content-Type", APPLICATION_JSON);

            CommandResult commandResult = findCommand(taskId, commandId);
            if (commandResult != null) {
                return commandResult;
            } else {
                throw new RuntimeException("Could find command: " + commandId);
            }
        }, toJson());

        get("/api/results/:taskId/commands/:commandId/data", (req, res) -> {
            String taskId = req.params("taskId");
            String commandId = req.params("commandId");
            res.header("Content-Type", APPLICATION_JSON);

            CommandResult commandResult = findCommand(taskId, commandId);
            if (commandResult != null) {
                return commandResult.getData();
            } else {
                throw new RuntimeException("Could find command: " + commandId);
            }
        }, toJson());

        delete("/api/results", (req, res) -> {
            taskResultService.clearAllTestResults();
            return "cleared";
        });
    }

    private CommandResult findCommand(String taskId, String commandId) {
        TaskResult taskResult = taskResultService.getTaskResult(taskId);
        if (taskResult != null && taskResult.getCommands() != null) {
            for (CommandResult commandResult : taskResult.getCommands()) {
                if (commandResult.getCommandId().equals(commandId)) {
                    return commandResult;
                }
            }
        }
        return null;
    }
}
