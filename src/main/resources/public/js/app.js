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

var Data = {
    devices: null,
    findDevice: function (deviceId) {
        if (this.devices) {
            for (var i = 0; i < this.devices.length; i++) {
                if (this.devices[i].deviceId === deviceId) {
                    return this.devices[i];
                }
            }
        }
        return null;
    }
};

var App = {
    templates: {},
    compileTemplate: function (id) {
        var source = $("#" + id).html();
        return new Template(Handlebars.compile(source));
    },
    initTemplates: function (pages) {
        for (var name in pages) {
            if (pages.hasOwnProperty(name)) {
                this.templates[name] = this.compileTemplate(pages[name]);
            }
        }
    },
    
    init: function () {
        this.initTemplates({
            testResults: "tpl-test-results",
            devices: "tpl-devices"
        });
        App.initProfilesPanel();
        App.initSettingsPanel();
        App.initDevicesPanel();

        this.fileBrowser = new FileBrowser(this);
        this.fileBrowser.update();

        this.loadProfilesModal = new LoadProfilesModal(this);
        this.saveProfilesModal = new SaveProfilesModal(this);
        this.settingsModal = new SettingsModal(this);

        this.deviceModal = new DeviceModal(this);

        this.testResultsPanel = new TestResultsPanel(this);
        this.testResultsPanel.update();

        App.updateDevices();
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
    initDevicesPanel: function () {
        whenClick(".action-devices-add-new", function () {
            App.deviceModal.show();
        });
    },

    submitNewDevice: function (device, callback) {
        API.devices.submitNew(device, function () {
            App.updateDevices();
            callback();
        });
    },
    showEditDevicePopup: function (device) {
        var sizes = null,
            sizeVariation = null;
        if (device.sizeProvider.type === "custom") {
            sizes = device.sizeProvider.sizes;
        } else if (device.sizeProvider.type === "range") {
            sizeVariation = device.sizeProvider.sizeVariation;
        }
        this.deviceModal.show({
            deviceId: device.deviceId,
            browserType: device.browserType,
            name: device.name,
            tags: device.tags,
            sizeType: device.sizeProvider.type,
            sizes: sizes,
            sizeVariation: sizeVariation
        });
    },
    submitUpdateDevice: function (deviceId, device, callback) {
        API.devices.update(deviceId, device, function () {
            App.updateDevices();
            callback();
        });
    },

    updateDevices: function () {
        API.devices.list(function (devices) {
            Data.devices = devices;
            App.showDevices(devices);
        });
    },
    showDevices: function (devices) {
        this.templates.devices.renderTo("#devices-panel", {devices: devices});
        whenClick("#devices-panel .action-edit-device", function () {
            var deviceId = this.attr("data-device-id");
            var device = Data.findDevice(deviceId);
            if (device) {
                App.showEditDevicePopup(device);
            }
        });

        whenClick("#devices-panel .action-delete-device", function () {
            var deviceId = this.attr("data-device-id");
            deleteJSON("api/devices/" + deviceId, function (data) {
                App.updateDevices();
            });
        });
    },

    runTest: function (specPath) {
        postJSON("api/tester/test", {specPath: specPath}, function (result) {
            App.testResultsPanel.waitForTestResults();
        });
    },

    loadFileInEditor: function (filePath) {
        this.fileBrowser.loadFileInEditor(filePath);
    },

};



$(function () {
    App.init();
})
