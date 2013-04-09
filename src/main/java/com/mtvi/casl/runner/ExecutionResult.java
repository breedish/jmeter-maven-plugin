package com.mtvi.casl.runner;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.mtvi.casl.domain.result.SystemTestResult;
import com.sun.istack.internal.Nullable;

import java.util.Collections;
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

    public Set<SystemTestResult> getFailedTests() {
        return Collections.unmodifiableSet(Sets.filter(executionResults, new Predicate<SystemTestResult>() {
            @Override
            public boolean apply(@Nullable SystemTestResult test) {
                return !test.isSuccess();
            }
        }));
    }

    public int getFailedSamples() {
        int result = 0;

        for (SystemTestResult testResult : getFailedTests()) {
            result += testResult.getFailedSamples();
        }

        return result;
    }

}
