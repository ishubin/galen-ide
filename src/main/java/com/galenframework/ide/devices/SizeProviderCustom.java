package com.galenframework.ide.devices;


import com.galenframework.ide.Size;
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
    public void forEachIteration(DeviceThread deviceThread, Consumer<Dimension> action) {
        sizes.stream().forEach(size -> {
            Dimension dimension = size.toSeleniumDimension();
            deviceThread.resize(dimension);
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
