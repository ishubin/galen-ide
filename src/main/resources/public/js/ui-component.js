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

function Template(tpl) {
    this._tpl = tpl;
}
Template.prototype.renderTo = function (elementLocator, data) {
    $(elementLocator).html(this._tpl(data));
};


var $UIComponent = {
    behaviors: {
        valueChange: {
            // Used in order to switch panels based on selected radio/select value
            panelSwitcher: function (panelsLocator, attrName) {
                return function (value) {
                    this.$find(panelsLocator).each(function () {
                        var $this = $(this);
                        if ($this.attr(attrName) === value) {
                            $this.show();
                        } else {
                            $this.hide();
                        }
                    });
                };
            }
        }
    },
    _createValueChangeCallback: function (component, callback) {
        return function () {
            var value = this.value;
            callback.call(component, value);
        }
    }
};

function UIComponent(locator, templateLocator) {
    this._locator = locator;
    this._templateLocator = templateLocator;
    this.compile();
}

UIComponent.prototype._tpl = null;

UIComponent.prototype.compile = function () {
    var source = $(this._templateLocator).html();
    this._tpl = new Template(Handlebars.compile(source));
};

UIComponent.prototype.$self = function () {
    return $(this._locator);
};
UIComponent.prototype.$find = function (childLocator) {
    return this.$self().find(childLocator);
};
UIComponent.prototype.render = function (data) {
    this._tpl.renderTo(this._locator, data);
    this.reassignBehavior(this.$behavior());
};
UIComponent.prototype.$behavior = function () {
    return {};
};
UIComponent.prototype.reassignBehavior = function (behavior) {
    var locator;
    if (behavior.hasOwnProperty("click")) {
        for (locator in behavior.click) {
            if (behavior.click.hasOwnProperty(locator)) {
                this.whenClick(locator, behavior.click[locator]);
            }
        }
    }

    if (behavior.hasOwnProperty("valueChange")) {
        for (locator in behavior.valueChange) {
            if (behavior.valueChange.hasOwnProperty(locator)) {
                var $element = this.$find(locator);
                $element.change($UIComponent._createValueChangeCallback(this, behavior.valueChange[locator]));

                behavior.valueChange[locator].call(this, $element.val());
            }
        }
    }
};
UIComponent.prototype.whenClick = function (elementLocator, callback) {
    var that = this;
    $(this._locator).find(elementLocator).click(function () {
        callback.call(that, $(this));
        return false;
    });
};
UIComponent.prototype.getTextfieldText = function (name) {
    return this.$find("input[name='" + name+"']").val();
};
UIComponent.prototype.collectModel = function (model, callback) {
    var that = this;
    model.collectModel(this, function (data) {
        callback.call(that, data);
    });
};
UIComponent.prototype.setModel = function (model, data) {
    model.setData(this, data);
};




function UIModal(locator, templateLocator) {
    UIModal._super(this, locator, templateLocator);
}
extend(UIModal, UIComponent);
UIModal.prototype.showModal = function () {
    var $modal = this.$find(".modal");
    $modal.modal("show");
};
UIModal.prototype.hideModal = function () {
    var $modal = this.$find(".modal");
    $modal.modal("hide");
};
UIModal.prototype.show = function () {
    this.render({});
    this.showModal();
};




var $Model = {
    property: function () {
        return new $Model.PropertyBuilder();
    },
    text: function () {
        return this.property().converter($Model.defaultConverters.trim);
    },
    checkbox: function () {
        return this.property();
    },
    number: function () {
        return this.property().converter($Model.defaultConverters.number);
    },
    radio: function () {
        return this.property();
    },
    select: function () {
        return this.property();
    },
    group: function (propertyBuilders) {
        return new $Model.GroupBuilder(propertyBuilders);
    },

    defaultConverters: {
        number: {
            read: function (text) {
                return parseInt(text);
            },
            write: function (value) {
                if (value !== null) {
                    return "" + value;
                } else {
                    return "0";
                }
            }
        },
        trim: {
            read: function (text) {
                return text.trim();
            },
            write: function (text) {
                return text;
            }
        }
    }
};


/* Validation Error */
$Model.ValidationError = function (field, $element, message) {
    this.field = field;
    this.$element = $element;
    this.message = message;
    this.errorType = "ValidationError";
};
$Model.ValidationError.prototype = Error.prototype;



/* PropertyBuilder */
$Model.PropertyBuilder = function () {
    this.validations = [];
    this.converters = [];
};
$Model.PropertyBuilder.prototype.converter = function (converter) {
    this.converters.push(converter);
    return this;
};
$Model.PropertyBuilder.prototype.required = function (errorMessage) {
    var actualErrorMessage = errorMessage || "This field is required";
    this.validations.push(function ($element, value) {
        if (value === undefined || value === null || value === "") {
            throw new $Model.ValidationError(this, $element, actualErrorMessage);
        }
    });
    return this;
};
$Model.PropertyBuilder.prototype.build = function (fieldName) {
    return new $Model.Property(fieldName, this.converters, this.validations);
};


