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

function Template(tpl) {
    this._tpl = tpl;
}
Template.prototype.renderTo = function (elementLocator, data) {
    $(elementLocator).html(this._tpl(data));
};


var Data = {
    specFiles: [{
        name: "loginPage.gspec"
    }, {
        name: "myNotesPage.gspec"
    }, {
        name: "welcomePage.gspec"
    }]
}

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
            testResults: "tpl-test-results"
        });

        this.templates.specsBrowser.renderTo("#specs-browser", {files: Data.specFiles});

        whenClick("#specs-browser .action-launch-spec", function () {
            var specName = this.attr("data-spec-name");
            postJSON("api/tester/test", {spec: specName}, function (result) {
                App.waitForTestResults();
            });
        });

        App.updateTestResults();
    },


    _waitForTestResultsTimer: null,
    waitForTestResults: function () {
        this._waitForTestResultsTimer = setInterval(function () {
            App.updateTestResults();
        }, 1000);
    },

    updateTestResults: function () {
        getJSON("api/tester/results", function (data) {
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
        this.templates.testResults.renderTo("#test-results", {tests: results});
    }
};



$(function () {
    App.init();
})
