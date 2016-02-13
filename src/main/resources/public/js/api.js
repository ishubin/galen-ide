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
    console.log("get  " + resource);
    $.getJSON(resource, callback);
}

function postJSON(resource, jsonObject, callback) {
    console.log("post  " + resource, jsonObject);
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
    profiles: {
        list: function (callback) {
            getJSON("api/profiles", function (items) {
                callback(items);
            });
        },
        load: function (path, callback) {
            postJSON("api/profiles-load/" + path, {}, function () {
                callback();
            });
        },
        save: function (name, callback) {
            postJSON("api/profiles", {
                name: name
            }, function () {
                callback();
            });
        }
    },
    files: {
        get: function (path, callback) {
            getJSON("/api/files/" + path, function (items) {
                callback(items);
            });
        },
        getFile: function (path, callback) {
            getJSON("/api/file-content/" + path, function (fileItem) {
                callback(fileItem);
            });
        }
    }
};



var APIMock = {
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
            callback();
        },
        save: function (name, callback) {
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
        }
    }
};

if (window.location.hash === "#mocked") {
    API = APIMock;
}
