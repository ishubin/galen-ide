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
package com.galenframework.ide.model.docs;


import java.util.List;

public class ApiDocRequest {
    private final String method;
    private final String path;
    private final String description;
    private final List<Object> requestExamples;
    private final List<Object> responseExamples;
    private final String title;

    public ApiDocRequest(String method, String path, String title, String description, List<Object> requestExamples, List<Object> responseExamples) {
        this.method = method;
        this.path = path;
        this.title = title;
        this.description = description;
        this.requestExamples = requestExamples;
        this.responseExamples = responseExamples;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

    public List<Object> getRequestExamples() {
        return requestExamples;
    }

    public List<Object> getResponseExamples() {
        return responseExamples;
    }

    public String getTitle() {
        return title;
    }
}
