package com.galenframework.ide.services.results;

import com.galenframework.ide.TestResultContainer;
import com.galenframework.ide.TestResultsOverview;
import com.galenframework.ide.devices.TestResultsListener;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.Service;
import org.openqa.selenium.Dimension;

import java.util.List;

public interface TestResultService extends TestResultsListener, Service {
    List<TestResultContainer> getTestResults(RequestData requestData);

    TestResultsOverview getTestResultsOverview(RequestData requestData);

    void clearAllTestResults(RequestData requestData);

    String registerNewTestResultContainer(RequestData requestData, String deviceName,
                                          List<String> tags, Dimension size);
}
