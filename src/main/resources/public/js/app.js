function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}

function FormHandler(locator) {
    this.$form = $(locator);
}
FormHandler.prototype.showModal = function () {
    this.$form.modal("show");
};
FormHandler.prototype.hideModal = function () {
    this.$form.modal("hide");
};
FormHandler.prototype.setCheck = function (name, isChecked) {
    if (isChecked) {
        this.check(name);
    } else {
        this.uncheck(name);
    }
};
FormHandler.prototype.check = function (name) {
    this.$form.find("input[name='" + name + "']").prop("checked", true);
};
FormHandler.prototype.uncheck = function (name) {
    this.$form.find("input[name='" + name + "']").prop("checked", false);
};
FormHandler.prototype.select = function (name) {
    return this.$form.find("select[name='" + name +"']").val();
};
FormHandler.prototype.mandatorySelect = function (name, readableName) {
    var value = this.$form.find("select[name='" + name +"']").val();
    if (isBlank(value)) {
        throw new Error(readableName + " should not be empty");
    }
    return value;
};
FormHandler.prototype.mandatoryTextfield = function (name, readableName) {
    var value = this.textfield(name);
    if (isBlank(value)) {
        throw new Error(readableName + " should not be empty");
    }
    return value;
};
FormHandler.prototype.textfield = function (name) {
    return this.get(name);
};
FormHandler.prototype.isChecked = function(name) {
    return this.$form.find("input[name='" + name +"']").is(":checked");
};
FormHandler.prototype.radio = function (name) {
    return this.$form.find("input:radio[name='size-type']:checked").val();
};
FormHandler.prototype.disable = function (name) {
    return this.$form.find("*[name='" + name +"']").prop("disabled", true);
};
FormHandler.prototype.enable = function (name) {
    return this.$form.find("*[name='" + name +"']").prop("disabled", false);
};
FormHandler.prototype.set = function (name, value) {
    this.$form.find("input[name='" + name +"']").val(value);
};
FormHandler.prototype.get = function (name) {
    return this.$form.find("input[name='" + name +"']").val();
};
FormHandler.prototype.showSubPanel = function (groupLocator, dataType) {
    this.$form.find(groupLocator).each(function () {
        var $this = $(this);
        if ($this.attr("data-type") === dataType) {
            $this.show();
        } else {
            $this.hide();
        }
    });
};
FormHandler.prototype.selectBootstrapRadioGroup = function (containerLocator, value) {
    var $container = this.$form.find(containerLocator);
    $container.find("input:radio").each(function () {
        var $this = $(this);
        if ($this.val() === value) {
            $this.prop("checked", true);
            $this.parent().addClass("active");
        } else {
            $this.prop("checked", false);
            $this.parent().removeClass("active");
        }
    });
};


function toCommaSeparated(elements) {
    var text = "";
    for (var i = 0; i < elements.length; i++) {
        if (i > 0) {
            text += ", ";
        }
        text += elements[i];
    }
    return text;
}
function fromCommaSeparated(text) {
    var parts = text.split(",");
    var result = [];
    for (var i = 0; i < parts.length; i++) {
        result.push(parts[i].trim());
    }
    return result;
}
function convertSizeFromText(text) {
    var parts = text.split("x");
    if (parts.length === 2) {
        return {
            width: parseInt(parts[0]),
            height: parseInt(parts[1])
        }
    } else {
        throw new Error("Incorrect size format: " + text);
    }
};
function sizeToText(size) {
    return size.width + "x" + size.height;
}


Handlebars.registerHelper("formatDurationHumanReadable", function (durationInMillis) {
    var durationInSeconds = Math.floor(durationInMillis / 1000);
    if (durationInSeconds > 0) {
        var hours = Math.floor(durationInSeconds / 3600);
        var minutes = Math.floor((durationInSeconds - hours * 3600) / 60);
        var seconds = Math.floor(durationInSeconds - hours * 3600 - minutes * 60);

        var text = "";
        if (hours > 0) {
            text += hours + "h";
        }

        if (minutes > 0 || hours > 0) {
            if (hours > 0) {
                text += " ";
            }
            text += minutes;
            text += "m";
        }

        if (seconds > 0) {
            if (hours > 0 || minutes > 0) {
                text += " ";
            }
            text += seconds;
            text += "s";
        }

        return text;
    }

    else return "0";
});

Handlebars.registerHelper("formatReportTime", function (time) {
    if (time !== null && time !== undefined) {
        var date = new Date(time);
        var hh = date.getHours();
        var mm = date.getMinutes();
        var ss = date.getSeconds();
        if (hh < 10) {hh = "0"+hh;}
        if (mm < 10) {mm = "0"+mm;}
        if (ss < 10) {ss = "0"+ss;}
        return hh + ":" + mm + ":" + ss;
    }
    return "";
});

