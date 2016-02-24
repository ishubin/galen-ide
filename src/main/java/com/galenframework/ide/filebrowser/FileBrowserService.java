package com.galenframework.ide.filebrowser;

import java.io.IOException;
import java.util.List;

public interface FileBrowserService {
    List<FileItem> getFilesInPath(String path);

    FileContent showFileContent(String path);

    void saveFile(String path, FileContent fileContent) throws IOException;
}
