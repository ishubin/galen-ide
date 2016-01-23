package com.galenframework.ide.devices;

import com.galenframework.utils.GalenUtils;
import org.openqa.selenium.Dimension;

import java.util.function.Consumer;

public class SizeProviderUnsupported extends SizeProvider {
    @Override
    public String getType() {
        return "unsupported";
    }

    @Override
    public void forEachIteration(DeviceThread deviceThread, Consumer<Dimension> action) {
        Dimension actualSize = new Dimension(0, 0);
        try {
            java.awt.Dimension viewportArea = GalenUtils.getViewportArea(deviceThread.getDevice().getDriver());
            actualSize = new Dimension(viewportArea.width, viewportArea.height);
        } catch (Exception ex) {
        }
        action.accept(actualSize);
    }
}
