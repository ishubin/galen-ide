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
package com.galenframework.ide.tests.unit;

import com.galenframework.ide.model.settings.IdeArguments;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class IdeArgumentsTest {

    @Test
    public void should_parse_command_line_arguments_correctly() throws Exception {
        IdeArguments ideArguments = IdeArguments.parse(new String[]{"-P", "profile.json", "-p", "1234", "-s", "some/static/storage"});
        assertEquals(ideArguments.getProfile(), "profile.json");
        assertEquals(ideArguments.getPort(), 1234);
        assertEquals(ideArguments.getFileStorage(), "some/static/storage");
    }

    @Test
    public void should_parse_command_line_arguments_with_full_names_correctly() throws Exception {
        IdeArguments ideArguments = IdeArguments.parse(new String[]{"--profile", "profile.json", "--port", "1234", "--storage", "some/static/storage"});
        assertEquals(ideArguments.getProfile(), "profile.json");
        assertEquals(ideArguments.getPort(), 1234);
        assertEquals(ideArguments.getFileStorage(), "some/static/storage");
    }
}
