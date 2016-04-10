package com.galenframework.ide.devices.commands;

import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceThread;
import com.galenframework.javascript.GalenJsExecutor;
import com.galenframework.utils.GalenUtils;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class DeviceRunJavaScriptCommand extends DeviceCommand {
    private final String path;

    public DeviceRunJavaScriptCommand(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public void execute(Device device, DeviceThread deviceThread) throws Exception {
        File file = GalenUtils.findFile(path);
        Reader scriptFileReader = new FileReader(file);

        GalenJsExecutor js = new GalenJsExecutor();
        js.eval(GalenJsExecutor.loadJsFromLibrary("GalenPages.js"));
        js.putObject("driver", device.getDriver());
        js.eval(scriptFileReader, path);
    }

    @Override
    public String getName() {
        return DeviceCommand.RUN_JS;
    }
}
