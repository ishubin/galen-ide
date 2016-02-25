package com.galenframework.ide.services.filebrowser;

import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.Service;

import java.io.IOException;
import java.util.List;

public interface FileBrowserService extends Service {
    List<FileItem> getFilesInPath(RequestData requestData, String path);

    FileContent showFileContent(RequestData requestData, String path);

    void saveFile(RequestData requestData, String path, FileContent fileContent) throws IOException;
}
