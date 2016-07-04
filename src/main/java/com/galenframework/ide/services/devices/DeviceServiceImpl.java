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
import com.galenframework.ide.devices.DeviceExecutor;
import com.galenframework.ide.devices.SizeProvider;
import com.galenframework.ide.devices.commands.*;
import com.galenframework.ide.devices.tasks.DeviceTask;
import com.galenframework.ide.model.results.CommandResult;
import com.galenframework.ide.model.results.ExecutionStatus;
import com.galenframework.ide.model.results.TaskResult;
import com.galenframework.ide.model.settings.IdeArguments;
import com.galenframework.ide.model.devices.DeviceRequest;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.results.TaskResultService;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toCollection;

public class DeviceServiceImpl implements DeviceService {
    private final ServiceProvider serviceProvider;
    private final IdeArguments ideArguments;
    private final String reportStoragePath;

    private SynchronizedStorage<DeviceExecutor> devices = new SynchronizedStorage<>();

    public DeviceServiceImpl(IdeArguments ideArguments, ServiceProvider serviceProvider, String reportStoragePath) {
        this.serviceProvider = serviceProvider;
        this.ideArguments = ideArguments;
        this.reportStoragePath = reportStoragePath;
    }

    @Override
    public List<Device> getAllDevices() {
        return devices.stream().map(DeviceExecutor::getDevice).collect(Collectors.toList());
    }

    @Override
    public Device getDevice(String deviceId) {
        Optional<DeviceExecutor> dt = devices.stream().filter(byDeviceIdOrName(deviceId)).findFirst();
        if (dt.isPresent()) {
            return dt.get().getDevice();
        } else {
            return null;
        }
    }

    @Override
    public TaskResult executeTask(String deviceId, DeviceTask task) {
        return withMandatoryDevice(deviceId, deviceExecutor -> executeTask(deviceExecutor, task));
    }

    private final static AtomicLong _uniqueId = new AtomicLong(new Date().getTime());

    private String generateUniqueId() {
        return Long.toString(_uniqueId.incrementAndGet(), 36);
    }

    private void provideUniqueIdsToTaskAndCommands(DeviceTask task) {
        task.setTaskId(generateUniqueId());
        if (task.getCommands() != null) {
            for (DeviceCommand command : task.getCommands()) {
                command.setCommandId(generateUniqueId());
            }
        }
    }

    private TaskResult executeTask(DeviceExecutor deviceExecutor, DeviceTask task) {
        provideUniqueIdsToTaskAndCommands(task);

        List<CommandResult> commandBasicResults = task.getCommands().stream().map(command ->
                new CommandResult(command.getCommandId(), command.getName(), ExecutionStatus.planned)
        ).collect(toCollection(LinkedList::new));

        TaskResult taskResult = serviceProvider.taskResultService().registerTaskResult(
            task.getTaskId(),
            task.getName(),
            commandBasicResults
        );

        deviceExecutor.sendTask(task);
        return taskResult;
    }

    public List<DeviceExecutor> getDeviceExecutors() {
        return devices.get();
    }

    private List<DeviceExecutor> getActiveDeviceThreads() {
        return getDeviceExecutors().stream().filter(d -> d.getDevice().getStatus() != DeviceStatus.SHUTDOWN).collect(Collectors.toList());
    }

    @Override
    public void updateAllPages(String pageUrl, String domSyncMethod) {
        if ("url".equals(domSyncMethod)) {
            openUrlOnAllBrowsers(pageUrl);
        } else if ("inject".equals(domSyncMethod)) {
            syncAllBrowsersUsingInjection();
        } else {
            syncAllBrowsersUsingProxy();
        }
    }

    private void openUrlOnAllBrowsers(String pageUrl) {
        getDeviceExecutors().stream().forEach(deviceExecutor -> executeTask(deviceExecutor, singleCommandTask(new DeviceOpenUrlCommand(pageUrl))));
    }

