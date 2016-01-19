package com.galenframework.quicktester;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class SpecsBrowserService {
    public List<FileItem> getFiles() {
        File folder = new File("specs");
        File[] filesInFolder = folder.listFiles();

        List<FileItem> fileItems = new LinkedList<>();
        if (filesInFolder != null) {
            for (File file : filesInFolder) {
                fileItems.add(FileItem.createFrom(file));
            }
        }
        return fileItems;
    }
}
