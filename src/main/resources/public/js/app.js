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
            specsBrowser: "tpl-specs-browser"
        });

        this.templates.specsBrowser.renderTo("#specs-browser", {files: Data.specFiles});

        whenClick("#specs-browser .action-launch-spec", function () {
            var specName = this.attr("data-spec-name");
            postJSON("api/tester/test", {spec: specName}, function (result) {
                App.waitForTestResults();
            });
        });
    },


    _waitForTestResultsTimer: null,
    waitForTestResults: function () {
        this._waitForTestResultsTimer = setInterval(function () {
            getJSON("api/tester/results", function (data) {
                if (data.status === "completed") {
                    App.showResults(data.results);
                    clearInterval(App._waitForTestResultsTimer);
                    console.log("got results, cleared interval");
                }
            });
        }, 1000);
    },

    showResults: function (results) {
        console.log("showResults", results);
    }
};



$(function () {
    App.init();
})
