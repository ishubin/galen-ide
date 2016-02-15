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

        App.updateDevices();
        App.updateTestResults();

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
        var sizes = null;
        if (device.sizeProvider.type === "custom") {
            sizes = device.sizeProvider.sizes;
        }
        this.deviceModal.show({
            deviceId: device.deviceId,
            browserType: device.browserType,
            name: device.name,
            tags: device.tags,
            sizeType: device.sizeProvider.type,
            sizes: sizes,
            sizeVariation: null
        });
    },
    submitUpdateDevice: function () {
        try {
            var request = this.collectDeviceRequest();
            var deviceId = $("#add-device-modal input[name='device-id']").val();

            putJSON("api/devices/" + deviceId, request, function () {
                App.updateDevices();
            });
            $("#add-device-modal").modal("hide");
        } catch (ex) {
            alert(ex);
        }
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
            App.waitForTestResults();
        });
    },

    _waitForTestResultsTimer: null,
    waitForTestResults: function () {
        this._waitForTestResultsTimer = setInterval(function () {
            App.updateTestResults();
        }, 1000);
    },

    updateTestResults: function () {
        getJSON("/api/tester/results", function (data) {
            App.showResults(data);

            var amountOfFinished = 0;
            for (var i = 0; i < data.length; i++) {
                if (data[i].status === "finished") {
                    amountOfFinished += 1;
                }
            }

            if (amountOfFinished > 0 && amountOfFinished === data.length) {
                clearInterval(App._waitForTestResultsTimer);
            }
        });
    },
    showResults: function (results) {
        console.log("rendering results",  results);
        this.templates.testResults.renderTo("#test-results", {
            lastTestCommand: results.lastTestCommand,
            tests: results.testResults,
            overview: this._createTestsOverview(results.testResults)
        });

        whenClick("#test-results .action-rerun-test", function () {
            var specPath = this.attr("data-file-path");
            App.runTest(specPath);
        });
        whenClick("#test-results .file-item", function () {
            var filePath = this.attr("data-file-path");
            App.loadFileInEditor(filePath);
        });
    },
    _createTestsOverview: function (results) {
        var firstTimestamp = -1;
        var lastTimestamp = -1;

        var allTestsAreFinished = true;

        for (var i = 0; i < results.length; i++) {
            if (results[i].status === "finished" && results[i].testResult !== null && results[i].testResult !== undefined) {
                if (firstTimestamp < 0 || firstTimestamp > results[i].testResult.startedAt) {
                    firstTimestamp = results[i].testResult.startedAt;
                }

                if (lastTimestamp < 0 || lastTimestamp < results[i].testResult.endedAt) {
                    lastTimestamp = results[i].testResult.endedAt;
                }

            } else {
                allTestsAreFinished = false;
            }
        }

        var totalDuration = null;
        if (firstTimestamp > 0 && lastTimestamp > 0 && allTestsAreFinished) {
            totalDuration = lastTimestamp - firstTimestamp;
        }

        return {
            totalDuration: totalDuration,
            startedAt: firstTimestamp,
            endedAt: firstTimestamp
        };
    }
};



$(function () {
    App.init();
})
