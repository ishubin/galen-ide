package com.galenframework.ide.devices;

import com.galenframework.ide.DeviceRequest;
import org.openqa.selenium.Dimension;

import java.util.function.Consumer;

public abstract class SizeProvider {
    public abstract String getType();

    public static SizeProvider readFrom(DeviceRequest createDeviceRequest) {
        switch (createDeviceRequest.getSizeType()) {
            case "custom":
                return new SizeProviderCustom(createDeviceRequest.getSizes());
            case "range":
                return new SizeProviderRange(createDeviceRequest.getSizeVariation());
            case "unsupported":
                return new SizeProviderUnsupported();
            default:
                throw new RuntimeException("Unknown size type: " + createDeviceRequest.getSizeType());
        }
    }

    public abstract void forEachIteration(DeviceThread deviceThread, Consumer<Dimension> action);
}