    private DeviceTask singleCommandTask(DeviceCommand deviceCommand) {
        DeviceTask deviceTask = new DeviceTask();
        deviceTask.setName(deviceCommand.getName());
        deviceTask.setCommands(singletonList(deviceCommand));
        return deviceTask;
    }


    private Optional<DeviceExecutor> findMasterDevice() {
        return getDeviceExecutors().stream().filter(d -> d.getDevice().isMaster()).findAny();
    }

    @Override
    public void createDevice(DeviceRequest createDeviceRequest) {
        verifyNameIsDefined(createDeviceRequest);

        DeviceExecutor deviceExecutor = registerDeviceExecutor(createDeviceRequest);

        Class<? extends WebDriver> webDriverClass = pickWebDriverClass(createDeviceRequest.getBrowserType());
        executeTask(deviceExecutor, singleCommandTask(new DeviceCreateDriverFromClassCommand(webDriverClass)));
    }

    private DeviceExecutor registerDeviceExecutor(DeviceRequest createDeviceRequest) {
        DeviceExecutor deviceExecutor;
        synchronized (this) {
            verifyNameIsUnique(createDeviceRequest.getName());

            deviceExecutor = createDeviceExecutorFromRequest(createDeviceRequest);
            if (createDeviceRequest.isMaster()) {
                verifyMasterIsAbsent();
                deviceExecutor.getDevice().setMaster(true);
            }
            addDeviceThread(deviceExecutor);
        }
        return deviceExecutor;
    }

    private void verifyMasterIsAbsent() {
        if (getDeviceExecutors().stream().filter(d -> d.getDevice().isMaster()).findAny().isPresent()) {
            throw new RuntimeException("Master device already exists");
        }
    }

    private void verifyNameIsUnique(String name) {
        if (getDeviceExecutors().stream().filter(d -> d.getDevice().getName().equals(name)).findAny().isPresent()) {
            throw new RuntimeException("Device with name \"" + name + "\" already exists");
        }
    }

