package com.galenframework.ide.jobs;

import com.galenframework.ide.model.results.TestResultContainer;
import com.galenframework.ide.util.SynchronizedStorage;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TestResultsStorageCleanupJob implements Runnable {
    private final SynchronizedStorage<TestResultContainer> testResultsStorage;
    private final int keepLastResults;


    public TestResultsStorageCleanupJob(SynchronizedStorage<TestResultContainer> testResultsStorage, int keepLastResults) {
        this.testResultsStorage = testResultsStorage;
        this.keepLastResults = keepLastResults;
    }

    @Override
    public void run() {
        try {
            List<TestResultContainer> filteredResults = testResultsStorage.stream()
                .filter((tr) -> tr.getFinishedDate() != null)
                .sorted(Comparator.comparingLong((tr) -> tr.getFinishedDate().getTime()))
                .collect(toList());

            if (filteredResults.size() > keepLastResults) {
                List<TestResultContainer> elementsToBeDeleted = filteredResults.subList(0, filteredResults.size() - keepLastResults);
                testResultsStorage.clear(elementsToBeDeleted::contains);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
