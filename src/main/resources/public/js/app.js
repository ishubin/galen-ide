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
Handlebars.registerHelper("renderDeviceSizeProvider", function (sizeProvider) {
    if (sizeProvider.type === "unsupported") {
        return new Handlebars.SafeString("<i>unsupported</i>")

    } else if (sizeProvider.type === "custom") {
        var html = '<ul>';
        for (var i = 0; i < sizeProvider.sizes.length; i++) {
            html += '<li>';
            html += '<code>' + sizeProvider.sizes[i].width + '<span class="size-splitter">x</span>'
                + sizeProvider.sizes[i].height + '</code>';
            html += '</li>';
        }
        html += '</ul>';
        return new Handlebars.SafeString(html);

    } else if (sizeProvider.type === "range") {
        html = '<div class="layout-whitespace-nowrap"><code>' + sizeProvider.sizeVariation.iterations + "</code> x ( ";
        html += '<code>' + sizeProvider.sizeVariation.start.width + '<span class="size-splitter">x</span>'
            + sizeProvider.sizeVariation.start.height + '</code>';
        html += ' until <code>' + sizeProvider.sizeVariation.end.width + '</span><span class="size-splitter">x</span>'
            + sizeProvider.sizeVariation.end.height + '</code> )';
        html += '</div>';
        return new Handlebars.SafeString(html);
    }
});


var App = {
    init: function () {
        App.initProfilesPanel();
        App.initSettingsPanel();

        this.fileBrowser = new FileBrowser(this);
        this.fileBrowser.update();

        this.devicesPanel = new DevicesPanel(this);
        this.devicesPanel.update();

        this.loadProfilesModal = new LoadProfilesModal(this);
        this.saveProfilesModal = new SaveProfilesModal(this);
        this.settingsModal = new SettingsModal(this);

        this.testResultsPanel = new TestResultsPanel(this);
        this.testResultsPanel.update();

    },
    initProfilesPanel: function () {
        whenClick(".action-profiles-load", function () {
            App.loadProfilesModal.show();
        });
        whenClick(".action-profiles-save", function () {
            App.saveProfilesModal.show();
        });
    },
    initSettingsPanel: function () {
        whenClick(".action-settings-panel", function () {
            App.settingsModal.show();
        });
    },

    submitNewDevice: function (device, callback) {
        API.devices.submitNew(device, function () {
            App.updateDevices();
            callback();
        });
    },
    submitUpdateDevice: function (deviceId, device, callback) {
        API.devices.update(deviceId, device, function () {
            this.devicesPanel.update();
            callback();
        });
    },

    runTest: function (specPath) {
        postJSON("api/tester/test", {specPath: specPath}, function (result) {
            App.testResultsPanel.waitForTestResults();
        });
    },

    loadFileInEditor: function (filePath) {
        this.fileBrowser.loadFileInEditor(filePath);
    }
};



$(function () {
    App.init();
});
