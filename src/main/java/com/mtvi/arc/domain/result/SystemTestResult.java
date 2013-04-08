package com.mtvi.arc.domain.result;

import com.mtvi.arc.domain.SystemTest;
import org.apache.commons.lang.time.DurationFormatUtils;

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

    private DetailedResult explain;

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

    public void bindResultExplanation(DetailedResult explanation) {
        if (explanation != null) {
            this.explain = explanation;
        }
    }

    public DetailedResult getExplain() {
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

    public boolean isSuccess() {
        for (SampleResult sample : explain.getSamples()) {
            if (!sample.isSuccess()) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public String getExecutionTime() {
        return DurationFormatUtils.formatDuration(endDate.getTime() - startDate.getTime(), "mm:ss.SSS");
    }

}
