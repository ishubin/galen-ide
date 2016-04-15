package com.galenframework.ide.util;

public interface Callback<T> {
    T apply() throws Exception;
}
