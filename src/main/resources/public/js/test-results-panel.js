function TestResultsPanel(app) {
    TestResultsPanel._super(this, "#test-results", "#tpl-test-results");
    this.app = app;
    this._waitForTestResultsTimer = null;
}
extend(TestResultsPanel, UIComponent);

TestResultsPanel.prototype.show = function (results) {
    this.render({
        tests: this._enrichWithExtraInfo(results.taskResults),
        overview: this._createTestsOverview(results.taskResults)
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
TestResultsPanel.prototype._enrichWithExtraInfo = function (taskResults) {
    for (var i = 0; i < taskResults.length; i++) {
        if  (taskResults[i].finishedDate !== null && taskResults[i].startedDate !== null) {
            taskResults[i].duration = taskResults[i].finishedDate - taskResults[i].startedDate;
        }

        var externalReports = [];

        var errorMessages = [];

        if (taskResults[i].commands != null) {
            for (var j = 0; j < taskResults[i].commands.length; j++) {
                if (taskResults[i].commands[j].externalReport !== null) {
                    externalReports.push(taskResults[i].commands[j].externalReport);
                }
                if (taskResults[i].commands[j].errorMessages !== null && taskResults[i].commands[j].errorMessages !== undefined) {
                    for (var k = 0; k < taskResults[i].commands[j].errorMessages.length; k++) {
                        errorMessages.push(taskResults[i].commands[j].errorMessages[k]);
                    }
                }
            }
        }

        taskResults[i].externalReports = externalReports;
        taskResults[i].errorMessages = errorMessages;
    }
    return taskResults;
};
TestResultsPanel.prototype._createTestsOverview = function (taskResults) {
    var firstTimestamp = -1;
    var lastTimestamp = -1;

    var allTestsAreFinished = true;

    for (var i = 0; i < taskResults.length; i++) {
        if  (taskResults[i].finishedDate !== null) {
            if (firstTimestamp < 0 || firstTimestamp > taskResults[i].startedDate) {
                firstTimestamp = taskResults[i].startedDate;
            }

            if (lastTimestamp < 0 || lastTimestamp < taskResults[i].finishedDate) {
                lastTimestamp = taskResults[i].finishedDate;
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
