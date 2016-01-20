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

function isChecked(locator) {
    return $(locator).is(":checked");
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


        App.updateSpecsBrowser();
        App.updateDevices();
        App.updateTestResults();
    },
    initSettingsPanel: function () {
        whenClick(".action-settings-panel", App.showSettingsPanel);
        whenClick(".action-settings-submit", App.onSettingsPanelSubmit);

    },

    showSettingsPanel: function() {
        getJSON("api/settings", function (settings) {
            $("#settings-panel input[name='make-screenshots']").prop("checked", settings.makeScreenshots);
            $("#settings-panel .modal").modal("show");
        });

    },
    onSettingsPanelSubmit: function() {
        var settings = {
            makeScreenshots: isChecked("#settings-panel input[name='make-screenshots']")
        };
        postJSON("api/settings", settings, function () {
            $("#settings-panel .modal").modal("hide");
        });
    },

    updateDevices: function () {
        getJSON("/api/devices", function (devices) {
            App.showDevices(devices);
        });
    },
    showDevices: function (devices) {
        this.templates.devices.renderTo("#devices-panel", {devices: devices});
    },

    updateSpecsBrowser: function () {
        getJSON("/api/specs", function (items) {
            App.showSpecBrowserItems(items);
            whenClick("#specs-browser .action-launch-spec", function () {
                var specName = this.attr("data-spec-name");
                postJSON("api/tester/test", {spec: specName}, function (result) {
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
