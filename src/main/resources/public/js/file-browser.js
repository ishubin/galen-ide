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


function FileBrowser(app) {
    FileBrowser._super(this, "#file-browser", "#tpl-file-browser");
    this.currentFolder = "";
    this.app = app;
    this.fileEditor = new FileEditor(app);
}
extend(FileBrowser, UIComponent);

FileBrowser.prototype.$behavior = {
    click: {
        "a.file-item": function (element) {
            var filePath = element.attr("data-file-path");
            this.loadFileInEditor(filePath);
        },
        "a.directory-item": function (element) {
            var filePath = element.attr("data-file-path");
            this.changeFolder(filePath);
        },
        ".action-launch-spec": function (element) {
            var specPath = element.attr("data-file-path");
            this.app.runTest(specPath);
        }
    }
};
FileBrowser.prototype.changeFolder = function (filePath) {
    this.currentFolder = filePath;
    this.update();
};
FileBrowser.prototype.update = function () {
    var that = this;
    API.files.get(this.currentFolder, function (items) {
        that.showItems(items);
    });
};
FileBrowser.prototype.showItems = function (items) {
    this.render({items: items});
};
FileBrowser.prototype.loadFileInEditor = function (filePath) {
    var that = this;
    API.files.getFile(filePath, function (fileItem) {
        that.showFileEditor(fileItem);
    });
};
FileBrowser.prototype.showFileEditor = function (fileItem) {
    this.fileEditor.show(fileItem);
};



function FileEditor(app) {
    FileEditor._super(this, "#file-editor", "#tpl-file-editor");
    this.fileItem = null;
}
extend(FileEditor, UIModal);

FileEditor.prototype.show = function (fileItem) {
    this.fileItem = fileItem;
    var content = fileItem.content;
    if (fileItem.executable) {
        content = GalenHighlightV2.specs(fileItem.content);
    }
    this.render({
        fileItem: {
            name: fileItem.name,
            executable: fileItem.executable,
            content: content
        }
    });
    this.showModal();
};
FileEditor.prototype.$behavior = {
    click: {
        ".action-file-editor-run-test": function () {
            this.app.runTest(this.fileItem.path);
            this.hideModal();
        }
    }
};

