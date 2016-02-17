function TestResultsPanel(app) {
    TestResultsPanel._super(this, "#test-results", "#tpl-test-results");
    this.app = app;
    this._waitForTestResultsTimer = null;
}
extend(TestResultsPanel, UIComponent);

TestResultsPanel.prototype.$behavior = function () {
    return {
        click: {
            ".action-rerun-test": function ($element) {
                var specPath = $element.attr("data-file-path");
                this.app.runTest(specPath);
            },
            ".file-item": function ($element) {
                var filePath = $element.attr("data-file-path");
                this.app.loadFileInEditor(filePath);
            }
        }
    }
};
TestResultsPanel.prototype.show = function (results) {
    this.render({
        lastTestCommand: results.lastTestCommand,
        tests: results.testResults,
        overview: this._createTestsOverview(results.testResults)
    });
};
TestResultsPanel.prototype.update = function () {
    var that = this;
    API.results.get(function (data) {
        that.show(data);

        var amountOfFinished = 0;
        for (var i = 0; i < data.length; i++) {
            if (data[i].status === "finished") {
                amountOfFinished += 1;
            }
        }

        if (amountOfFinished > 0 && amountOfFinished === data.length) {
            clearInterval(that._waitForTestResultsTimer);
        }
    });
};
TestResultsPanel.prototype.waitForTestResults = function () {
    var that = this;
    this._waitForTestResultsTimer = setInterval(function () {
        that.update();
    }, 1000);
};
TestResultsPanel.prototype._createTestsOverview = function (results) {
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
};
