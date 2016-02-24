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
package com.galenframework.ide;

import com.galenframework.ide.devices.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.*;

public class TesterService {
    private final DeviceContainer deviceContainer;
    private final String reportStoragePath;
    private TestCommand lastTestCommand;

    private Map<String, DomSnapshot> domSnapshots = new HashMap<>();

    public TesterService(DeviceContainer deviceContainer, String reportStoragePath) {
        this.deviceContainer = deviceContainer;
        this.reportStoragePath = reportStoragePath;
    }

    public void syncAllBrowsers() {
        String originSource = deviceContainer.getMasterDriver().getPageSource();
        String url = deviceContainer.getMasterDriver().getCurrentUrl();

        String domSyncMethod = getSettings().getDomSyncMethod();

        if ("inject".equals(domSyncMethod)) {
            syncAllBrowsersUsingInjection(originSource, url);
        } else {
            syncAllBrowsersUsingProxy(originSource, url);
        }
    }

    private void syncAllBrowsersUsingProxy(String originSource, String url) {
        String uniqueDomId = UUID.randomUUID().toString();
        try {
            domSnapshots.put(uniqueDomId, DomSnapshot.createSnapshotAndReplaceUrls(originSource, url));
        } catch (Exception ex) {
            throw new RuntimeException("Couldn't parse url: " + url, ex);
        }
        deviceContainer.getDeviceThreads().forEach((device) -> device.openUrl("http://localhost:4567/api/dom/" + uniqueDomId));
    }

    private void syncAllBrowsersUsingInjection(String originSource, String url) {
        deviceContainer.getDeviceThreads().forEach((device) -> device.injectSource(url, originSource));
    }

    private Settings getSettings() {
        return deviceContainer.getSettings();
    }

    public void testAllBrowsers(String spec, String reportStoragePath) {
        deviceContainer.clearAllTestResults();
        deviceContainer.getDeviceThreads().forEach(dt ->
                        dt.getDevice().getSizeProvider().forEachIteration(dt, size -> {
                            TestResultContainer testResultContainer = deviceContainer.registerNewTestResultContainer(dt, size);
                            dt.checkLayout(deviceContainer.getSettings(), testResultContainer.getUniqueId(), size, spec, deviceContainer, reportStoragePath);
                        })
        );
    }

    public void createDevice(DeviceRequest createDeviceRequest) {
        Class<? extends WebDriver> webDriverClass = pickWebDriverClass(createDeviceRequest.getBrowserType());

        Device device = new Device(createDeviceRequest.getName(),
                createDeviceRequest.getBrowserType(),
                createDeviceRequest.getTags(),
                SizeProvider.readFrom(createDeviceRequest)
        );
        DeviceThread  deviceThread = new DeviceThread(device);
        deviceContainer.addDeviceThread(deviceThread);

        deviceThread.createDriverFromClass(webDriverClass);
    }


    private static final Map<String, Class<? extends WebDriver>> webDriverMappings = new HashMap<String, Class<? extends WebDriver>>() {{
        put("firefox", FirefoxDriver.class);
        put("chrome", ChromeDriver.class);
        put("safari", SafariDriver.class);
        put("edge", EdgeDriver.class);
        put("ie", InternetExplorerDriver.class);
        put("phantomjs", PhantomJSDriver.class);
    }};
    private Class<? extends WebDriver> pickWebDriverClass(String browserType) {
        if (webDriverMappings.containsKey(browserType)) {
            return webDriverMappings.get(browserType);
        } else {
            throw new RuntimeException("Unsupported browser type: " + browserType);
        }
    }

    public void applySettings(Settings settings) {
        deviceContainer.setSettings(settings);

        applySystemPropertyIfDefined("webdriver.chrome.driver", settings.getChromeDriverBinPath());
        applySystemPropertyIfDefined("phantomjs.binary.path", settings.getPhantomjsDriverBinPath());
        applySystemPropertyIfDefined("webdriver.edge.driver", settings.getEdgeDriverBinPath());
        applySystemPropertyIfDefined("webdriver.ie.driver", settings.getIeDriverBinPath());
    }

    private void applySystemPropertyIfDefined(String systemPropertyName, String value) {
        if (value != null) {
            System.setProperty(systemPropertyName, value);
        }
    }

    public void shutdownDevice(String deviceId) {
        deviceContainer.shutdownDevice(deviceId);
    }

    public void changeDevice(String deviceId, DeviceRequest deviceRequest) {
        Optional<DeviceThread> optionalDevice = deviceContainer.getDeviceThreads().stream().filter(d -> d.getDevice().getDeviceId().equals(deviceId)).findFirst();
        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get().getDevice();
            device.setName(deviceRequest.getName());
            device.setTags(deviceRequest.getTags());
            device.setSizeProvider(SizeProvider.readFrom(deviceRequest));
        }
    }

    public TestResultsOverview getTestResultsOverview() {
        return new TestResultsOverview(deviceContainer.getTestResults(), getLastTestCommand());
    }

    public List<Device> getAllDevices() {
        return deviceContainer.getAllDevices();
    }

    public void runtTest(TestCommand testCommand) {
        this.lastTestCommand = testCommand;
        syncAllBrowsers();
        testAllBrowsers(testCommand.getSpecPath(), reportStoragePath);
    }

    public TestCommand getLastTestCommand() {
        return lastTestCommand;
    }

    public Map<String, DomSnapshot> getDomSnapshots() {
        return domSnapshots;
    }
}
