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
import com.galenframework.ide.devices.DeviceThread;
import com.galenframework.ide.devices.TestResult;
import com.galenframework.ide.devices.TestResultsListener;
import com.galenframework.javascript.GalenJsExecutor;
import com.galenframework.utils.GalenUtils;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Date;

public class DeviceRunJavaScriptCommand extends DeviceCommand {
    private final String path;
    private final String reportId;
    private final TestResultsListener testResultsListener;

    public DeviceRunJavaScriptCommand(String path, String reportId, TestResultsListener testResultsListener) {
        this.path = path;
        this.reportId = reportId;
        this.testResultsListener = testResultsListener;
    }

    public String getPath() {
        return path;
    }

    @Override
    public void execute(Device device, DeviceThread deviceThread) throws Exception {
        TestResult testResult;
        Date startDate = new Date();
        try {
            File file = GalenUtils.findFile(path);
            Reader scriptFileReader = new FileReader(file);

            GalenJsExecutor js = new GalenJsExecutor();
            js.eval(GalenJsExecutor.loadJsFromLibrary("GalenPages.js"));
            js.putObject("driver", device.getDriver());
            js.eval(scriptFileReader, path);

            testResult = new TestResult("JS executed: " + path);
        } catch (Exception ex) {
            ex.printStackTrace();

            testResult = new TestResult(ex);
        }
        testResult.setStartedAt(startDate);
        testResult.setEndedAt(new Date());
        testResultsListener.onTestResult(reportId, testResult);
    }

    @Override
    public String getName() {
        return DeviceCommand.RUN_JS;
    }
}
