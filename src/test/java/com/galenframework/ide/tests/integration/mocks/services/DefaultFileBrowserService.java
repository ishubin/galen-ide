package com.galenframework.ide.tests.integration.mocks.services;

import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.filebrowser.FileContent;
import com.galenframework.ide.services.filebrowser.FileItem;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class DefaultFileBrowserService implements FileBrowserService {
    @Override
    public List<FileItem> getFilesInPath(RequestData requestData, String path) {
        return Collections.emptyList();
    }

    @Override
    public FileContent showFileContent(RequestData requestData, String path) {
        return new FileContent("somefile.txt", "/somefile.txt", "Some content");
    }

    @Override
    public void saveFile(RequestData requestData, String path, FileContent fileContent) throws IOException {
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return null;
    }
}
