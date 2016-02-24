function SettingsModal(app) {
    SettingsModal._super(this, "#global-modal", "#tpl-settings-modal");
    this.model = new Model({
        makeScreenshots: Model.checkbox(),
        homeDirectory: Model.text(),
        domSyncMethod: Model.select(),
        chromeDriverBinPath: Model.text(),
        safariDriverBinPath: Model.text(),
        edgeDriverBinPath: Model.text(),
        ieDriverBinPath: Model.text(),
        phantomjsDriverBinPath: Model.text()
    });
}
extend(SettingsModal, UIModal);
SettingsModal.prototype.$behavior = function () {
    return {
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