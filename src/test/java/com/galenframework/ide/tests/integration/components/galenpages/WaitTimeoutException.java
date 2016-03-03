package com.galenframework.ide.tests.integration.components.galenpages;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class WaitTimeoutException extends RuntimeException {
    public WaitTimeoutException(String message, List<String> checkMessages) {
        super(message + " due to:\n" + String.join("-n", checkMessages.stream().map(checkMessage -> "    - " + checkMessage).collect(toList())));
    }
}
