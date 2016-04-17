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
function getJSON(resource, callback) {
    $.getJSON(resource, callback);
}

function postJSON(resource, jsonObject, callback) {
    sendJSON(resource, "post", jsonObject, callback);
}
function putJSON(resource, jsonObject, callback) {
    sendJSON(resource, "put", jsonObject, callback);
}

function sendJSON(resource, method, jsonObject, callback) {
    $.ajax({
        url: resource,
        type: method,
        contentType: "application/json",
        success: function (data) {
            if (callback !== undefined && callback !== null) {
                callback(data);
            }
        },
        data: JSON.stringify(jsonObject)
    });
}

function deleteJSON(resource, callback) {
    console.log("delete " + resource);
    $.ajax({
        url: resource,
        type: 'delete',
        success: function (data) {
            if (callback !== undefined && callback !== null) {
                callback(data);
            }
        }
    });
}


var API = {
    help: {
        general: function (callback) {
            getJSON("api/help", callback);
        }  
    },
    devices: {
        submitNew: function (device, callback) {
            postJSON("api/devices", device, callback);
        },
        list: function (callback) {
            getJSON("api/devices", callback);
        },
        update: function (deviceId, device, callback) {
            putJSON("api/devices/" + deviceId, device, callback);
        },
        deleteDevice: function (deviceId, callback) {
            deleteJSON("api/devices/" + deviceId, callback);
        }
    },
    settings: {
        get: function (callback) {
            getJSON("api/settings", function (settings) {
                callback(settings);
            });
        },
        save: function (settings, callback) {
            postJSON("api/settings", settings, callback);
        }
    },
    profiles: {
        list: function (callback) {
            getJSON("api/profiles", callback);
        },
        load: function (path, callback) {
            postJSON("api/profiles-load/" + path, {}, callback);
        },
        save: function (name, callback) {
            postJSON("api/profiles", {name: name}, callback);
        }
    },
    files: {
        get: function (path, callback) {
            getJSON("/api/files/" + path, callback);
        },
        getFile: function (path, callback) {
            getJSON("/api/file-content/" + path, callback);
        },
        saveFile: function (path, fileItem, callback) {
            putJSON("/api/file-content/" + path, fileItem, callback);
        }
    },
    results: {
        get: function (callback) {
            getJSON("/api/results", callback);
        }
    }
};



