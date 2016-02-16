function LoadProfilesModal(app) {
    UIModal._super(this, "#global-modal", "#tpl-load-profiles-modal");
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
    UIModal._super(this, "#global-modal", "#tpl-save-profiles-modal");
    this.app = app;
}
extend(SaveProfilesModal, UIModal);

SaveProfilesModal.prototype.$behavior = function (){
    return {
        click: {
            ".action-profiles-submit-save": function () {
                var profileName = this.getTextfieldText("profile-name", "Profile name");
                if (!isBlank(profileName)) {
                    var that = this;
                    API.profiles.save(profileName, function () {
                        that.hideModal();
                    });
                } else {
                    alert("Profile name should not be empty");
                }
            }
        }
    }
};