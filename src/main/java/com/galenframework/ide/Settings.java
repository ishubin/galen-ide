package com.galenframework.ide;

import java.io.File;

public class Settings {
    private boolean makeScreenshots = true;
    private String homeDirectory = System.getProperty("user.home") + File.separator + ".galen";
    private String chromeDriverBinPath;
    private String safariDriverBinPath;
    private String phantomjsDriverBinPath;
    private String edgeDriverBinPath;


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
}
