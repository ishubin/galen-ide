package com.galenframework.ide.controllers;

import com.galenframework.ide.controllers.actions.DeviceActionCheckLayoutRequest;
import com.galenframework.ide.controllers.actions.DeviceActionOpenUrlRequest;
import com.galenframework.ide.controllers.actions.DeviceActionResizeRequest;
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceStatus;
import com.galenframework.ide.devices.SizeProviderCustom;
import com.galenframework.ide.devices.SizeProviderRange;
import com.galenframework.ide.model.Size;
import com.galenframework.ide.model.SizeVariation;
import com.galenframework.ide.model.docs.ApiDocRequest;
import com.galenframework.ide.model.docs.ApiDocSection;


import java.util.List;

import static com.galenframework.ide.util.JsonTransformer.toJson;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static spark.Spark.*;

public class HelpController {

    private static final List<Object> NO_REQUEST = emptyList();
    private static final String NO_DESCRIPTION = null;
    private static final List<Object> NO_RESPONSE = emptyList();

    public HelpController() {
        initRoutes();
    }

    private void initRoutes() {
        get("api/help", (req, res) -> {
            return new ApiDocSection("", asList(
                new ApiDocRequest("GET", "/api/devices", "Return list of all available devices", NO_DESCRIPTION, NO_REQUEST, asList(asList(
                    new Device(
                        "124as324tgdsg4-3t12asfasf4-12412s-aaf421",
                        "Device_1", "firefox", asList("mobile"),
                        new SizeProviderCustom(asList(new Size(1024,768), new Size(1200, 800))),
                        DeviceStatus.READY
                    )),
                    new Device(
                        "234uy3253-235iuy35235o-235oi35u",
                        "Device_2", "firefox", asList("tablet"),
                        new SizeProviderRange(new SizeVariation(new Size(600, 700), new Size(900, 700), 5, false)),
                        DeviceStatus.BUSY
                    ))
                ),
                new ApiDocRequest("POST", "/api/devices/{deviceId}/action/openUrl", "Open url in a browser", NO_DESCRIPTION,
                    asList(
                        new DeviceActionOpenUrlRequest().setUrl("http://example.com")
                    ),
                    NO_RESPONSE
                ),
                new ApiDocRequest("POST", "/api/devices/{deviceId}/action/resize", "Resize browser window", NO_DESCRIPTION,
                    asList(
                        new DeviceActionResizeRequest().setWidth(650).setHeight(450)
                    ),
                    NO_RESPONSE
                ),
                new ApiDocRequest("POST", "/api/devices/{deviceId}/action/checkLayout", "Check layout", NO_DESCRIPTION,
                    asList(
                            new DeviceActionCheckLayoutRequest().setPath("homePage.gspec").setTags(asList("mobile"))
                    ),
                    NO_RESPONSE
                )
            ));
        }, toJson());
    }


}
