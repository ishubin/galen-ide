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

import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceStatus;
import com.galenframework.ide.devices.DeviceThread;
import com.galenframework.ide.devices.SizeProvider;
import com.galenframework.ide.devices.commands.DeviceCommand;
import com.galenframework.ide.model.settings.IdeArguments;
import com.galenframework.ide.model.settings.Settings;
import com.galenframework.ide.model.devices.DeviceRequest;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.results.TestResultService;
import com.galenframework.ide.util.SynchronizedStorage;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DeviceServiceImpl implements DeviceService {
    private static final String MASTER_DEVICE_NAME = "master";
    private final ServiceProvider serviceProvider;
    private final IdeArguments ideArguments;

    private SynchronizedStorage<DeviceThread> devices = new SynchronizedStorage<>();

    public DeviceServiceImpl(IdeArguments ideArguments, ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
        this.ideArguments = ideArguments;
    }

    @Override
    public List<Device> getAllDevices() {
        return devices.stream().map(DeviceThread::getDevice).collect(Collectors.toList());
    }

    @Override
    public Device getDevice(String deviceId) {
        Optional<DeviceThread> dt = devices.stream().filter(byDeviceIdOrName(deviceId)).findFirst();
        if (dt.isPresent()) {
            return dt.get().getDevice();
        } else {
            return null;
        }
    }

    public List<DeviceThread> getDeviceThreads() {
        return devices.get();
    }

    private List<DeviceThread> getActiveDeviceThreads() {
        return getDeviceThreads().stream().filter(d -> d.getDevice().getStatus() != DeviceStatus.SHUTDOWN).collect(Collectors.toList());
    }

    @Override
    public void syncAllBrowsersWithMaster() {
        Optional<DeviceThread> masterDevice = findMasterDevice();

        if (masterDevice.isPresent()) {
            String originSource = masterDevice.get().getPageSource();
            String url = masterDevice.get().getCurrentUrl();

            String domSyncMethod = serviceProvider.settingsService().getSettings().getDomSyncMethod();

            if ("inject".equals(domSyncMethod)) {
                syncAllBrowsersUsingInjection(originSource, url);
            } else {
                syncAllBrowsersUsingProxy(originSource, url);
            }
        } else {
            throw new RuntimeException("Master device was not configured");
        }
    }

    private Optional<DeviceThread> findMasterDevice() {
        return getDeviceThreads().stream().filter(d -> d.getDevice().isMaster()).findAny();
    }

    @Override
    public void createDevice(DeviceRequest createDeviceRequest) {
        verifyNameIsDefined(createDeviceRequest);

        DeviceThread deviceThread = registerDeviceThread(createDeviceRequest);

        Class<? extends WebDriver> webDriverClass = pickWebDriverClass(createDeviceRequest.getBrowserType());
        deviceThread.initDriverFromClass(webDriverClass);
    }

    private DeviceThread registerDeviceThread(DeviceRequest createDeviceRequest) {
        DeviceThread deviceThread;
        synchronized (this) {
            verifyNameIsUnique(createDeviceRequest.getName());

            deviceThread = createDeviceThreadFromRequest(createDeviceRequest);
            if (createDeviceRequest.isMaster()) {
                verifyMasterIsAbsent();
                deviceThread.getDevice().setMaster(true);
            }
            addDeviceThread(deviceThread);
        }
        return deviceThread;
    }

    private void verifyMasterIsAbsent() {
        if (getDeviceThreads().stream().filter(d -> d.getDevice().isMaster()).findAny().isPresent()) {
            throw new RuntimeException("Master device already exists");
        }
    }

    private void verifyNameIsUnique(String name) {
        if (getDeviceThreads().stream().filter(d -> d.getDevice().getName().equals(name)).findAny().isPresent()) {
            throw new RuntimeException("Device with name \"" + name + "\" already exists");
        }
    }

    private void verifyNameIsDefined(DeviceRequest createDeviceRequest) {
        String name = createDeviceRequest.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing device name");
        }
    }

    private DeviceThread createDeviceThreadFromRequest(DeviceRequest createDeviceRequest) {
        String uniqueId = UUID.randomUUID().toString();

        Device device = new Device(uniqueId, createDeviceRequest.getName(),
                createDeviceRequest.getBrowserType(),
                createDeviceRequest.getTags(),
                SizeProvider.readFrom(createDeviceRequest)
        );
        return new DeviceThread(device);
    }


    @Override
    public void testAllNodeDevices(String spec, String reportStoragePath) {
        TestResultService testResultService = serviceProvider.testResultService();
        testResultService.clearAllTestResults();
        Settings settings = serviceProvider.settingsService().getSettings();

        getDeviceThreads().stream().filter(byNotMaster()).forEach(dt ->
            dt.getDevice().getSizeProvider().forEachIteration(dt, size -> {
                String reportId = testResultService.registerNewTestResultContainer(dt.getDevice().getName(), dt.getTags());
                dt.checkLayout(settings, reportId, spec, dt.getTags(), testResultService, reportStoragePath);
            })
        );
    }

    public void addDeviceThread(DeviceThread deviceThread) {
        devices.add(deviceThread);
        deviceThread.start();
    }

    private Predicate<DeviceThread> byDeviceIdOrName(String requestedDeviceId) {
        return (dt) -> dt.getDevice().getDeviceId().equals(requestedDeviceId) || dt.getDevice().getName().equals(requestedDeviceId);
    }

    @Override
    public void shutdownDevice(String deviceId) {
        Optional<DeviceThread> deviceOption = devices.stream().filter(byDeviceIdOrName(deviceId)).findFirst();
        if (deviceOption.isPresent()) {
            DeviceThread deviceThread = deviceOption.get();
            deviceThread.shutdownDevice();
            devices.remove(deviceThread);
        } else {
            throw new RuntimeException("Unknown device: " + deviceId);
        }
    }

    @Override
    public void changeDevice(String deviceId, DeviceRequest deviceRequest) {
        Optional<DeviceThread> optionalDevice = getDeviceThreads().stream().filter(byDeviceIdOrName(deviceId)).findFirst();
        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get().getDevice();
            device.setName(deviceRequest.getName());
            device.setTags(deviceRequest.getTags());
            device.setSizeProvider(SizeProvider.readFrom(deviceRequest));
        }
    }

    @Override
    public void shutdownAllDevices() {
        devices.stream().forEach(DeviceThread::shutdownDevice);
    }

    @Override
    public void openUrl(String deviceId, String url) {
        withMandatoryDevice(deviceId, deviceThread -> {
            deviceThread.openUrl(url);
            return null;
        });
    }


    @Override
    public void resize(String deviceId, Dimension size) {
        withMandatoryDevice(deviceId, (dt) -> {
            dt.resize(size);
            return null;
        });
    }

    @Override
    public List<DeviceCommand> getCurrentCommands(String deviceId) {
        return withMandatoryDevice(deviceId, DeviceThread::getCurrentCommands);
    }

    @Override
    public void injectScript(String deviceId, String script) {
        withMandatoryDevice(deviceId, dt -> {
            dt.injectScript(script);
            return null;
        });
    }

    @Override
    public void runJavaScript(String deviceId, String path) {
        withMandatoryDevice(deviceId, dt -> {
            dt.runJavaScript(path);
            return null;
        });
    }

    @Override
    public void restartDevice(String deviceId) {
        withMandatoryDevice(deviceId, deviceThread -> {
            deviceThread.restartDevice();
            return null;
        });
    }

    @Override
    public void clearCookies(String deviceId) {
        withMandatoryDevice(deviceId, dt -> {
            dt.clearCookies();
            return null;
        });
    }

    @Override
    public String checkLayout(String deviceId, String specPath, List<String> tags, String reportStoragePath) {
        return withMandatoryDevice(deviceId, (dt) -> {
            TestResultService testResultService = serviceProvider.testResultService();
            Settings settings = serviceProvider.settingsService().getSettings();

            String reportId = testResultService.registerNewTestResultContainer(dt.getDevice().getName(), tags);
            dt.checkLayout(settings, reportId, specPath, tags, testResultService, reportStoragePath);

            return reportId;
        });
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    private <T> T withMandatoryDevice(String deviceId, Function<DeviceThread, T> action) {
        Optional<DeviceThread> deviceOption = devices.stream().filter(byDeviceIdOrName(deviceId)).findFirst();
        if (deviceOption.isPresent()) {
            return action.apply(deviceOption.get());
        } else {
            throw new RuntimeException("Device is not registered: " + deviceId);
        }
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

    private void syncAllBrowsersUsingProxy(String originSource, String url) {
        String uniqueDomId = serviceProvider.domSnapshotService().createSnapshot(originSource, url);
        getActiveDeviceThreads().stream().filter(byNotMaster()).forEach((device) -> device.openUrl("http://localhost:" + ideArguments.getPort() + "/api/dom-snapshots/" + uniqueDomId + "/snapshot.html"));
    }

    private void syncAllBrowsersUsingInjection(String originSource, String url) {
        getActiveDeviceThreads().stream().filter(byNotMaster()).forEach((device) -> device.injectSource(url, originSource));
    }

    private Predicate<DeviceThread> byNotMaster() {
        return d -> !d.getDevice().isMaster();
    }
}
