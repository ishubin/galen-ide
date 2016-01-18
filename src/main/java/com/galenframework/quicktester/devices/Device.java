package com.galenframework.quicktester.devices;

import com.galenframework.quicktester.devices.commands.DeviceCheckLayoutCommand;
import com.galenframework.quicktester.devices.commands.DeviceInjectSourceCommand;
import org.openqa.selenium.*;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Device extends Thread {
    private final List<String> tags;
    private WebDriver driver;
    private List<Dimension> sizes;
    private boolean isActive = true;

    private final BlockingQueue<DeviceCommand> commands = new ArrayBlockingQueue<>(100);


    public Device(WebDriver driver, List<String> tags, List<Dimension> sizes) {
        this.driver = driver;
        this.sizes = sizes;
        this.tags = tags;
        if (sizes.size() > 0) {
            driver.manage().window().setSize(sizes.get(0));
        } else {
            throw new RuntimeException("Should have at least one size");
        }
    }

    @Override
    public void run() {
        while(isActive) {
            if (!commands.isEmpty()) {
                try {
                    DeviceCommand command = commands.take();
                    if (command != null) {
                        System.out.println("Executing command " + command.toString());
                        command.execute(this, driver);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void injectSource(String url, String originSource) {
        sendCommand(new DeviceOpenUrlCommand(url));
        sendCommand(new DeviceInjectSourceCommand(originSource));
    }

    public void checkLayout(String uniqueId, String spec, TestResultsListener testResultsListener, String reportStoragePath) {
        sendCommand(new DeviceCheckLayoutCommand(uniqueId, spec, testResultsListener, reportStoragePath));
    }

    public void resize(Dimension size) {
        sendCommand(new DeviceResizeCommand(size));
    }

    public List<String> getTags() {
        return tags;
    }

    private void sendCommand(DeviceCommand command) {
        try {
            this.commands.put(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public List<Dimension> getSizes() {
        return sizes;
    }

    public void setSizes(List<Dimension> sizes) {
        this.sizes = sizes;
    }

}
