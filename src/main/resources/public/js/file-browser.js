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
var FileBrowser = {
    currentFolder: "",
    changeFolder: function (filePath) {
        this.currentFolder = filePath;
        this.update();
    },
    update: function () {
        var that = this;
        API.files.get(this.currentFolder, function (items) {
            that.showItems(items);
            whenClick("#file-browser .action-launch-spec", function () {
                var specPath = this.attr("data-file-path");
                App.runTest(specPath);
            });
        });
    },
    showItems: function (items) {
        App.templates.fileBrowser.renderTo("#file-browser", {items: items});
        var that = this;
        whenClick("#file-browser a.file-item", function () {
            var filePath = this.attr("data-file-path");
            that.loadFileInEditor(filePath);
        });
        var that = this;
        whenClick("#file-browser a.directory-item", function () {
            var filePath = this.attr("data-file-path");
            that.changeFolder(filePath);
        });
    },
    loadFileInEditor: function (filePath) {
        var that = this;
        API.files.getFile(filePath, function (fileItem) {
            that.showFileEditor(fileItem);
        });
    },
    showFileEditor: function (fileItem) {
        var $modal = $("#file-editor-modal");
        var buttonRunTest = $modal.find(".action-file-editor-run-test");
        if (fileItem.executable) {
            buttonRunTest.show();
        } else {
            buttonRunTest.hide();
        }
        $modal.find(".modal-title").html(fileItem.name);
        $modal.find("input[name='file-path']").val(fileItem.path);
        $modal.find(".code-placeholder").text();
        $modal.find("pre.code-gspec code").html(GalenHighlightV2.specs(fileItem.content));
        $modal.modal("show");
    }
};
