function DevicesPanel(app) {
    DevicesPanel._super(this, "#devices-panel", "#tpl-devices-panel");
    this.app = app;
    this.devices = [];
    this.deviceModal = new DeviceModal(app);
}
extend(DevicesPanel, UIComponent);

DevicesPanel.prototype.$behavior = function () {
    return {
        click: {
            ".action-devices-add-new": function () {
                this.deviceModal.show(null, this.devices);
            },
            ".action-edit-device": function ($element) {
                var deviceId = $element.attr("data-device-id");
                var device = this.findDevice(deviceId);
                if (device) {
                    this.showEditDevicePopup(device);
                }
            },
            ".action-delete-device": function ($element) {
                var deviceId = $element.attr("data-device-id");
                var device = this.findDevice(deviceId);
                if (device.status !== "SHUTDOWN") {
                    var that = this;
                    API.devices.deleteDevice(deviceId, function () {
                        that.update();
                    });
                }
            }
        }
    };
};
DevicesPanel.prototype.update = function () {
    var that = this;
    API.devices.list(function (devices) {
        that.devices = devices;
        that.render({devices: devices});
    });
};
DevicesPanel.prototype.findDevice = function (deviceId) {
    if (this.devices) {
        for (var i = 0; i < this.devices.length; i++) {
            if (this.devices[i].deviceId === deviceId) {
                return this.devices[i];
            }
        }
    }
    return null;
};
DevicesPanel.prototype.showEditDevicePopup = function (device) {
    var sizes = null,
        sizeVariation = null;
    if (device.sizeProvider.type === "custom") {
        sizes = device.sizeProvider.sizes;
    } else if (device.sizeProvider.type === "range") {
        sizeVariation = device.sizeProvider.sizeVariation;
    }
    this.deviceModal.show({
        deviceId: device.deviceId,
        browserType: device.browserType,
        name: device.name,
        tags: device.tags,
        sizeType: device.sizeProvider.type,
        sizes: sizes,
        sizeVariation: sizeVariation
    }, this.devices);
};
