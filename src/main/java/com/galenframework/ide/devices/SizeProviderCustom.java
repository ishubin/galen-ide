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
package com.galenframework.ide.devices;


import com.galenframework.ide.model.Size;
import org.openqa.selenium.Dimension;

import java.util.List;
import java.util.function.Consumer;

public class SizeProviderCustom extends SizeProvider {

    private List<Size> sizes;

    public SizeProviderCustom(List<Size> sizes) {
        this.sizes = sizes;
    }

    @Override
    public String getType() {
        return "custom";
    }

    @Override
    public void forEachIteration(Consumer<Dimension> action) {
        sizes.stream().forEach(size -> {
            Dimension dimension = size.toSeleniumDimension();
            action.accept(dimension);
        });
    }

    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }
}
