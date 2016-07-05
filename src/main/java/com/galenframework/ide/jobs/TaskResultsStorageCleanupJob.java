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
package com.galenframework.ide.jobs;

import com.galenframework.ide.model.results.CommandResult;
import com.galenframework.ide.model.results.TaskResult;
import com.galenframework.ide.util.SynchronizedStorage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TaskResultsStorageCleanupJob implements Runnable {
    private final SynchronizedStorage<TaskResult> testResultsStorage;
    private final int keepLastResults;
    private final int zoombieResultsCleanupTimeInMinutes;
    private final String reportStoragePath;


    public TaskResultsStorageCleanupJob(SynchronizedStorage<TaskResult> testResultsStorage, int keepLastResults, int zoombieResultsCleanupTimeInMinutes, String reportStoragePath) {
        this.testResultsStorage = testResultsStorage;
        this.keepLastResults = keepLastResults;
        this.reportStoragePath = reportStoragePath;
        this.zoombieResultsCleanupTimeInMinutes = zoombieResultsCleanupTimeInMinutes;
    }

    @Override
    public void run() {
        try {
            clearFinishedResults();
            clearOldPlannedResults();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clearOldPlannedResults() {
        List<TaskResult> elementsToBeDeleted = testResultsStorage.stream()
            .filter((tr) -> tr.getFinishedDate() == null && minutesSince(tr.getStartedDate()) > zoombieResultsCleanupTimeInMinutes)
            .collect(toList());

        testResultsStorage.clear(elementsToBeDeleted::contains);
        removeExternalReports(elementsToBeDeleted);
    }

    private void clearFinishedResults() {
        List<TaskResult> filteredResults = testResultsStorage.stream()
            .filter((tr) -> tr.getFinishedDate() != null)
            .sorted(Comparator.comparingLong((tr) -> tr.getFinishedDate().getTime()))
            .collect(toList());

        if (filteredResults.size() > keepLastResults) {
            List<TaskResult> elementsToBeDeleted = filteredResults.subList(0, filteredResults.size() - keepLastResults);
            testResultsStorage.clear(elementsToBeDeleted::contains);
            removeExternalReports(elementsToBeDeleted);
        }
    }

    private void removeExternalReports(List<TaskResult> elementsToBeDeleted) {
        for (TaskResult taskResult : elementsToBeDeleted) {
            if (taskResult.getCommands() != null) {
                for (CommandResult commandResult : taskResult.getCommands()) {
                    if (commandResult.getExternalReportFolder() != null) {
                        deleteReportFolder(commandResult.getExternalReportFolder());
                    }
                }
            }
        }
    }

    private void deleteReportFolder(String externalReportFolder) {
        try {
            FileUtils.deleteDirectory(new File(reportStoragePath + File.separator + externalReportFolder));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static long minutesSince(Date date) {
        Date now = new Date();

        if (date != null) {
            return (now.getTime() - date.getTime()) / 60000L;
        } else {
            return now.getTime();
        }
    }
}
