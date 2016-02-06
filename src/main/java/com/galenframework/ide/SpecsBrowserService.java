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
import java.util.LinkedList;
import java.util.List;

public class SpecsBrowserService {
    public List<FileItem> getFiles() {
        File folder = new File("specs");
        File[] filesInFolder = folder.listFiles();

        List<FileItem> fileItems = new LinkedList<>();
        if (filesInFolder != null) {
            for (File file : filesInFolder) {
                String name = file.getName();
                if (name.endsWith(".spec") || name.endsWith(".gspec")) {
                    fileItems.add(FileItem.createFrom(file));
                }
            }
        }
        return fileItems;
    }
}
