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
import com.galenframework.ide.DriverProvider;
import com.galenframework.ide.Settings;
import com.galenframework.ide.devices.commands.*;
import org.openqa.selenium.*;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DeviceThread extends Thread {
    protected final Device device;
    @JsonIgnore
    private final BlockingQueue<DeviceCommand> commands = new ArrayBlockingQueue<>(100);


    public DeviceThread(Device device) {
        this.device = device;
    }

    @Override
    public void run() {
        while(device.isActive()) {
            if (!commands.isEmpty()) {
                try {
                    DeviceCommand command = commands.take();
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        device.setStatus(DeviceStatus.SHUTDOWN);
    }

    public void openUrl(String url) {
        sendCommand(new DeviceOpenUrlCommand(url));
    }
    public void injectSource(String url, String originSource) {
        sendCommand(new DeviceOpenUrlCommand(url));
        sendCommand(new DeviceInjectSourceCommand(originSource));
    }

    public void checkLayout(Settings settings, String uniqueId, Dimension size, String spec, TestResultsListener testResultsListener, String reportStoragePath) {
        sendCommand(new DeviceCheckLayoutCommand(settings, uniqueId, size, spec, testResultsListener, reportStoragePath));
    }

    public void resize(Dimension size) {
        sendCommand(new DeviceResizeCommand(size));
    }

    public void createDriverFromClass(Class<? extends WebDriver> driverClass) {
        sendCommand(new DeviceCreateDriverFromClassCommand(driverClass));
    }

    public void shutdownDevice() {
        sendCommand(new DeviceShutdownCommand());
    }

    public void createDriver(DriverProvider driverProvider) {
        sendCommand(new DeviceCreateDriverFromProvider(driverProvider));
    }

    private void sendCommand(DeviceCommand command) {
        try {
            if (device.isActive()) {
                this.commands.put(command);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<String> getTags() {
        return device.getTags();
    }

    public Device getDevice() {
        return device;
    }

}
