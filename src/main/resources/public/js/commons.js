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
function extend(Child, Parent) {
    Child.prototype = inherit(Parent.prototype);
    Child.prototype.constructor = Child;
    Child.parent = Parent.prototype;
    Child._super = function (thisItem) {
        var args = [];
        for (var i = 1; i < arguments.length; i++) {
            args.push(arguments[i]);
        }
        this.parent.constructor.apply(thisItem, args);
    };
}
function inherit(proto) {
    function F() {};
    F.prototype = proto;
    return new F;
}

function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}


function toCommaSeparated(elements) {
    var text = "";
    for (var i = 0; i < elements.length; i++) {
        if (i > 0) {
            text += ", ";
        }
        text += elements[i];
    }
    return text;
}
function fromCommaSeparated(text) {
    var parts = text.split(",");
    var result = [];
    for (var i = 0; i < parts.length; i++) {
        result.push(parts[i].trim());
    }
    return result;
}
function convertSizeFromText(text) {
    var parts = text.split("x");
    if (parts.length === 2) {
        return {
            width: parseInt(parts[0]),
            height: parseInt(parts[1])
        }
    } else {
        throw new Error("Incorrect size format: " + text);
    }
};
function sizeToText(size) {
    return size.width + "x" + size.height;
}

Handlebars.registerHelper("renderDeviceSizeProvider", function (sizeProvider) {
    if (sizeProvider.type === "unsupported") {
        return new Handlebars.SafeString("<i>unsupported</i>")

    } else if (sizeProvider.type === "custom") {
        var html = '<ul>';
        for (var i = 0; i < sizeProvider.sizes.length; i++) {
            html += '<li>';
            html += '<code>' + sizeProvider.sizes[i].width + '<span class="size-splitter">x</span>'
                + sizeProvider.sizes[i].height + '</code>';
            html += '</li>';
        }
        html += '</ul>';
        return new Handlebars.SafeString(html);

    } else if (sizeProvider.type === "range") {
        html = '<div class="layout-whitespace-nowrap"><code>' + sizeProvider.sizeVariation.iterations + "</code> x ( ";
        html += '<code>' + sizeProvider.sizeVariation.start.width + '<span class="size-splitter">x</span>'
            + sizeProvider.sizeVariation.start.height + '</code>';
        html += ' until <code>' + sizeProvider.sizeVariation.end.width + '</span><span class="size-splitter">x</span>'
            + sizeProvider.sizeVariation.end.height + '</code> )';
        html += '</div>';
        return new Handlebars.SafeString(html);
    }
});

Handlebars.registerHelper("formatDurationHumanReadable", function (durationInMillis) {
    var durationInSeconds = Math.floor(durationInMillis / 1000);
    if (durationInSeconds > 0) {
        var hours = Math.floor(durationInSeconds / 3600);
        var minutes = Math.floor((durationInSeconds - hours * 3600) / 60);
        var seconds = Math.floor(durationInSeconds - hours * 3600 - minutes * 60);

        var text = "";
        if (hours > 0) {
            text += hours + "h";
        }

        if (minutes > 0 || hours > 0) {
            if (hours > 0) {
                text += " ";
            }
            text += minutes;
            text += "m";
        }

        if (seconds > 0) {
            if (hours > 0 || minutes > 0) {
                text += " ";
            }
            text += seconds;
            text += "s";
        }

        return text;
    }

    else return "0";
});

Handlebars.registerHelper("formatReportTime", function (time) {
    if (time !== null && time !== undefined) {
        var date = new Date(time);
        var hh = date.getHours();
        var mm = date.getMinutes();
        var ss = date.getSeconds();
        if (hh < 10) {hh = "0"+hh;}
        if (mm < 10) {mm = "0"+mm;}
        if (ss < 10) {ss = "0"+ss;}
        return hh + ":" + mm + ":" + ss;
    }
    return "";
});

Handlebars.registerHelper('shortText', function(text) {
    text = Handlebars.Utils.escapeExpression(text);

    if (text.length > 50) {
        text = text.substring(0, 50) + "...";
    }
    return new Handlebars.SafeString(text);
});

Handlebars.registerHelper('asHtml', function(html) {
    return new Handlebars.SafeString(html);
});


Handlebars.registerHelper('formCheckbox', function(elementName, label) {
    var randomId = "chk-" + Math.round(Math.random() * 100000) + "-" + elementName;
    var html = "<input id='" + randomId + "' name='" + elementName + "' " +
                " type='checkbox' aria-label='Screenshot' />" +
                "<label for='" + randomId + "'>" + label + "</label>"
    return new Handlebars.SafeString(html);
});

Handlebars.registerHelper('formTextfield', function(elementName, placeholder) {
    var html = "<input name='" + elementName + "' class='form-control' type='text' " +
                "placeholder='" + placeholder + "' aria-describedby='basic-addon1' />";
    return new Handlebars.SafeString(html);
});

Handlebars.registerHelper('jsonToString', function(json) {
    return JSON.stringify(json, null, 2);
});


function whenClick(locator, callback) {
    $(locator).click(function () {
        callback.call($(this));

        return false;
    });
}
