package com.galenframework.ide.devices;

import com.galenframework.ide.CreateDeviceRequest;
import com.galenframework.ide.Size;
import org.openqa.selenium.Dimension;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class SizeProvider {
    public abstract String getType();

    public static SizeProvider readFrom(CreateDeviceRequest createDeviceRequest) {
        switch (createDeviceRequest.getSizeType()) {
            case "custom":
                return new SizeProviderCustom(toSeleniumSizes(createDeviceRequest.getSizes()));
            case "range":
                return new SizeProviderRange(createDeviceRequest.getSizeVariation());
            case "unsupported":
                return new SizeProviderUnsupported();
            default:
                throw new RuntimeException("Unknown size type: " + createDeviceRequest.getSizeType());
        }
    }

    private static List<Dimension> toSeleniumSizes(List<Size> sizes) {
        if (sizes != null) {
            return sizes.stream().map(Size::toSeleniumDimension).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    public abstract void forEachIteration(DeviceThread deviceThread, Consumer<Dimension> action);
}
