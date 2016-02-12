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

