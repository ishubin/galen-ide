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
package com.galenframework.ide.services.tester;

import com.galenframework.ide.model.TestCommand;
import com.galenframework.ide.services.ServiceProvider;

public class TesterServiceImpl implements TesterService {
    private final String reportStoragePath;
    private final ServiceProvider serviceProvider;
    private TestCommand lastTestCommand;


    public TesterServiceImpl(ServiceProvider serviceProvider, String reportStoragePath) {
        this.serviceProvider = serviceProvider;
        this.reportStoragePath = reportStoragePath;
    }

    @Override
    public void runtTest(TestCommand testCommand) {
        this.lastTestCommand = testCommand;
        serviceProvider.deviceService().updateAllPages(testCommand.getPageUrl(), testCommand.getDomSyncMethod());
        serviceProvider.deviceService().testAllNodeDevices(testCommand.getPath(), reportStoragePath);
    }

    @Override
    public TestCommand getLastTestCommand() {
        return lastTestCommand;
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }
}