var APIMock = {
    devices: {
        submitNew: function (device, callback) {
            console.log("API.devices.submitNew", device);
            callback();
        },
        list: function (callback) {
            callback([{
                "deviceId": "b13dadec-d83d-417a-ae2f-1963d677b4c3",
                "tags": ["desktop"],
                "name": "some device",
                "supportsResizing": true,
                "master": true,
                "sizeProvider": {
                    "sizes": [
                        {"width": 450, "height": 800}
                    ],
                    "type": "custom"
                },
                "status": "READY",
                "lastErrorMessage": null,
                "browserType": "chrome",
                "active": true
            },{
                "deviceId": "qwr3242-111-417a-ae2f-1963d677b4c3",
                "tags": ["desktop", "tablet"],
                "name": "some device 2",
                "supportsResizing": false,
                "master": false,
                "sizeProvider": {
                    "type": "unsupported"
                },
                "status": "READY",
                "lastErrorMessage": null,
                "browserType": "firefox",
                "active": true
            },{
                "deviceId": "03529981-6673-4f41-94b5-b060efc51a3f",
                "tags": [
                    "desktop"
                ],
                "name": "asdsd",
                "supportsResizing": true,
                "master": false,
                "sizeProvider": {
                    "sizeVariation": {
                        "start": {
                            "width": 400,
                            "height": 800
                        },
                        "end": {
                            "width": 500,
                            "height": 600
                        },
                        "iterations": 5,
                        "random": false
                    },
                    "type": "range"
                },
                "status": "READY",
                "lastErrorMessage": null,
                "browserType": "firefox",
                "active": true
            }]);
        },
        update: function (deviceId, device, callback) {
            console.log("API.devices.update", deviceId, device);
            callback();
        },
        deleteDevice: function (deviceId, callback) {
            console.log("API.devices.deleteDevice", deviceId);
            callback();
        }
    },
    settings: {
        get: function (callback) {
            callback({
                "makeScreenshots": true,
                "homeDirectory": "/home/ishubin/.galen",
                "chromeDriverBinPath": "some/chrome/driver",
                "safariDriverBinPath": null,
                "phantomjsDriverBinPath": "some/phantomjs/driver",
                "edgeDriverBinPath": null,
                "ieDriverBinPath": null
            });
        },
        save: function (settings, callback) {
            console.log("APIMock.settings.save", settings);
            callback();
        }
    },
    profiles: {
        list: function (callback) {
            callback([{
                "directory": false,
                "name": "responsive-emulation.galen",
                "path": "/home/ishubin/.galen/responsive-emulation.galen",
                "executable": false
            }, {
                "directory": false,
                "name": "crossbrowser-testing.galen",
                "path": "/home/ishubin/.galen/crossbrowser-emulation.galen",
                "executable": false
            }]);
        },
        load: function (path, callback) {
            console.log("API.profiles.load", path);
            callback();
        },
        save: function (name, callback) {
            console.log("API.profiles.save", name);
            callback();
        }
    },
    files: {
        get: function (path, callback) {
            callback([
                {
                    "directory": true,
                    "name": "..",
                    "path": ".",
                    "executable": false
                },
                {
                    "directory": true,
                    "name": "components",
                    "path": "specs/components",
                    "executable": false
                },
                {
                    "directory": true,
                    "name": "galen-extras",
                    "path": "specs/galen-extras",
                    "executable": false
                },
                {
                    "directory": false,
                    "name": "addNotePage.gspec",
                    "path": "specs/addNotePage.gspec",
                    "executable": true
                },
                {
                    "directory": false,
                    "name": "common-data.gspec",
                    "path": "specs/common-data.gspec",
                    "executable": true
                }
            ]);
        },
        getFile: function (path, callback) {
            callback({
                "directory": false,
                "name": "addNotePage.gspec",
                "path": "specs/addNotePage.gspec",
                "executable": true,
                "content": "@import common.gspec\n\n@objects\n    caption                 #content h2\n    title_textfield         input[name='note.title']\n    description_textfield   textarea\n    add_button              button.btn-primary\n    cancel_button           button.btn-default\n\n\n@groups\n    (add_note_form_element, add_note_form_elements)     caption, title_textfield, description_textfield\n    (add_note_form_button, add_note_form_buttons)       add_button, cancel_button\n    (add_note_element, add_note_elements)               &add_note_form_elements, &add_note_form_buttons\n\n\n@set\n    add_note_elements_vertical_margin   5 to 20px\n    description_height                  150 to 350px\n\n\n= Add note page =\n    | caption is at the top inside content with ${content_vertical_margin} margin\n\n    | every &add_note_element is more or less readable\n    | every &add_note_form_element stretches to content with ${content_horizontal_margin} margin\n    | every &add_note_form_button is tapable\n\n    @on desktop, tablet\n        | &add_note_form_elements are aligned vertically above each other with ${add_note_elements_vertical_margin} margin\n        | last &add_note_form_element is above add_button ${add_note_elements_vertical_margin}\n        | &add_note_form_buttons are aligned horizontally next to each other with 0 to 5px margin\n    @on mobile\n        | &add_note_elements are aligned vertically above each other with ${add_note_elements_vertical_margin} margin\n\n    title_textfield:\n        height ${form_textfield_height}\n    \n    description_textfield:\n        height ${description_height}\n"
            });
        },
        saveFile: function (path, fileItem, callback) {
            console.log("Save file", path, fileItem);
            callback();
        }
    },
    results: {
        get: function(callback) {
            callback({
                "testResults": [
                    {
                        "uniqueId": "f02b5d55-bfc8-4561-a7e0-5a8dd0af0036",
                        "testResult": {
                            "size": {
                                "width": 600,
                                "height": 500
                            },
                            "errorMessages": [
                                "\"welcome_block\" is absent on page",
                                "\"welcome_block\" is absent on page",
                                "\"greeting\" is absent on page",
                                "\"greeting\" is absent on page",
                                "\"greeting\" is absent on page",
                                "\"greeting\" is absent on page",
                                "\"greeting\" is absent on page",
                                "\"login_button\" is absent on page",
                                "\"login_button\" is absent on page",
                                "\"login_button\" is absent on page",
                                "\"login_button\" is absent on page"
                            ],
                            "errors": 11,
                            "warnings": 0,
                            "hasCrashed": false,
                            "status": "failed",
                            "exception": null,
                            "externalReport": "f02b5d55-bfc8-4561-a7e0-5a8dd0af0036-1455729714622/1-specs-welcomepage.gspec-on-some-desktop-with-size-1024x768.html",
                            "startedAt": 1455729712981,
                            "endedAt": 1455729714670,
                            "duration": 1689
                        },
                        "status": "finished",
                        "name": "Some desktop",
                        "tags": [
                            "desktop"
                        ]
                    }
                ],
                "lastTestCommand": {
                    "specPath": "specs/welcomePage.gspec"
                }
            });
        }
    },
    help: {
        general: function (callback) {
            callback({
                "section": "Api Documentation",
                "requests": [{
                    "method": "GET",
                    "path": "/api/devices",
                    "title": "Return list of all available devices",
                    "requestExamples": [],
                    "responseExamples": [
                        [
                            {
                                "deviceId": "124as324tgdsg4-3t12asfasf4-12412s-aaf421",
                                "tags": [
                                    "mobile"
                                ],
                                "name": "Device_1",
                                "supportsResizing": true,
                                "sizeProvider": {
                                    "sizes": [
                                        {
                                            "width": 1024,
                                            "height": 768
                                        },
                                        {
                                            "width": 1200,
                                            "height": 800
                                        }
                                    ],
                                    "type": "custom"
                                },
                                "status": "READY",
                                "lastErrorMessage": null,
                                "browserType": "firefox",
                                "active": true
                            }
                        ]
                    ]
                }]
            });
        }
    }
};

if (window.location.hash === "#mocked") {
    API = APIMock;
}
