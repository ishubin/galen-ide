/*******************************************************************************
 * Copyright 2016 Ivan Shubin http://galenframework.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

function App() {
    App._super(this, "body");
    this.fileBrowser = new FileBrowser(this);
    this.devicesPanel = new DevicesPanel(this);
    this.loadProfilesModal = new LoadProfilesModal(this);
    this.saveProfilesModal = new SaveProfilesModal(this);
    this.settingsModal = new SettingsModal(this);
    this.testResultsPanel = new TestResultsPanel(this);
    this.testSetupPanel = new TestSetupPanel(this);

    this.render();
    this.init();
}
extend(App, UIComponent);
App.prototype.$behavior = function () {
    return {
        click: {
            ".action-profiles-load": function () {
                this.loadProfilesModal.show();
            },
            ".action-profiles-save": function () {
                this.saveProfilesModal.show();
            },
            ".action-settings-panel": function () {
                this.settingsModal.show();
            }
        }
    };
};
App.prototype.init = function () {
    this.fileBrowser.update();
    this.devicesPanel.update();
    this.testResultsPanel.update();
    this.testSetupPanel.render();
};
App.prototype.runTest = function (specPath) {
    this.testSetupPanel.runTest(specPath);
};
App.prototype.loadFileInEditor = function (filePath) {
    this.fileBrowser.loadFileInEditor(filePath);
};
App.prototype.updateDevices = function () {
    this.devicesPanel.update();
};
App.prototype.waitForTestResults = function () {
    this.testResultsPanel.waitForTestResults();
};

var app = null;

$(function () {
    app = new App();
});
