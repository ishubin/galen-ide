package com.galenframework.ide;

import com.galenframework.ide.devices.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.*;

public class TesterService {
    private final DeviceContainer deviceContainer;

    public TesterService(DeviceContainer deviceContainer) {
        this.deviceContainer = deviceContainer;
    }

    public void syncAllBrowsers() {
        String originSource = deviceContainer.getMasterDriver().getPageSource();
        String url = deviceContainer.getMasterDriver().getCurrentUrl();

        deviceContainer.getDeviceThreads().forEach((device) -> device.injectSource(url, originSource));
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

    public List<TestResultContainer> getTestResults() {
        return deviceContainer.getTestResults();
    }

    public List<Device> getAllDevices() {
        return deviceContainer.getAllDevices();
    }
}
