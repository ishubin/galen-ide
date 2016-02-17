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
import org.openqa.selenium.JavascriptExecutor;

public class DeviceInjectSourceCommand extends DeviceCommand {
    private final String originSource;

    public DeviceInjectSourceCommand(String originSource) {
        this.originSource = originSource;
    }

    @Override
    public void execute(Device device, DeviceThread deviceThread) {
        ((JavascriptExecutor)device.getDriver()).executeScript("var source = arguments[0]; " +
                "function injectBody() {" +
                "document.write(source);" +
                "setTimeout('window.stop();', 1000);" +
                "}; setTimeout(injectBody, 10);", originSource);
        try {
            //TODO Make a better way to wait for injected source
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
