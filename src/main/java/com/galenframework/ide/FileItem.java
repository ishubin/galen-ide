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

    public static FileItem directory(String name, String path) {
        FileItem fileItem = new FileItem();
        fileItem.setName(name);
        fileItem.setPath(path);
        fileItem.setDirectory(true);
        return fileItem;
    }
}
