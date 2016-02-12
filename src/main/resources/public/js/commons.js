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
function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}

function FormHandler(locator) {
    this.$form = $(locator);
}
FormHandler.prototype.showModal = function () {
    this.$form.modal("show");
};
FormHandler.prototype.hideModal = function () {
    this.$form.modal("hide");
};
FormHandler.prototype.setCheck = function (name, isChecked) {
    if (isChecked) {
        this.check(name);
    } else {
        this.uncheck(name);
    }
};
FormHandler.prototype.check = function (name) {
    this.$form.find("input[name='" + name + "']").prop("checked", true);
};
FormHandler.prototype.uncheck = function (name) {
    this.$form.find("input[name='" + name + "']").prop("checked", false);
};
FormHandler.prototype.select = function (name) {
    return this.$form.find("select[name='" + name +"']").val();
};
FormHandler.prototype.mandatorySelect = function (name, readableName) {
    var value = this.$form.find("select[name='" + name +"']").val();
    if (isBlank(value)) {
        throw new Error(readableName + " should not be empty");
    }
    return value;
};
FormHandler.prototype.mandatoryTextfield = function (name, readableName) {
    var value = this.textfield(name);
    if (isBlank(value)) {
        throw new Error(readableName + " should not be empty");
    }
    return value;
};
FormHandler.prototype.textfield = function (name) {
    return this.get(name);
};
FormHandler.prototype.isChecked = function(name) {
    return this.$form.find("input[name='" + name +"']").is(":checked");
};
FormHandler.prototype.radio = function (name) {
    return this.$form.find("input:radio[name='size-type']:checked").val();
};
FormHandler.prototype.disable = function (name) {
    return this.$form.find("*[name='" + name +"']").prop("disabled", true);
};
FormHandler.prototype.enable = function (name) {
    return this.$form.find("*[name='" + name +"']").prop("disabled", false);
};
FormHandler.prototype.set = function (name, value) {
    this.$form.find("input[name='" + name +"']").val(value);
};
FormHandler.prototype.get = function (name) {
    return this.$form.find("input[name='" + name +"']").val();
};
FormHandler.prototype.showSubPanel = function (groupLocator, dataType) {
    this.$form.find(groupLocator).each(function () {
        var $this = $(this);
        if ($this.attr("data-type") === dataType) {
            $this.show();
        } else {
            $this.hide();
        }
    });
};
FormHandler.prototype.selectBootstrapRadioGroup = function (containerLocator, value) {
    var $container = this.$form.find(containerLocator);
    $container.find("input:radio").each(function () {
        var $this = $(this);
        if ($this.val() === value) {
            $this.prop("checked", true);
            $this.parent().addClass("active");
        } else {
            $this.prop("checked", false);
            $this.parent().removeClass("active");
        }
    });
};


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

function Template(tpl) {
    this._tpl = tpl;
}
Template.prototype.renderTo = function (elementLocator, data) {
    $(elementLocator).html(this._tpl(data));
};


function whenClick(locator, callback) {
    $(locator).click(function () {
        callback.call($(this));

        return false;
    });
}
