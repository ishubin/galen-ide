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

import com.galenframework.ide.model.SizeVariation;
import org.openqa.selenium.Dimension;

import java.util.List;
import java.util.function.Consumer;

public class SizeProviderRange extends SizeProvider {
    private final SizeVariation sizeVariation;

    public SizeProviderRange(SizeVariation sizeVariation) {
        this.sizeVariation = sizeVariation;
    }

    public SizeVariation getSizeVariation() {
        return sizeVariation;
    }

    @Override
    public String getType() {
        return "range";
    }

    @Override
    public void forEachIteration(DeviceThread deviceThread, Consumer<Dimension> action) {
        List<Dimension> sizes = sizeVariation.generateVariations();
        sizes.stream().forEach(size -> {
            deviceThread.resize(size);
            action.accept(size);
        });

    }
}
