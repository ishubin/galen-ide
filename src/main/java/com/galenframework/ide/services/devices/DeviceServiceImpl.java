package com.galenframework.ide.services.devices;

import com.galenframework.ide.*;
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceThread;
import com.galenframework.ide.devices.SizeProvider;
import com.galenframework.ide.devices.TestResult;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.results.TestResultService;
import com.galenframework.ide.services.settings.SettingsService;
import org.openqa.selenium.Dimension;
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
    private Map<String, DomSnapshot> domSnapshots = new HashMap<>();


    public DeviceServiceImpl(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
        masterDriver = new FirefoxDriver();
        masterDriver.get("http://localhost:8080");
        masterDriver.manage().window().maximize();
    }

    @Override
    public List<Device> getAllDevices() {
        return devices.stream().map(d -> d.getDevice()).collect(Collectors.toList());
    }

    public List<DeviceThread> getDeviceThreads() {
        return devices;
    }

    @Override
    public void syncAllBrowsers() {
        String originSource = masterDriver.getPageSource();
        String url = masterDriver.getCurrentUrl();

        String domSyncMethod = serviceProvider.settingsService().getSettings().getDomSyncMethod();

        if ("inject".equals(domSyncMethod)) {
            syncAllBrowsersUsingInjection(originSource, url);
        } else {
            syncAllBrowsersUsingProxy(originSource, url);
        }
    }

    @Override
    public void createDevice(DeviceRequest createDeviceRequest) {
        Class<? extends WebDriver> webDriverClass = pickWebDriverClass(createDeviceRequest.getBrowserType());

        Device device = new Device(createDeviceRequest.getName(),
                createDeviceRequest.getBrowserType(),
                createDeviceRequest.getTags(),
                SizeProvider.readFrom(createDeviceRequest)
        );
        DeviceThread  deviceThread = new DeviceThread(device);
        addDeviceThread(deviceThread);

        deviceThread.createDriverFromClass(webDriverClass);
    }


    @Override
    public void testAllBrowsers(String spec, String reportStoragePath) {
        TestResultService testResultService = serviceProvider.testResultService();
        testResultService.clearAllTestResults();
        Settings settings = serviceProvider.settingsService().getSettings();
        getDeviceThreads().forEach(dt ->
            dt.getDevice().getSizeProvider().forEachIteration(dt, size -> {
                String uniqueId = testResultService.registerNewTestResultContainer(dt.getDevice().getName(), dt.getTags(), size);
                dt.checkLayout(settings, uniqueId, size, spec, testResultService, reportStoragePath);
            })
        );
    }

    @Override
    public Map<String, DomSnapshot> getDomSnapshots() {
        return domSnapshots;
    }



    public void addDeviceThread(DeviceThread deviceThread) {
        devices.add(deviceThread);
        deviceThread.start();
    }

    @Override
    public void shutdownDevice(String deviceId) {
        Optional<DeviceThread> deviceOption = devices.stream().filter(d -> d.getDevice().getDeviceId().equals(deviceId)).findFirst();
        if (deviceOption.isPresent()) {
            deviceOption.get().shutdownDevice();
        } else {
            throw new RuntimeException("Unknown device: " + deviceId);
        }
    }

    @Override
    public void changeDevice(String deviceId, DeviceRequest deviceRequest) {
        Optional<DeviceThread> optionalDevice = getDeviceThreads().stream().filter(d -> d.getDevice().getDeviceId().equals(deviceId)).findFirst();
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

    private void syncAllBrowsersUsingProxy(String originSource, String url) {
        String uniqueDomId = UUID.randomUUID().toString();
        try {
            domSnapshots.put(uniqueDomId, DomSnapshot.createSnapshotAndReplaceUrls(originSource, url));
        } catch (Exception ex) {
            throw new RuntimeException("Couldn't parse url: " + url, ex);
        }
        getDeviceThreads().forEach((device) -> device.openUrl("http://localhost:4567/api/dom/" + uniqueDomId));
    }

    private void syncAllBrowsersUsingInjection(String originSource, String url) {
        getDeviceThreads().forEach((device) -> device.injectSource(url, originSource));
    }

}