package com.galenframework.ide;

import com.galenframework.ide.devices.TestResult;
import org.openqa.selenium.Dimension;

import java.util.List;
import java.util.UUID;

public class TestResultContainer {

    private final String uniqueId;
    private TestResult testResult;
    private String status = "running";
    private String name = "Unnamed";
    private String size;
    private List<String> tags;

    public TestResultContainer(String name, List<String> tags, Dimension size) {
        this.uniqueId = UUID.randomUUID().toString();
        this.tags = tags;
        this.name = name;
        this.size = String.format("%dx%d", size.getWidth(), size.getHeight());
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
        if (testResult != null) {
            this.status = "finished";
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUniqueId() {
        return uniqueId;
    }
}
