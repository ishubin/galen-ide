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
package com.galenframework.ide.util;


public class ScriptTagsRemover {

    private final int position;
    private final String html;

    public ScriptTagsRemover(String html) {
        this.position = 0;
        this.html = html;
    }


    public String removeScriptTags() {
        String [] parts = html.split("(<script>|<script\\s)");

        StringBuilder builder = new StringBuilder();
        builder.append(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            builder.append(removeUntilEnding(parts[i]));
        }
        return builder.toString();
    }

    private String removeUntilEnding(String part) {
        int totalLength = part.length();
        int selectedSkipIndex;

        int closingSlashIndex = indexOfOrValue(part, "/>", totalLength  + 1);
        int closingScriptIndex = indexOfOrValue(part, "</script>", totalLength + 1);

        if (closingSlashIndex < closingScriptIndex) {
            selectedSkipIndex = closingSlashIndex + 2;
        } else {
            selectedSkipIndex = closingScriptIndex + 9;
        }

        if (selectedSkipIndex <= totalLength) {
            return part.substring(selectedSkipIndex);
        } else {
            return part;
        }
    }

    private int indexOfOrValue(String part, String sub, int totalLength) {
        int index = part.indexOf(sub);
        if (index >= 0) {
            return index;
        } else {
            return totalLength;
        }
    }
}
