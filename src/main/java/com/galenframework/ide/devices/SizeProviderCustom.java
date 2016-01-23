package com.galenframework.ide.devices;

import org.openqa.selenium.Dimension;

import java.util.List;
import java.util.function.Consumer;

public class SizeProviderCustom extends SizeProvider {

    private List<Dimension> sizes;

    public SizeProviderCustom(List<Dimension> sizes) {
        this.sizes = sizes;
    }

    @Override
    public String getType() {
        return "custom";
    }

    @Override
    public void forEachIteration(DeviceThread deviceThread, Consumer<Dimension> action) {
        sizes.stream().forEach(size -> {
            deviceThread.resize(size);
            action.accept(size);
        });
    }

    public List<Dimension> getSizes() {
        return sizes;
    }

    public void setSizes(List<Dimension> sizes) {
        this.sizes = sizes;
    }
}
