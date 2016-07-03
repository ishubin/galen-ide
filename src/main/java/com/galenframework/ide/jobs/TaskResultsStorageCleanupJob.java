package com.galenframework.ide.jobs;

import com.galenframework.ide.model.results.TaskResult;
import com.galenframework.ide.util.SynchronizedStorage;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TaskResultsStorageCleanupJob implements Runnable {
    private final SynchronizedStorage<TaskResult> testResultsStorage;
    private final int keepLastResults;


    public TaskResultsStorageCleanupJob(SynchronizedStorage<TaskResult> testResultsStorage, int keepLastResults) {
        this.testResultsStorage = testResultsStorage;
        this.keepLastResults = keepLastResults;
    }

    @Override
    public void run() {
        try {
            List<TaskResult> filteredResults = testResultsStorage.stream()
                .filter((tr) -> tr.getFinishedDate() != null)
                .sorted(Comparator.comparingLong((tr) -> tr.getFinishedDate().getTime()))
                .collect(toList());

            if (filteredResults.size() > keepLastResults) {
                List<TaskResult> elementsToBeDeleted = filteredResults.subList(0, filteredResults.size() - keepLastResults);
                testResultsStorage.clear(elementsToBeDeleted::contains);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
