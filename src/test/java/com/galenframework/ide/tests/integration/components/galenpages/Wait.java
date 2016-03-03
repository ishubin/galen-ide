package com.galenframework.ide.tests.integration.components.galenpages;

import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.singletonList;

public class Wait {
    private int timeout = 5000;
    private int interval = 500;
    private String prefix;

    public Wait(String prefix) {
        this.prefix = prefix;
    }


    public void untilALL(List<WaitCheck> checks) {
        if (checks.isEmpty()) {
            throw new IllegalArgumentException("Check list should not be empty");
        }

        int counterTime = timeout;

        List<String> errorMessages = new LinkedList<>();

        while(counterTime > 0) {
            counterTime -= interval;
            boolean allChecksPassed = true;
            errorMessages.clear();

            for (WaitCheck check : checks) {
                if (!check.check()) {
                    allChecksPassed = false;
                    errorMessages.add(check.getName());
                }
            }

            if (allChecksPassed) {
                return;
            } else {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        throw new WaitTimeoutException("Timeout out waiting for: " + getPrefix(), errorMessages);
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void until(WaitCheckCondition waitCheckCondition) {
        untilALL(singletonList(WaitCheck.check(prefix, waitCheckCondition)));
    }
}
