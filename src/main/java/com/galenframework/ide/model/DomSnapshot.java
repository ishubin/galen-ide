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
package com.galenframework.ide.model;

import com.galenframework.ide.util.ScriptTagsRemover;

import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DomSnapshot {
    private String originSource;

    public DomSnapshot() {
    }

    public DomSnapshot(String originSource) {
        this.originSource = originSource;
    }

    public String getOriginSource() {
        return originSource;
    }

    public void setOriginSource(String originSource) {
        this.originSource = originSource;
    }

    public static DomSnapshot createSnapshotAndReplaceUrls(String source, String originUrl) throws MalformedURLException {
        String host = fetchHost(originUrl);

        Pattern pattern = Pattern.compile("\\s(src|href)(\\s*?)=(\\s)*?(\"|')(.*?)(\"|')");
        Matcher matcher = pattern.matcher(source);

        StringBuffer buff = new StringBuffer();
        while (matcher.find()) {
            String fullGroup = matcher.group();
            String href = matcher.group(5);
            String properHref;

            if (!href.startsWith("http:") && !href.startsWith("https:")) {
                properHref = "http://" + host;
                if (!href.startsWith("/")) {
                    properHref += "/";
                }
                properHref += href;
            } else {
                properHref = href;
            }

            matcher.appendReplacement(buff, fullGroup.replace(href, properHref));
        }
        matcher.appendTail(buff);

        return new DomSnapshot(new ScriptTagsRemover(buff.toString()).removeScriptTags());
    }

    private static String fetchHost(String originUrl) {
        int id1 = originUrl.indexOf("//");
        int id2 = originUrl.indexOf("/", id1 + 2);
        if (id2 < 0) {
            id2 = originUrl.length();
        }

        return originUrl.substring(id1 + 2, id2);
    }
}
