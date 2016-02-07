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

import com.galenframework.specs.Spec;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SpecsBrowserService {
    private File workingDirectory = new File(".");

    public List<FileItem> getFilesInPath(String path) {
        if (path.isEmpty()) {
            path = ".";
        }
        List<FileItem> fileItems = new LinkedList<>();

        File folder = new File(path);
        upFrom(folder).ifPresent(fileItems::add);

        File[] filesInFolder = folder.listFiles();
        if (filesInFolder != null) {
            for (File file : filesInFolder) {
                fileItems.add(FileItem.createFrom(file));
            }
        }
        Collections.sort(fileItems, SpecsBrowserService::sortOrderForFileItems);
        return fileItems;
    }

    private static int sortOrderForFileItems(FileItem left, FileItem right) {
        int dirDiff = toInt(right.isDirectory()) - toInt(left.isDirectory());
        if (dirDiff != 0) {
            return dirDiff;
        }
        return left.getName().compareTo(right.getName());
    }

    private static int toInt(boolean booleanValue) {
        return booleanValue ? 1: 0;
    }

    private Optional<FileItem> upFrom(File folder) {
        File parentFile = folder.getParentFile();
        if (parentFile != null) {
            return Optional.of(FileItem.directory("..", parentFile.getPath()));
        } else if (!folder.getName().equals(".")) {
            return Optional.of(FileItem.directory("..", "."));
        } else {
            return Optional.empty();
        }

    }

}
