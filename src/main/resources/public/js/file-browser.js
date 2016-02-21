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

FileBrowser.prototype.$behavior = function () {
    return {
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
    FileEditor._super(this, "#global-modal", "#tpl-file-editor");
    this.app = app;
    this.fileItem = null;
}
extend(FileEditor, UIModal);

FileEditor.prototype.show = function (fileItem) {
    this.fileItem = fileItem;
    var content = fileItem.content;
    this.render({
        fileItem: {
            name: fileItem.name,
            executable: fileItem.executable,
            content: content
        }
    });
    this.showModal();
    var thatFileEditor = this;

    CodeMirror.commands.save = function () {
        thatFileEditor.saveFile();
    };
    this.codeMirror = CodeMirror.fromTextArea(document.getElementById("file-editor-textarea"), {
        lineNumbers: true,
        mode: "galenspecs",
        smartIndent: true,
        theme: "ambiance",
        indentUnit: 4
    });
    this.codeMirror.setOption("extraKeys", {
        Tab: function(cm) {
            var spaces = Array(cm.getOption("indentUnit") + 1).join(" ");
            cm.replaceSelection(spaces);
        }
    });
    this.codeMirror.on("change", function (codeMirror, changeObj) {
        thatFileEditor.onCodeChanged();
    });

    var windowHeight = $(window).height();
    this.codeMirror.setSize(null, windowHeight - 300);
};
FileEditor.prototype.onCodeChanged = function () {
    this.$find(".action-file-editor-save-changes").prop("disabled", false);
};
FileEditor.prototype.$behavior = function () {
    return {
        click: {
            ".action-file-editor-run-test": function () {
                this.app.runTest(this.fileItem.path);
                this.hideModal();
            },
            ".action-file-editor-save-changes": function () {
                this.saveFile();
            }
        }
    };
};
FileEditor.prototype.saveFile = function () {
    this.fileItem.content = this.codeMirror.getValue();
    API.files.saveFile(this.fileItem.path, this.fileItem, function () {});
};