    private void verifyNameIsDefined(DeviceRequest createDeviceRequest) {
        String name = createDeviceRequest.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing device name");
        }
    }

    private DeviceExecutor createDeviceExecutorFromRequest(DeviceRequest createDeviceRequest) {
        String uniqueId = UUID.randomUUID().toString();

        Device device = new Device(uniqueId, createDeviceRequest.getName(),
                createDeviceRequest.getBrowserType(),
                createDeviceRequest.getTags(),
                SizeProvider.readFrom(createDeviceRequest)
        );
        return new DeviceExecutor(device, serviceProvider.taskResultService(), serviceProvider.settingsService(), reportStoragePath);
    }


    @Override
    public void testAllNodeDevices(String spec, String reportStoragePath) {
        TaskResultService testResultService = serviceProvider.taskResultService();
        testResultService.clearAllTestResults();

        getDeviceExecutors().stream().filter(byNotMaster()).forEach(deviceExecutor ->
            deviceExecutor.getDevice().getSizeProvider().forEachIteration(size -> {
                if (size != null) {
                    executeTask(deviceExecutor, new DeviceTask("Test on size " + convertSizeToString(size), asList(
                        new DeviceResizeCommand(size.getWidth(), size.getHeight()),
                        new DeviceCheckLayoutCommand(spec, deviceExecutor.getTags())
                    )));
                } else {
                    executeTask(deviceExecutor, new DeviceTask("Test on size " + currentSizeOf(deviceExecutor), singletonList(
                        new DeviceCheckLayoutCommand(spec, deviceExecutor.getTags())
                    )));
                }
            })
        );
    }

    private String currentSizeOf(DeviceExecutor deviceExecutor) {
        try {
            return convertSizeToString(deviceExecutor.getCurrentSize());
        } catch (Exception ex) {
            return "unknown";
        }
    }

    private String convertSizeToString(Dimension size) {
        return size.getWidth() + "x" + size.getHeight();
    }

    public void addDeviceThread(DeviceExecutor deviceThread) {
        devices.add(deviceThread);
        deviceThread.start();
    }

    private Predicate<DeviceExecutor> byDeviceIdOrName(String requestedDeviceId) {
        return (dt) -> dt.getDevice().getDeviceId().equals(requestedDeviceId) || dt.getDevice().getName().equals(requestedDeviceId);
    }

    @Override
    public void shutdownDevice(String deviceId) {
        Optional<DeviceExecutor> deviceOption = devices.stream().filter(byDeviceIdOrName(deviceId)).findFirst();
        if (deviceOption.isPresent()) {
            DeviceExecutor deviceThread = deviceOption.get();
            deviceThread.shutdownDevice();
            devices.remove(deviceThread);
        } else {
            throw new RuntimeException("Unknown device: " + deviceId);
        }
    }

    @Override
    public void changeDevice(String deviceId, DeviceRequest deviceRequest) {
        Optional<DeviceExecutor> optionalDevice = getDeviceExecutors().stream().filter(byDeviceIdOrName(deviceId)).findFirst();
        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get().getDevice();
            device.setName(deviceRequest.getName());
            device.setTags(deviceRequest.getTags());
            device.setSizeProvider(SizeProvider.readFrom(deviceRequest));
        }
    }

    @Override
    public void shutdownAllDevices() {
        devices.stream().forEach(DeviceExecutor::shutdownDevice);
    }

    @Override
    public List<DeviceTask> getCurrentTasks(String deviceId) {
        return withMandatoryDevice(deviceId, DeviceExecutor::getCurrentTasks);
    }

    @Override
    public void restartDevice(String deviceId) {
        withMandatoryDevice(deviceId, deviceExecutor -> {
            if (deviceExecutor.getDeviceInitializationCommand() != null) {
                executeTask(deviceExecutor, singleCommandTask(new DeviceRestartCommand()));
            }
            return null;
        });
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    private <T> T withMandatoryDevice(String deviceId, Function<DeviceExecutor, T> action) {
        Optional<DeviceExecutor> deviceOption = devices.stream().filter(byDeviceIdOrName(deviceId)).findFirst();
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

    private void syncAllBrowsersUsingProxy() {
        withMasterUrlAndSource((url, originSource) -> {
            String uniqueDomId = serviceProvider.domSnapshotService().createSnapshot(originSource, url);
            getActiveDeviceThreads().stream().filter(byNotMaster()).forEach((deviceExecutor) ->
                executeTask(deviceExecutor, singleCommandTask(new DeviceOpenUrlCommand("http://localhost:" + ideArguments.getPort() + "/api/dom-snapshots/" + uniqueDomId + "/snapshot.html")))
            );
        });
    }

    private void syncAllBrowsersUsingInjection() {
        withMasterUrlAndSource((url, originSource) ->
            getActiveDeviceThreads().stream().filter(byNotMaster()).forEach((deviceExecutor) ->
                executeTask(deviceExecutor, new DeviceTask("Injecting source", asList(
                    new DeviceOpenUrlCommand(url),
                    new DeviceInjectSourceCommand(originSource)
                )))
            )
        );
    }

    private Predicate<DeviceExecutor> byNotMaster() {
        return d -> !d.getDevice().isMaster();
    }

    private void withMasterUrlAndSource(UrlAndSource urlAndSourceProvider) {
        Optional<DeviceExecutor> masterDevice = findMasterDevice();
        if (masterDevice.isPresent()) {
            String originSource = masterDevice.get().getPageSource();
            String url = masterDevice.get().getCurrentUrl();
            urlAndSourceProvider.provideUrlAndSource(url, originSource);
        } else {
            throw new RuntimeException("Master device was not configured");
        }
    }

    private interface UrlAndSource {
        void provideUrlAndSource(String url, String source);
    }
}
