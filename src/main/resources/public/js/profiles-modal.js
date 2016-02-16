function LoadProfilesModal(app) {
    SaveProfilesModal._super(this, "#global-modal", "#tpl-load-profiles-modal");
    this.app = app;
}
extend(LoadProfilesModal, UIModal);
LoadProfilesModal.prototype.$behavior = function () {
    return {
        click: {
            ".profile-file-item": function (element) {
                var path = element.attr("data-file-name");
                if (!isBlank(path)) {
                    var that = this;
                    API.profiles.load(path, function () {
                        that.hideModal();
                        that.app.updateDevices();
                    });
                }
            }
        }
    }
};
LoadProfilesModal.prototype.show = function() {
    var that = this;
    API.profiles.list(function (items) {
        that.render({items: items});
        that.showModal();
    });
};


function SaveProfilesModal(app) {
    SaveProfilesModal._super(this, "#global-modal", "#tpl-save-profiles-modal");
    this.app = app;
    this.model = new Model({
        profileName: Model.text().required()
    })
}
extend(SaveProfilesModal, UIModal);

SaveProfilesModal.prototype.$behavior = function (){
    return {
        click: {
            ".action-profiles-submit-save": function () {
                var that = this;
                this.collectModel(this.model, function (profile) {
                    API.profiles.save(profile.profileName, function () {
                        that.hideModal();
                    });
                });
            }
        }
    }
};