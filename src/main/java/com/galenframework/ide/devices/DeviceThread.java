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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.galenframework.ide.model.settings.Settings;
import com.galenframework.ide.devices.commands.*;
import com.galenframework.ide.util.Callback;
import org.eclipse.jetty.util.ArrayQueue;
import org.openqa.selenium.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class DeviceThread extends Thread {
    protected final Device device;

    @JsonIgnore
    private final Queue<DeviceCommand> commands = new ArrayQueue<>(500);

    @JsonIgnore
    private final ReentrantLock commandsLock = new ReentrantLock();


    // Used for restarting device
    private DeviceCommand deviceInitializationCommand = null;

    public DeviceThread(Device device) {
        this.device = device;
    }

    @Override
    public void run() {
        while(device.isActive()) {
            if (!commands.isEmpty()) {
                try {
                    DeviceCommand command = withCommandsLock(commands::poll);

                    if (command != null) {

                        if (device.getStatus() == DeviceStatus.READY) {
                            device.setStatus(DeviceStatus.BUSY);
                        }

                        System.out.println("Executing command " + command.toString());
                        try {
                            command.execute(device, this);
                            if (commands.isEmpty()) {
                                device.setStatus(DeviceStatus.READY);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            device.setStatus(DeviceStatus.CRASHED);
                            device.setIsActive(false);
                            device.setLastErrorMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        device.setStatus(DeviceStatus.SHUTDOWN);
    }

    public void openUrl(String url) {
        sendCommands(new DeviceOpenUrlCommand(url));
    }
    public void injectSource(String url, String originSource) {
        sendCommands(
            new DeviceOpenUrlCommand(url),
            new DeviceInjectSourceCommand(originSource)
        );
    }

    public void checkLayout(Settings settings, String uniqueId, String spec, List<String> tags, TestResultsListener testResultsListener, String reportStoragePath) {
        sendCommands(new DeviceCheckLayoutCommand(settings, uniqueId, spec, tags, testResultsListener, reportStoragePath));
    }

    public void runJavaScript(String path, String reportId, TestResultsListener testResultsListener) {
        sendCommands(new DeviceRunJavaScriptCommand(path, reportId, testResultsListener));
    }

    public void resize(Dimension size) {
        sendCommands(new DeviceResizeCommand(size));
    }

    public void initDriverFromClass(Class<? extends WebDriver> driverClass) {
        DeviceCommand deviceCommand = new DeviceCreateDriverFromClassCommand(driverClass);
        sendCommands(deviceCommand);
        deviceInitializationCommand = deviceCommand;
    }

    public void restartDevice() {
        if (deviceInitializationCommand != null) {
            sendCommands(new DeviceRestartCommand(deviceInitializationCommand));
        } else {
            throw new RuntimeException("Device wasn't initialized");
        }
    }

    public void shutdownDevice() {
        device.shutdown();
        deleteAllCommands();
    }

    private void deleteAllCommands() {
        withCommandsLock(() -> {
            commands.clear();
            return null;
        });
    }

    public void injectScript(String script) {
        sendCommands(new DeviceInjectCommand(script));
    }

    public void clearCookies() {
        sendCommands(new DeviceClearCookiesCommand());
    }

    public synchronized void sendCommands(DeviceCommand... commands) {
        if (device.isActive()) {
            withCommandsLock(() -> {
                for (DeviceCommand command : commands) {
                    this.commands.add(command);
                }
                return null;
            });
        }
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

    public List<DeviceCommand> getCurrentCommands() {
        return withCommandsLock(() ->new LinkedList<>(commands));
    }


    private <A> A withCommandsLock(Callback<A> callback) {
        commandsLock.lock();
        A result;
        try {
            result = callback.apply();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            commandsLock.unlock();
        }
        return result;
    }
}
