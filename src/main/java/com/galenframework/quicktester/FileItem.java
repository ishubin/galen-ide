package com.galenframework.quicktester;

import java.io.File;

public class FileItem {
    private boolean directory;
    private String name;
    private String path;
    private boolean executable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public static FileItem createFrom(File file) {
        FileItem fileItem = new FileItem();
        fileItem.directory = file.isDirectory();
        fileItem.name = file.getName();
        fileItem.path = file.getPath();
        fileItem.executable = file.getName().endsWith(".gspec") || file.getName().endsWith(".spec");

        return fileItem;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isExecutable() {
        return executable;
    }

    public void setExecutable(boolean executable) {
        this.executable = executable;
    }
}