Handlebars.registerHelper('shortText', function(text) {
    text = Handlebars.Utils.escapeExpression(text);
    
    if (text.length > 50) {
        text = text.substring(0, 50) + "...";
    }
    return new Handlebars.SafeString(text);
});

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

function getJSON(resource, callback) {
    console.log("get  " + resource);
    $.getJSON(resource, callback);
}

function postJSON(resource, jsonObject, callback) {
    console.log("post  " + resource, jsonObject);
    sendJSON(resource, "post", jsonObject, callback);
}
function putJSON(resource, jsonObject, callback) {
    sendJSON(resource, "put", jsonObject, callback);
}

function sendJSON(resource, method, jsonObject, callback) {
    $.ajax({
        url: resource,
        type: method,
        contentType: "application/json",
        success: function (data) {
            if (callback !== undefined && callback !== null) {
                callback(data);
            }
        },
        data: JSON.stringify(jsonObject)
    });
}

function deleteJSON(resource, callback) {
    console.log("delete " + resource);
    $.ajax({
        url: resource,
        type: 'delete',
        success: function (data) {
            if (callback !== undefined && callback !== null) {
                callback(data);
            }
        }
    });
}

function Template(tpl) {
    this._tpl = tpl;
}
Template.prototype.renderTo = function (elementLocator, data) {
    $(elementLocator).html(this._tpl(data));
};


