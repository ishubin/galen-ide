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
package com.galenframework.ide.devices;

import com.galenframework.ide.devices.tasks.DeviceTask;
import com.galenframework.ide.model.results.CommandExecutionResult;
import com.galenframework.ide.model.results.ExecutionStatus;
import com.galenframework.ide.devices.commands.*;
import com.galenframework.ide.model.settings.Settings;
import com.galenframework.ide.services.results.TaskResultService;
import com.galenframework.ide.services.settings.SettingsService;
import com.galenframework.ide.util.Callback;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.String.format;

public class DeviceExecutor extends Thread {
    protected final Device device;
    private final Logger logger = LoggerFactory.getLogger(DeviceExecutor.class);

    private final Queue<DeviceTask> tasks = new LinkedBlockingQueue<>();
    private final ReentrantLock tasksLock = new ReentrantLock();
    private final SettingsService settingsService;
    private final String reportStoragePath;
    private final TaskResultService taskResultService;

    // Used for restarting device
    private DeviceCommand deviceInitializationCommand = null;

    public DeviceExecutor(Device device, TaskResultService taskResultService, SettingsService settingsService, String reportStoragePath) {
        this.device = device;
        this.taskResultService = taskResultService;
        this.settingsService = settingsService;
        this.reportStoragePath = reportStoragePath;
    }

    @Override
    public void run() {
        while(device.isActive()) {
            if (!tasks.isEmpty()) {
                try {
                    DeviceTask task = withTaskLock(tasks::poll);
                    if (task != null) {
                        device.setStatus(DeviceStatus.BUSY);
                        executeTask(task);
                        device.setStatus(DeviceStatus.READY);
                    }
                } catch (Exception e) {
                    logger.error("Error executing task", e);
                }
            }
        }
        device.setStatus(DeviceStatus.SHUTDOWN);
    }

    private void executeTask(DeviceTask task) {
        logger.info("Executing task: " + task.getName());

        taskResultService.notifyTaskStarted(task.getTaskId());
        boolean hasTaskCrashed = false;
        Set<ExecutionStatus> allCommandStatuses = new HashSet<>();

        if (task.getCommands() != null) {
            Iterator<DeviceCommand> it = task.getCommands().iterator();
            while(it.hasNext() && !hasTaskCrashed) {
                DeviceCommand command = it.next();
                try {
                    allCommandStatuses.add(executeCommand(command, device, this, task.getTaskId(), settingsService.getSettings(), reportStoragePath));
                } catch (Exception e) {
                    logger.error(format("Task \"%s\" has crashed at command \"%s\"", task.getName(), command.getName()), e);
                    hasTaskCrashed = true;
                    taskResultService.notifyCommandCompleted(task.getTaskId(), command.getCommandId(), CommandExecutionResult.error(e));
                }
            }

        }
        ExecutionStatus taskStatus = identifyTaskStatus(hasTaskCrashed, allCommandStatuses);
        taskResultService.notifyTaskCompleted(task.getTaskId(), taskStatus);
    }

    private ExecutionStatus identifyTaskStatus(boolean hasTaskCrashed, Set<ExecutionStatus> allCommandStatuses) {
        ExecutionStatus taskStatus = ExecutionStatus.passed;
        if (hasTaskCrashed) {
            taskStatus = ExecutionStatus.failed;
        } else if (allCommandStatuses.contains(ExecutionStatus.failed)) {
            taskStatus = ExecutionStatus.failed;
        } else if (allCommandStatuses.contains(ExecutionStatus.warning)) {
            taskStatus = ExecutionStatus.warning;
        }
        return taskStatus;
    }

    private ExecutionStatus executeCommand(DeviceCommand command, Device device, DeviceExecutor deviceExecutor, String taskId, Settings settings, String reportStoragePath) throws Exception {
        logger.info("Executing command: " + command.getName());
        taskResultService.notifyCommandStarted(taskId, command.getCommandId());
        CommandExecutionResult result = command.execute(device, deviceExecutor, taskId, settings, reportStoragePath);
        taskResultService.notifyCommandCompleted(taskId, command.getCommandId(), result);
        return result.getStatus();
    }

    public void shutdownDevice() {
        device.shutdown();
        deleteAllTasks();
    }

    private void deleteAllTasks() {
        withTaskLock(() -> {
            tasks.clear();
            return null;
        });
    }

    public List<String> getTags() {
        return device.getTags();
    }

    public Device getDevice() {
        return device;
    }

    public Dimension getCurrentSize() {
        return device.retrieveCurrentSize();
    }

    public String getCurrentUrl() {
        return device.getDriver().getCurrentUrl();
    }

    public String getPageSource() {
        return device.getDriver().getPageSource();
    }

    public List<DeviceTask> getCurrentTasks() {
        return withTaskLock(() -> new LinkedList<>(tasks));
    }


    private <A> A withTaskLock(Callback<A> callback) {
        tasksLock.lock();
        A result;
        try {
            result = callback.apply();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            tasksLock.unlock();
        }
        return result;
    }

    public DeviceCommand getDeviceInitializationCommand() {
        return deviceInitializationCommand;
    }

    public void setDeviceInitializationCommand(DeviceCommand deviceInitializationCommand) {
        this.deviceInitializationCommand = deviceInitializationCommand;
    }

    public void sendTask(DeviceTask task) {
        if (device.isActive()) {
            withTaskLock(() -> {
                tasks.add(task);
                return null;
            });
        }
    }
}
