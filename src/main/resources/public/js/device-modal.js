var TagsConverter = {
    read: function (text) {
        if (text !== null && text != "") {
            return fromCommaSeparated(text);
        } else {
            return null;
        }
    },
    write: function (value) {
        if (value !== null) {
            return value.join(", ");
        } else {
            return "";
        }
    }
};

var SizeArrayConverter = {
    read: function (text) {
        if (text !== null && text != "") {
            return fromCommaSeparated(text).map(convertSizeFromText);
        } else {
            return null;
        }
    },
    write: function (value) {
        if (value !== null && Array.isArray(value)) {
            return value.map(sizeToText).join(", ");
        } else {
            return "";
        }
    }
};

var SizeConverter = {
    read: function (text) {
        if (text !== null && text != "") {
            return convertSizeFromText(text);
        } else {
            return null;
        }
    },
    write: function (value) {
        if (value !== null) {
            return sizeToText(value);
        } else {
            return "";
        }
    }
};


function DeviceModal(app) {
    UIModal._super(this, "#global-modal", "#tpl-device-modal");
    this.app = app;
    this.device = null;
    this.model = new $Model.Model({
        browserType: $Model.select(),
        name: $Model.text().required("A name of a browser should not be empty"),
        tags: $Model.text().converter(TagsConverter),
        sizeType: $Model.radio(),
        sizeProviderCustom: $Model.group({
            sizes: $Model.text().required().converter(SizeArrayConverter),
        }),
        sizeVariation: $Model.group({
            start: $Model.property().required().converter(SizeConverter),
            end: $Model.property().required().converter(SizeConverter),
            iterations: $Model.number().required(),
            random: $Model.checkbox()
        })
    });
}
extend(DeviceModal, UIModal);
DeviceModal.prototype.$behavior = function () {
    return {
        valueChange: {
            "*[name='sizeType']": $UIComponent.behaviors.valueChange.panelSwitcher(".settings-form-group-size", "data-type")
        },
        click: {
            ".action-device-submit": function () {
                this.collectModel(this.model, function (device) {
                    if (device.sizeProviderCustom !== null) {
                        device.sizes = device.sizeProviderCustom.sizes;
                    }
                    delete device.sizeProviderCustom;

                    var that = this;
                    if (this.device !== null) {
                        this.app.submitUpdateDevice(this.device.deviceId, device, function () {
                            that.hideModal();
                        });
                    } else {
                        this.app.submitNewDevice(device, function () {
                            that.hideModal();
                        });
                    }
                });
            }
        }
    }
};
DeviceModal.prototype.show = function (device) {
    this.device = device || null;
    this.render({device: this.device});
    if (this.device !== null) {
        this.setModel(this.model, this.device);
    }
    this.showModal();
};