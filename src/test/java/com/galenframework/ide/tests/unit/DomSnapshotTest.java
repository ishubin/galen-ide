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

import com.galenframework.ide.model.DomSnapshot;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class DomSnapshotTest {


    @Test
    public void shouldReplace_allLinks_andRemoveScripts() throws IOException {
        String originSource = IOUtils.toString(getClass().getResourceAsStream("/some-source-origin.html"));
        String expectedResult = IOUtils.toString(getClass().getResourceAsStream("/sanitized-source.html"));

        DomSnapshot domSnapshot = DomSnapshot.createSnapshotAndReplaceUrls(originSource, "http://another.example.com");

        assertEquals(trimEveryLine(domSnapshot.getOriginSource()), expectedResult);
    }

    private String trimEveryLine(String text) {
        StringBuilder builder = new StringBuilder();
        String lines[] = text.split("\\r?\\n");

        for (String line : lines) {
            builder.append(line.trim());
            builder.append('\n');
        }
        return builder.toString();
    }
}
