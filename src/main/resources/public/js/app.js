function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}

function FormHandler(locator) {
    this.$form = $(locator);
}
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
    return this.$form.find("input[name='" + name +"']").val();
};
FormHandler.prototype.isChecked = function(name) {
    return this.$form.find("input[name='" + name +"']").is(":checked");
};


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

function getJSON(resource, callback) {
    console.log("get  " + resource);
    $.getJSON(resource, callback);
}

function postJSON(resource, jsonObject, callback) {
    console.log("post " + resource);
    $.ajax({
        url: resource,
        type: 'post',
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
            fileEditor: "tpl-file-editor"
        });
        App.initSettingsPanel();
        App.initDevicesPanel();


        App.updateSpecsBrowser();
        App.updateDevices();
        App.updateTestResults();
    },
    initSettingsPanel: function () {
        whenClick(".action-settings-panel", App.showSettingsPanel);
        whenClick(".action-settings-submit", App.onSettingsPanelSubmit);
    },
    initDevicesPanel: function () {
        whenClick(".action-devices-add-new", App.showNewDevicePopup);
        whenClick(".action-devices-add-new-submit", App.submitNewDevice);
    },
    showNewDevicePopup: function () {
        $("#add-device-modal").modal("show");
    },
    submitNewDevice: function () {
        try {
            var f = new FormHandler("#add-device-modal");

            var request = {
                browserType: f.mandatorySelect("browser-type", "Browser type"),
                name: f.mandatoryTextfield("name", "Device name"),
                tags: fromCommaSeparated(f.mandatoryTextfield("tags", "Tags")),
                //sizes: fromCommaSeparated(f.mandatoryTextfield("sizes", "Sizes")).map(convertSizeFromText)
                sizeVariation: {
                    start: convertSizeFromText(f.mandatoryTextfield("size-range-start", "Size from")),
                    end: convertSizeFromText(f.textfield("size-range-end")),
                    iterations: parseInt(f.textfield("size-range-iterations"))
                }
            };
            postJSON("api/devices", request, function () {
                App.updateDevices();
            });
            $("#add-device-modal").modal("hide");

        } catch (error) {
            alert(error);
        }
    },

    showSettingsPanel: function() {
        getJSON("api/settings", function (settings) {
            $("#settings-modal input[name='make-screenshots']").prop("checked", settings.makeScreenshots);
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
        };
        postJSON("api/settings", settings, function () {
            $("#settings-modal").modal("hide");
        });
    },

    updateDevices: function () {
        getJSON("/api/devices", function (devices) {
            App.showDevices(devices);
        });
    },
    showDevices: function (devices) {
        this.templates.devices.renderTo("#devices-panel", {devices: devices});
        whenClick("#devices-panel .action-delete-device", function () {
            var deviceId = this.attr("data-device-id");
            deleteJSON("api/devices/" + deviceId, function (data) {
                App.updateDevices();
            });
        });
    },

    updateSpecsBrowser: function () {
        getJSON("/api/specs", function (items) {
            App.showSpecBrowserItems(items);
            whenClick("#specs-browser .action-launch-spec", function () {
                var specPath = this.attr("data-spec-path");
                postJSON("api/tester/test", {specPath: specPath}, function (result) {
                    App.waitForTestResults();
                });
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
        this.templates.fileEditor.renderTo("#file-editor", {fileItem: fileItem});
        var $content = $("#file-editor pre.code-gspec");
        $content.html(GalenHighlightV2.specs($content.html()));

        $("#file-editor > .modal").modal("show");
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
