package com.mtvi.casl.runner;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.mtvi.casl.domain.result.SystemTestResult;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * ExecutionResult.
 */
public class ExecutionResult {

    /**
     * Set of results for each individual system test.
     */
    private final Set<SystemTestResult> executionResults;

    /**
     * Execution start date.
     */
    private final Date startDate;

    /**
     * Execution end date.
     */
    private Date endDate;

    /**
     * Default Constructor.
     */
    public ExecutionResult() {
        this.executionResults = new LinkedHashSet<SystemTestResult>();
        this.startDate = new Date();
    }

    /**
     * Registers result.
     * @param result - system test execution result.
     */
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

    /**
     * @return true of execution was successful.
     */
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
            public boolean apply(SystemTestResult test) {
                return !test.isSuccess();
            }
        }));
    }

    /**
     * @return formatted execution time
     */
    public String getExecutionTime() {
        if (this.endDate == null) {
            this.endDate = new Date();
        }
        return DurationFormatUtils.formatDurationHMS(this.endDate.getTime() - this.startDate.getTime());
    }

    /**
     * Returns quantity of failed sample.//TODO Refactor!!
     * @return
     */
    public int getFailedSamples() {
        int result = 0;

        for (SystemTestResult testResult : getFailedTests()) {
            result += testResult.getFailedSamples();
        }

        return result;
    }

}
