package com.galenframework.ide.jobs;

import com.galenframework.ide.model.results.TestResultContainer;
import com.galenframework.ide.util.SynchronizedStorage;

import java.util.Date;

public class TestResultsStorageCleanupJob implements Runnable {
    private final SynchronizedStorage<TestResultContainer> testResultsStorage;
    private final int keepResultsForLastMinutes;

    public TestResultsStorageCleanupJob(SynchronizedStorage<TestResultContainer> testResultsStorage, int keepResultsForLastMinutes) {
        this.testResultsStorage = testResultsStorage;
        this.keepResultsForLastMinutes = keepResultsForLastMinutes;
    }

    @Override
    public void run() {
        long now = new Date().getTime();
        try {
            testResultsStorage.clear((tr) -> tr.getFinishedDate() != null && now - tr.getFinishedDate().getTime() > keepResultsForLastMinutes * 60000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
