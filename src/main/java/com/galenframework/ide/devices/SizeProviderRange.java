package com.galenframework.ide.devices;

import com.galenframework.ide.SizeVariation;
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
