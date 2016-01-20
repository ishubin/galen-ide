package com.galenframework.quicktester.devices;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.galenframework.quicktester.Settings;
import com.galenframework.quicktester.devices.commands.*;
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
                            device.setStatus(DeviceStatus.CRASHED);
                            device.setIsActive(false);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
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

    private void sendCommand(DeviceCommand command) {
        try {
            this.commands.put(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Dimension> getSizes() {
        return device.getSizes();
    }

    public List<String> getTags() {
        return device.getTags();
    }

    public Device getDevice() {
        return device;
    }
}
