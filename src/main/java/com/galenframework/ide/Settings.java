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

import java.io.File;

public class Settings {
    private boolean makeScreenshots = true;
    private String homeDirectory = System.getProperty("user.home") + File.separator + ".galen";
    private String domSyncMethod;
    private String chromeDriverBinPath;
    private String safariDriverBinPath;
    private String phantomjsDriverBinPath;
    private String edgeDriverBinPath;
    private String ieDriverBinPath;


    public boolean isMakeScreenshots() {
        return makeScreenshots;
    }

    public void setMakeScreenshots(boolean makeScreenshots) {
        this.makeScreenshots = makeScreenshots;
    }

    public String getChromeDriverBinPath() {
        return chromeDriverBinPath;
    }

    public void setChromeDriverBinPath(String chromeDriverBinPath) {
        this.chromeDriverBinPath = chromeDriverBinPath;
    }

    public String getSafariDriverBinPath() {
        return safariDriverBinPath;
    }

    public void setSafariDriverBinPath(String safariDriverBinPath) {
        this.safariDriverBinPath = safariDriverBinPath;
    }

    public String getEdgeDriverBinPath() {
        return edgeDriverBinPath;
    }

    public void setEdgeDriverBinPath(String edgeDriverBinPath) {
        this.edgeDriverBinPath = edgeDriverBinPath;
    }

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public String getPhantomjsDriverBinPath() {
        return phantomjsDriverBinPath;
    }

    public void setPhantomjsDriverBinPath(String phantomjsDriverBinPath) {
        this.phantomjsDriverBinPath = phantomjsDriverBinPath;
    }

    public String getIeDriverBinPath() {
        return ieDriverBinPath;
    }

    public void setIeDriverBinPath(String ieDriverBinPath) {
        this.ieDriverBinPath = ieDriverBinPath;
    }

    public String getDomSyncMethod() {
        return domSyncMethod;
    }

    public void setDomSyncMethod(String domSyncMethod) {
        this.domSyncMethod = domSyncMethod;
    }
}