/* GroupBuilder */
$Model.GroupBuilder = function (propertyBuilders) {
    this.propertyBuilders = propertyBuilders;
};
$Model.GroupBuilder.prototype.build = function (fieldName) {
    return new $Model.Group(fieldName, this.propertyBuilders);
};


/* Property */
$Model.Property = function (fieldName, converters, validations) {
    this.fieldName = fieldName;
    this.converters = converters || [];
    this.validations = validations || [];
};
$Model.Property.prototype.setDataValue = function (component, dataValue) {
    var $element = component.$find("*[name='" + this.fieldName + "']");
    if ($element.length > 0) {
        var attrType = $element.attr("type");
        if (attrType === "checkbox") {
            if (dataValue === true) {
                $element.prop("checked", true);
            } else {
                $element.prop("checked", false);
            }
        } else if (attrType === "radio") {
            $element.each(function () {
                var $radio = $(this);
                if ($radio.val() === dataValue) {
                    $radio.click();
                }
            });
        }
        else {
            var result = this.convertWrite($element, dataValue);
            if (result === null) {
                result = "";
            }
            $element.val(result);
        }
    } else {
        console.error("Error couldn't find form element: " + this.fieldName);
    }
};
$Model.Property.prototype.collectDataValue = function (component) {
    var $element = component.$find("*[name='" + this.fieldName + "']");

    if ($element.length > 0) {
        var attrType = $element.attr("type");

        var value = null;
        if (attrType === "checkbox") {
            value = $element.is(":checked");
        } else if (attrType === "radio") {
            value = $element.filter(":checked").val();
        } else {
            value = $element.val();
        }

        var convertedValue = this.convertRead($element, value);
        return this.validate($element, convertedValue);
    } else {
        throw new Error("Couldn't find form element: " + this.fieldName);
    }
};
$Model.Property.prototype.convertRead = function ($element, value) {
    try {
        var newValue = value;
        for (var i = 0; i < this.converters.length; i++) {
            newValue = this.converters[i].read(newValue);
        }
        return newValue;
    } catch (error) {
        if (error.errorType === "ValidationError") {
            throw error;
        } else {
            throw new $Model.ValidationError(this, $element, error.message);
        }
    }
};
$Model.Property.prototype.convertWrite = function ($element, value){
    var newValue = value;
    for (var i = 0; i < this.converters.length; i++) {
        newValue = this.converters[i].write(newValue);
    }
    return newValue;
};
$Model.Property.prototype.validate = function ($element, value) {
    for (var i = 0; i < this.validations.length; i++) {
        this.validations[i].call(this, $element, value);
    }
    return value;
};



/* Group */
$Model.Group = function (fieldName, fieldBuilders) {
    this.fieldName = fieldName;
    this.subModel = new $Model.Model(fieldBuilders);
};
$Model.Group.prototype.setDataValue = function(component, dataValue) {
};
$Model.Group.prototype.collectDataValue = function(component) {
    var $groupDiv = component.$find("*[data-model-group='" + this.fieldName + "']");
    if ($groupDiv.is(":visible")) {
        var subComponent = {
            $find: function (locator) {
                return $groupDiv.find(locator);
            }
        };

        return this.subModel._collectAllFieldData(subComponent);
    }
    return null;
};


/* Model */
$Model.Model = function (fieldBuilders) {
    this._init(fieldBuilders);
};
$Model.Model.prototype._init = function (fieldBuilders) {
    this.fields = [];
    for (var propName in fieldBuilders) {
        if (fieldBuilders.hasOwnProperty(propName)) {
            var fieldBuilder = fieldBuilders[propName];
            this.fields.push(fieldBuilder.build(propName));
        }
    }
};
$Model.Model.prototype.setData = function (component, data) {
    for (var i = 0; i < this.fields.length; i += 1) {
        var dataValue = null;
        if (data.hasOwnProperty(this.fields[i].fieldName)) {
            dataValue = data[this.fields[i].fieldName];
        }
        this.fields[i].setDataValue(component, dataValue);
    }
};
$Model.Model.prototype._collectAllFieldData = function (component) {
    var result = {};
    for (var i = 0; i < this.fields.length; i += 1) {
        result[this.fields[i].fieldName] = this.fields[i].collectDataValue(component);
    }
    return result;
};
$Model.Model.prototype.collectModel = function (component, callback) {
    var success = false;
    try {
        var result = this._collectAllFieldData(component);
        success = true;
    } catch (ex) {
        if (ex.errorType === "ValidationError") {
            ex.$element.addClass("invalid-form-value");
        } else {
            console.error(ex);
        }
    }
    if (success) {
        callback.call(component, result);
    }
};