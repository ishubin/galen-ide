package com.galenframework.ide;

import com.galenframework.ide.devices.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.*;
import java.util.stream.Collectors;

public class TesterService implements TestResultsListener {
    private WebDriver masterDriver;
    private List<DeviceThread> devices = new LinkedList<>();
    private List<TestResultContainer> testResults = Collections.synchronizedList(new LinkedList<>());
    private Settings settings = new Settings();

    public TesterService() {
        masterDriver = null; //new FirefoxDriver();
        //masterDriver.get("http://testapp.galenframework.com");
        //masterDriver.manage().window().maximize();

        /*devices.add(new DeviceThread(new Device("Firefox mobile", "firefox", asList("mobile"), new SizeProviderCustom(asList(size(450, 600), size(480, 600), size(500, 600))))));
        devices.add(new DeviceThread(new Device("Firefox tablet", "firefox", asList("tablet"), asList(size(600, 600), size(700, 600), size(800, 600)))));
        devices.add(new DeviceThread(new Device("Firefox desktop", "firefox", asList("desktop"), asList(size(1024, 768), size(1100, 768), size(1200, 768)))));
        devices.forEach((device -> {
            device.start();
            device.createDriverFromClass(FirefoxDriver.class);
            //device.openUrl("http://localhost:8080");
        }));
        */
    }

    public void syncAllBrowsers() {
        String originSource = masterDriver.getPageSource();
        String url = masterDriver.getCurrentUrl();

        devices.forEach((device) -> device.injectSource(url, originSource));
    }

    public void testAllBrowsers(String spec, String reportStoragePath) {
        this.testResults.clear();
        devices.forEach(deviceThread ->
                        deviceThread.getDevice().getSizeProvider().forEachIteration(deviceThread, size -> {
                            TestResultContainer testResultContainer = registerNewTestResultContainer(deviceThread, size);
                            deviceThread.checkLayout(settings, testResultContainer.getUniqueId(), size, spec, this, reportStoragePath);
                        })
        );
    }

    private TestResultContainer registerNewTestResultContainer(DeviceThread deviceThread, Dimension size) {
        TestResultContainer testResultContainer = new TestResultContainer(
                deviceThread.getDevice().getName(),
                deviceThread.getTags(),
                size
        );
        testResults.add(testResultContainer);
        return testResultContainer;
    }

    @Override
    public void onTestResult(String testUniqueId, TestResult testResult) {
        Optional<TestResultContainer> foundTestResult = testResults.stream().filter((r) -> r.getUniqueId().equals(testUniqueId)).findFirst();

        if (foundTestResult.isPresent()) {
            foundTestResult.get().setTestResult(testResult);
        }
    }

    private Dimension size(int w, int h) {
        return new Dimension(w, h);
    }

    public List<TestResultContainer> getTestResults() {
        return testResults;
    }

    public List<Device> getAllDevices() {
        return devices.stream().map(d -> d.getDevice()).collect(Collectors.toList());
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void createDevice(DeviceRequest createDeviceRequest) {
        Class<? extends WebDriver> webDriverClass = pickWebDriverClass(createDeviceRequest.getBrowserType());

        Device device = new Device(createDeviceRequest.getName(),
                createDeviceRequest.getBrowserType(),
                createDeviceRequest.getTags(),
                SizeProvider.readFrom(createDeviceRequest)
        );
        DeviceThread  deviceThread = new DeviceThread(device);
        devices.add(deviceThread);

        deviceThread.start();
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
        setSettings(settings);
        if (settings.getChromeDriverBinPath() != null) {
            System.setProperty("webdriver.chrome.driver", settings.getChromeDriverBinPath());
        }
    }

    public void shutdownDevice(String deviceId) {
        Optional<DeviceThread> deviceOption = devices.stream().filter(d -> d.getDevice().getDeviceId().equals(deviceId)).findFirst();
        if (deviceOption.isPresent()) {
            deviceOption.get().shutdownDevice();
        } else {
            throw new RuntimeException("Unknown device: " + deviceId);
        }
    }

    public void changeDevice(String deviceId, DeviceRequest deviceRequest) {
        Optional<DeviceThread> optionalDevice = devices.stream().filter(d -> d.getDevice().getDeviceId().equals(deviceId)).findFirst();
        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get().getDevice();
            device.setName(deviceRequest.getName());
            device.setTags(deviceRequest.getTags());
            device.setSizeProvider(SizeProvider.readFrom(deviceRequest));
        }
    }
}