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

import com.galenframework.ide.devices.TestResult;
import org.openqa.selenium.Dimension;

import java.util.List;
import java.util.UUID;

public class TestResultContainer {

    private final String uniqueId;
    private TestResult testResult;
    private String status = "running";
    private String name = "Unnamed";
    private List<String> tags;

    public TestResultContainer(String name, List<String> tags) {
        this.uniqueId = UUID.randomUUID().toString();
        this.tags = tags;
        this.name = name;
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
