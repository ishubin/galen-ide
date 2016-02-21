package com.galenframework.ide;

public class DomSnapshot {
    private String originSource;

    public DomSnapshot(String originSource) {
        this.originSource = originSource;
    }

    public String getOriginSource() {
        return originSource;
    }

    public void setOriginSource(String originSource) {
        this.originSource = originSource;
    }
}
