package com.mtvi.arc.domain;

import java.io.File;
import java.util.Date;

/**
 * SystemTestResult.
 */
public class SystemTestResult {

    private final SystemTest test;

    private final Date startDate;

    private final Date endDate;

    private final File rawResult;

    private SystemTestResultExplanation explain;

    public SystemTestResult(SystemTest test, Date startDate, Date endDate, File rawResult) {
        if (test == null || startDate == null) {
            throw new IllegalArgumentException(
                String.format("Null or blank value has been passed in as required argument, [test=%s, startDate=%s]", test, startDate));
        }
        this.test = test;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rawResult = rawResult;
    }

    public void bindResultExplanation(SystemTestResultExplanation explanation) {
        if (explanation != null) {
            this.explain = explanation;
        }
    }

    public SystemTestResultExplanation getExplain() {
        return explain;
    }

    public SystemTest getTest() {
        return test;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public File getRawResult() {
        return rawResult;
    }
}
