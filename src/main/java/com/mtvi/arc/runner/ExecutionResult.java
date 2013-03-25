package com.mtvi.arc.runner;

import com.mtvi.arc.domain.SystemTestResult;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * ExecutionResult.
 */
public class ExecutionResult {

    private final Set<SystemTestResult> executionResults;

    private final Date startDate;

    public ExecutionResult() {
        this.executionResults = new LinkedHashSet<SystemTestResult>();
        this.startDate = new Date();
    }

    public void bindResult(SystemTestResult result) {
        if (result != null) {
            executionResults.add(result);
        }
    }

}
