function TestSetupPanel(app) {
    TestSetupPanel._super(this, "#test-setup", "#tpl-test-setup");
    this.app = app;
    this.model = new Model({
        pageUrl: Model.text(),
        domSyncMethod: Model.select()
    });
}
extend(TestSetupPanel, UIComponent);

TestSetupPanel.prototype.runTest = function (path) {
    var that = this;
    this.collectModel(this.model, function (testSetup) {
        testSetup.path = path;
        API.tester.test(testSetup, function () {
            that.app.waitForTestResults();
        });

        testSetup.lastTestCommand = {path: path};
        that.render(testSetup);
        that.setModel(this.model, testSetup);
    });
};
TestSetupPanel.prototype.$behavior = function () {
    return {
        click: {
            ".action-rerun-test": function ($element) {
                var path = $element.attr("data-file-path");
                this.runTest(path);
            },
            ".file-item": function ($element) {
                var filePath = $element.attr("data-file-path");
                this.app.loadFileInEditor(filePath);
            }
        }
    }
};
