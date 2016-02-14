function SettingsModal(app) {
    UIModal._super(this, "#settings-modal", "#tpl-settings-modal");
    this.model = {
        makeScreenshots: "make-screenshots",
        homeDirectory: "home-directory",
        chromeDriverBinPath: "chrome-driver-bin-path",
        safariDriverBinPath: "safari-driver-bin-path",
        edgeDriverBinPath: "edge-driver-bin-path",
        ieDriverBinPath: "ie-driver-bin-path",
        phantomjsDriverBinPath: "phantomjs-driver-bin-path"
    };
}
extend(SettingsModal, UIModal);

SettingsModal.prototype.$behavior = {
    click: {
        ".action-settings-submit": function () {
            this.collectModel(this.model, function (settings) {
                var that = this;
                API.settings.save(settings, function () {
                    that.hideModal();
                });
            });
        }
    }
};
SettingsModal.prototype.show = function () {
    var that = this;
    API.settings.get(function (settings) {
        that.render({});
        that.setModel(that.model, settings);
        that.showModal();
    });
};