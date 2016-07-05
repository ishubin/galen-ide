/*******************************************************************************
* Copyright 2016 Ivan Shubin http://galenframework.com
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*   http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package com.galenframework.ide.util;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.function.Supplier;
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

    public void clear(Predicate<T> predicate) {
        modifyWithLock(() -> {
            writeItems.removeIf(predicate);
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

    private <A> A modifyWithLock(Supplier<A> returnFunc) {
        lock.lock();
        A result;
        try {
            result = returnFunc.get();
            readItems = new ArrayList<>(writeItems);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            lock.unlock();
        }
        return result;
    }

}
