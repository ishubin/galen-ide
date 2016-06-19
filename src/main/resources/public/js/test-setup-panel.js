function TestSetupPanel(app) {
    TestSetupPanel._super(this, "#test-setup", "#tpl-test-setup");
    this.app = app;
    this.model = new Model({
        pageUrl: Model.text(),
        domSyncMethod: Model.select()
    });
}
extend(TestSetupPanel, UIComponent);

TestSetupPanel.prototype.runTest = function (specPath) {
    var that = this;
    this.collectModel(this.model, function (testSetup) {
        testSetup.specPath = specPath;
        API.tester.test(testSetup, function () {
            that.app.waitForTestResults();
        });

        testSetup.lastTestCommand = {specPath: specPath};
        that.render(testSetup);
        that.setModel(this.model, testSetup);
    });
};
TestSetupPanel.prototype.$behavior = function () {
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
