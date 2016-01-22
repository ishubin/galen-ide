package com.galenframework.ide;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileContent {
    private final String name;
    private final String path;
    private final String content;

    public FileContent(String name, String path, String content) {
        this.name = name;
        this.path = path;
        this.content = content;
    }

    public static FileContent fromFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (!file.isDirectory()) {
                String content;
                try {
                    content = FileUtils.readFileToString(file);
                } catch (IOException e) {
                    throw new RuntimeException("Couldn't read file contents: " + path, e);
                }
                return new FileContent(file.getName(), file.getPath(), content);
            } else throw  new RuntimeException("Path is a directory: " + path);
        } else throw new RuntimeException("File is not found: " + path);
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }
}
