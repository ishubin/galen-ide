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
package com.galenframework.ide.services.devices;

import com.galenframework.ide.*;
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceStatus;
import com.galenframework.ide.devices.DeviceThread;
import com.galenframework.ide.devices.SizeProvider;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.results.TestResultService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.*;
import java.util.stream.Collectors;

public class DeviceServiceImpl implements DeviceService {
    private final ServiceProvider serviceProvider;
    private WebDriver masterDriver;
    private List<DeviceThread> devices = new LinkedList<>();

    public DeviceServiceImpl(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
        masterDriver = new FirefoxDriver();
        masterDriver.get("http://localhost:8080");
        masterDriver.manage().window().maximize();
    }

    @Override
    public List<Device> getAllDevices(RequestData requestData) {
        return devices.stream().map(DeviceThread::getDevice).collect(Collectors.toList());
    }

    public List<DeviceThread> getDeviceThreads() {
        return devices;
    }

    private List<DeviceThread> getActiveDeviceThreads() {
        return getDeviceThreads().stream().filter(d -> d.getDevice().getStatus() != DeviceStatus.SHUTDOWN).collect(Collectors.toList());
    }

    @Override
    public void syncAllBrowsers(RequestData requestData) {
        String originSource = masterDriver.getPageSource();
        String url = masterDriver.getCurrentUrl();

        String domSyncMethod = serviceProvider.settingsService().getSettings(requestData).getDomSyncMethod();

        if ("inject".equals(domSyncMethod)) {
            syncAllBrowsersUsingInjection(originSource, url);
        } else {
            syncAllBrowsersUsingProxy(requestData, originSource, url);
        }
    }

    @Override
    public void createDevice(RequestData requestData, DeviceRequest createDeviceRequest) {
        Class<? extends WebDriver> webDriverClass = pickWebDriverClass(createDeviceRequest.getBrowserType());

        String uniqueId = UUID.randomUUID().toString();

        Device device = new Device(uniqueId, createDeviceRequest.getName(),
                createDeviceRequest.getBrowserType(),
                createDeviceRequest.getTags(),
                SizeProvider.readFrom(createDeviceRequest)
        );
        DeviceThread  deviceThread = new DeviceThread(device);
        addDeviceThread(deviceThread);

        deviceThread.createDriverFromClass(webDriverClass);
    }


    @Override
    public void testAllBrowsers(RequestData requestData, String spec, String reportStoragePath) {
        TestResultService testResultService = serviceProvider.testResultService();
        testResultService.clearAllTestResults(requestData);
        Settings settings = serviceProvider.settingsService().getSettings(requestData);
        getDeviceThreads().forEach(dt ->
            dt.getDevice().getSizeProvider().forEachIteration(dt, size -> {
                String uniqueId = testResultService.registerNewTestResultContainer(requestData, dt.getDevice().getName(), dt.getTags(), size);
                dt.checkLayout(settings, uniqueId, size, spec, testResultService, reportStoragePath);
            })
        );
    }

    public void addDeviceThread(DeviceThread deviceThread) {
        devices.add(deviceThread);
        deviceThread.start();
    }

    @Override
    public void shutdownDevice(RequestData requestData, String deviceId) {
        Optional<DeviceThread> deviceOption = devices.stream().filter(d -> d.getDevice().getDeviceId().equals(deviceId)).findFirst();
        if (deviceOption.isPresent()) {
            deviceOption.get().shutdownDevice();
        } else {
            throw new RuntimeException("Unknown device: " + deviceId);
        }
    }

    @Override
    public void changeDevice(RequestData requestData, String deviceId, DeviceRequest deviceRequest) {
        Optional<DeviceThread> optionalDevice = getDeviceThreads().stream().filter(d -> d.getDevice().getDeviceId().equals(deviceId)).findFirst();
        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get().getDevice();
            device.setName(deviceRequest.getName());
            device.setTags(deviceRequest.getTags());
            device.setSizeProvider(SizeProvider.readFrom(deviceRequest));
        }
    }

    @Override
    public void shutdownAllDevices(RequestData requestData) {
        devices.stream().forEach(DeviceThread::shutdownDevice);
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
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

    private void syncAllBrowsersUsingProxy(RequestData requestData, String originSource, String url) {
        String uniqueDomId = serviceProvider.domSnapshotService().createSnapshot(requestData, originSource, url);
        getActiveDeviceThreads().forEach((device) -> device.openUrl("http://localhost:4567/api/dom-snapshots/" + uniqueDomId + "/snapshot.html"));
    }


    private void syncAllBrowsersUsingInjection(String originSource, String url) {
        getActiveDeviceThreads().forEach((device) -> device.injectSource(url, originSource));
    }
}
