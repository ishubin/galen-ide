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
package com.galenframework.ide.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galenframework.ide.model.TestCommand;
import com.galenframework.ide.services.tester.TesterService;

import static spark.Spark.*;


public class TesterController {
    private final TesterService testerService;

    ObjectMapper mapper = new ObjectMapper();

    public TesterController(TesterService testerService) {
        this.testerService  = testerService;
        initRoutes();
    }

    private void initRoutes() {
        post("/api/tester/test", (request, response) -> {
            TestCommand testCommand = mapper.readValue(request.body(), TestCommand.class);
            testerService.runtTest(testCommand);
            return "Started testing: " + testCommand.getSpecPath();
        });
    }
}