function whenClick(locator, callback) {
    $(locator).click(function () {
        callback.call($(this));

        return false;
    });
}

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
        for (name in pages) {
            if (pages.hasOwnProperty(name)) {
                this.templates[name] = this.compileTemplate(pages[name]);
            }
        }
    },
    
    init: function () {
        this.initTemplates({
            specsBrowser: "tpl-specs-browser",
            testResults: "tpl-test-results",
            devices: "tpl-devices",
            profilesBrowser: "tpl-profiles-browser"
        });
        App.initProfilesPanel();
        App.initSettingsPanel();
        App.initDevicesPanel();
        App.initFileEditorPanel();

        App.updateSpecsBrowser();
        App.updateDevices();
        App.updateTestResults();

    },
    initProfilesPanel: function () {
        whenClick(".action-profiles-load", App.showLoadProfilesPanel);
        whenClick(".action-profiles-save", App.showSaveProfilePanel);
        whenClick(".action-profiles-submit-save", App.submitSaveProfile);
    },
    initSettingsPanel: function () {
        whenClick(".action-settings-panel", App.showSettingsPanel);
        whenClick(".action-settings-submit", App.onSettingsPanelSubmit);
    },
    initDevicesPanel: function () {
        whenClick(".action-devices-add-new", App.showNewDevicePopup);
        whenClick("#add-device-modal .action-devices-submit", function () {
            var type = this.attr("data-type");
            if (type === "add") {
                App.submitNewDevice();
            } else if (type === "update") {
                App.submitUpdateDevice();
            }
        });

        $("#add-device-modal input:radio[name='size-type']").change(function() {
            var selectedSizeType = $("input:radio[name='size-type']:checked").val();
            $("#add-device-modal .settings-form-group-size").each(function () {
                var $this = $(this);
                if ($this.attr("data-type") === selectedSizeType) {
                    $this.show();
                } else {
                    $this.hide();
                }
            });
        });
    },
    initFileEditorPanel: function () {
        whenClick(".action-file-editor-run-test", function () {
            var f = new FormHandler("#file-editor-modal");
            var specPath = f.get("spec-path");
            App.runTest(specPath);
            f.hideModal();
        });
    },

    showLoadProfilesPanel: function () {
        getJSON("api/profiles", function (files) {
            Data.profiles = files;
            App.templates.profilesBrowser.renderTo("#load-profiles-modal-files", {items: files});

            whenClick("#load-profiles-modal .profile-file-item", function () {
                var path = $(this).attr("data-file-name");
                if (!isBlank(path)) {
                    postJSON("api/profiles-load/" + path, {}, function () {
                        $("#load-profiles-modal").modal("hide");
                        App.updateDevices();
                    });
                }
            });

            $("#load-profiles-modal").modal("show");
        });
    },
    showSaveProfilePanel: function () {
        getJSON("api/profiles", function (files) {
            Data.profiles = files;
            var f = new FormHandler("#save-profiles-modal");
            f.set("profile-name", "");
            f.showModal();
        });
    },
    submitSaveProfile: function () {
        var f = new FormHandler("#save-profiles-modal");
        var profileName = f.mandatoryTextfield("profile-name", "Profile name");

        postJSON("api/profiles", {
            name: profileName
        }, function () {
            f.hideModal();
        });
    },

    showNewDevicePopup: function () {
        var f = new FormHandler("#add-device-modal");
        f.enable("browser-type");
        f.showSubPanel(".action-devices-submit", "add");

        $("#add-device-modal .modal-title").html("Add new device");
        $("#add-device-modal").modal("show");
    },
    showEditDevicePopup: function (device) {
        var f = new FormHandler("#add-device-modal");
        f.disable("browser-type");
        f.set("name", device.name);
        f.set("device-id", device.deviceId);
        f.set("tags", toCommaSeparated(device.tags));
        $("#add-device-modal .modal-title").html("Edit device: " + device.name);

        f.selectBootstrapRadioGroup(".device-size-type", device.sizeProvider.type);
        if (device.sizeProvider.type === "custom") {
            f.set("sizes", toCommaSeparated(device.sizeProvider.sizes.map(sizeToText)));
        } else if (device.sizeProvider.type === "range") {
            f.set("size-range-start", sizeToText(device.sizeProvider.sizeVariation.start));
            f.set("size-range-end", sizeToText(device.sizeProvider.sizeVariation.end));
            f.set("size-range-iterations", device.sizeProvider.sizeVariation.iterations);
            f.setCheck("random", device.sizeProvider.sizeVariation.random);
        }
        f.showSubPanel(".action-devices-submit", "update");
        f.showSubPanel(".settings-form-group-size", device.sizeProvider.type);
        $("#add-device-modal").modal("show");
    },
    submitNewDevice: function () {
        try {
            var request = this.collectDeviceRequest();

            postJSON("api/devices", request, function () {
                App.updateDevices();
            });
            $("#add-device-modal").modal("hide");
        } catch (ex) {
            alert(ex);
        }
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
    collectDeviceRequest: function () {
        var f = new FormHandler("#add-device-modal");
        var sizeType = f.radio("size-type");

        var request = {
            browserType: f.select("browser-type", "Browser type"),
            name: f.mandatoryTextfield("name", "Device name"),
            tags: fromCommaSeparated(f.mandatoryTextfield("tags", "Tags")),
            sizeType: sizeType
        };

        if (sizeType === "custom") {
            request["sizes"] = fromCommaSeparated(f.mandatoryTextfield("sizes", "Sizes")).map(convertSizeFromText);
        } else if (sizeType === "range") {
            request["sizeVariation"] = {
                start: convertSizeFromText(f.mandatoryTextfield("size-range-start", "Size from")),
                end: convertSizeFromText(f.textfield("size-range-end")),
                iterations: parseInt(f.textfield("size-range-iterations")),
                random: f.isChecked("random")
            }
        }

        return request;
    },

    showSettingsPanel: function() {
        getJSON("api/settings", function (settings) {
            var f = new FormHandler("#settings-modal");
            f.setCheck("make-screenshots", settings.makeScreenshots);
            f.set("home-directory", settings.homeDirectory);
            f.set("chrome-driver-bin-path", settings.chromeDriverBinPath);
            f.set("safari-driver-bin-path", settings.safariDriverBinPath);
            f.set("edge-driver-bin-path", settings.edgeDriverBinPath);
            f.set("phantomjs-driver-bin-path", settings.phantomjsDriverBinPath);
            $("#settings-modal").modal("show");
        });
    },
    onSettingsPanelSubmit: function() {
        var f = new FormHandler("#settings-modal");
        var settings = {
            makeScreenshots: f.isChecked("make-screenshots"),
            chromeDriverBinPath: f.textfield("chrome-driver-bin-path"),
            safariDriverBinPath: f.textfield("safari-driver-bin-path"),
            edgeDriverBinPath: f.textfield("edge-driver-bin-path"),
            phantomjsDriverBinPath: f.textfield("phantomjs-driver-bin-path")
        };
        postJSON("api/settings", settings, function () {
            $("#settings-modal").modal("hide");
        });
    },

    updateDevices: function () {
        getJSON("/api/devices", function (devices) {
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

    updateSpecsBrowser: function () {
        getJSON("/api/specs", function (items) {
            App.showSpecBrowserItems(items);
            whenClick("#specs-browser .action-launch-spec", function () {
                var specPath = this.attr("data-spec-path");
                App.runTest(specPath);
            });
        });
    },
    showSpecBrowserItems: function (items) {
        this.templates.specsBrowser.renderTo("#specs-browser", {items: items});
        whenClick("#specs-browser a.file-item", function () {
            var filePath = this.attr("data-file-path");
            getJSON("/api/specs-content/" + filePath, function (fileItem) {
                App.showFileEditor(fileItem);
            });
        });
    },
    showFileEditor: function (fileItem) {
        var $modal = $("#file-editor-modal");
        $modal.find(".modal-title").html(fileItem.name);
        $modal.find("input[name='spec-path']").val(fileItem.path);
        $modal.find(".code-placeholder").text();
        $modal.find("pre.code-gspec code").html(GalenHighlightV2.specs(fileItem.content));
        $modal.modal("show");
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
            tests: results,
            overview: this._createTestsOverview(results)
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
