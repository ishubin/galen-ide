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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileContent extends FileItem {
    private final String content;

    public FileContent(String name, String path, String content) {
        this.setName(name);
        this.setDirectory(false);
        this.setPath(path);
        this.content = content;
        this.setExecutable(FileItem.isExecutable(name));
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


    public String getContent() {
        return content;
    }
}
