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
package com.galenframework.ide.devices.commands;

import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceExecutor;
import com.galenframework.ide.model.results.CommandExecutionResult;
import com.galenframework.ide.model.settings.Settings;
import com.galenframework.javascript.GalenJsExecutor;
import com.galenframework.utils.GalenUtils;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import static com.galenframework.ide.model.results.CommandExecutionResult.passed;

public class DeviceRunJavaScriptCommand extends DeviceCommand {
    private String path;
    private String content;

    public DeviceRunJavaScriptCommand() {
    }

    public DeviceRunJavaScriptCommand(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public CommandExecutionResult execute(Device device, DeviceExecutor deviceExecutor, String taskId, Settings settings, String reportStoragePath) throws Exception {
        File file = GalenUtils.findFile(path);
        Reader scriptFileReader = new FileReader(file);

        GalenJsExecutor js = new GalenJsExecutor();
        js.eval(GalenJsExecutor.loadJsFromLibrary("GalenPages.js"));
        js.putObject("driver", device.getDriver());
        js.putObject("taskId", taskId);
        js.putObject("commandId", getCommandId());

        if (path != null) {
            js.eval(scriptFileReader, path);
        } else if (content != null) {
            js.eval(content);
        } else {
            throw new IllegalArgumentException("Both path and content are null");
        }

        return passed();
    }

    @Override
    public String getName() {
        return "runJs";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
