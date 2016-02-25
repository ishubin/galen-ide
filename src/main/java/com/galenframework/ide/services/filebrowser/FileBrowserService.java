package com.galenframework.ide.services.filebrowser;

import com.galenframework.ide.services.Service;

import java.io.IOException;
import java.util.List;

public interface FileBrowserService extends Service {
    List<FileItem> getFilesInPath(String path);

    FileContent showFileContent(String path);

    void saveFile(String path, FileContent fileContent) throws IOException;
}
