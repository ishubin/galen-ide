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
    this.reassignBehavior(this.$behavior);
};
UIComponent.prototype.$behavior = {};
UIComponent.prototype.reassignBehavior = function (behavior) {
    if (behavior.hasOwnProperty("click")) {
        for (var locator in behavior.click) {
            if (behavior.click.hasOwnProperty(locator)) {
                this.whenClick(locator, behavior.click[locator]);
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
UIComponent.prototype.getTextfieldText = function (name, readableName) {
    return this.$find("input[name='" + name+"']").val();
};
UIComponent.prototype.collectModel = function (model, callback) {
    var result = {};
    for (var prop in model) {
        if (model.hasOwnProperty(prop)) {
            var formElementName = model[prop];

            var $element = this.$find("*[name='" + formElementName + "']");
            if ($element.length > 0) {
                var attrType = $element.attr("type");
                if (attrType === "checkbox") {
                    result[prop] = $element.is(":checked");
                } else if (attrType === "text") {
                    result[prop] = $element.val();
                }
            } else {
                throw new Error("Couldn't find form element: " + prop);
            }
        }
    }
    callback.call(this, result);
};
UIComponent.prototype.setModel = function (model, data) {
    for (var prop in model) {
        if (model.hasOwnProperty(prop)) {
            var formElementName = model[prop];

            var $element = this.$find("*[name='" + formElementName + "']");
            if ($element.length > 0) {
                var attrType = $element.attr("type");
                if (attrType === "checkbox") {
                    if (data[prop]) {
                        $element.prop("checked", true);
                    } else {
                        $element.prop("checked", false);
                    }
                } else if (attrType === "text") {
                    $element.val(data[prop]);
                }
            } else {
                console.error("Error couldn't find form element: " + formElementName);
            }
        }
    }
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