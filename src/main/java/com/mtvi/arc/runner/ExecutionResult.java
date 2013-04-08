package com.mtvi.arc.runner;

import com.mtvi.arc.domain.result.SystemTestResult;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * ExecutionResult.
 */
public class ExecutionResult {

    private final Set<SystemTestResult> executionResults;

    private final Date startDate;

    private Date endDate;

    public ExecutionResult() {
        this.executionResults = new LinkedHashSet<SystemTestResult>();
        this.startDate = new Date();
    }

    public void bindResult(SystemTestResult result) {
        if (result != null) {
            executionResults.add(result);
        }
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Set<SystemTestResult> getExecutionResults() {
        return executionResults;
    }

    public Date getStartDate() {
        return startDate;
    }

    public boolean isSuccess() {
        for (SystemTestResult testResult : executionResults) {
            if (!testResult.isSuccess()) {
                return false;
            }
        }
        return true;
    }

}
