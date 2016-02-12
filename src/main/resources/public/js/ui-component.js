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

UIComponent.prototype.render = function (data) {
    this._tpl.renderTo(this._locator, data);
};

UIComponent.prototype.whenClick = function (elementLocator, callback) {
    var that = this;
    $(this._locator).find(elementLocator).click(function () {
        callback.call(that, $(this));
        return false;
    });
};
