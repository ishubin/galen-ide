package com.galenframework.ide;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class SynchronizedStorage<T> {
    private List<T> writeItems = new ArrayList<>();
    private List<T> readItems = new ArrayList<>();

    private ReentrantLock lock = new ReentrantLock();

    public List<T> get() {
        return readItems;
    }

    public void clear() {
        modifyWithLock(() -> {
            writeItems.clear();
            return null;
        });
    }

    public T add(T element) {
        return modifyWithLock(() -> {
            writeItems.add(element);
            return element;
        });
    }

    public Stream<T> stream() {
        return get().stream();
    }

    public void remove(T item) {
        modifyWithLock(() -> {
            writeItems.remove(item);
            return null;
        });
    }

    private <A> A modifyWithLock(Callback<A> returnFunc) {
        lock.lock();
        A result;
        try {
            result = returnFunc.apply();
            readItems = new ArrayList<>(writeItems);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            lock.unlock();
        }
        return result;
    }
}
