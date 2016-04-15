package com.galenframework.ide;

public interface Callback<T> {
    T apply() throws Exception;
}
