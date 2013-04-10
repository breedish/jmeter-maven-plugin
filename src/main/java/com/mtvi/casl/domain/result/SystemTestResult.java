package com.mtvi.casl.domain.result;

import com.mtvi.casl.domain.SystemTestDefinition;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.io.File;
import java.util.Date;

/**
 * SystemTestResult.
 */
public class SystemTestResult {

    /**
     * System Test Definition.
     */
    private final SystemTestDefinition test;

    /**
     * Execution Start Date.
     */
    private final Date startDate;

    /**
     * Execution end date.
     */
    private final Date endDate;

    /**
     * Results raw JMeter output file.
     */
    private final File rawResult;

    /**
     * Results explanation.
     */
    private DetailedResult explain;

    /**
     * Constructor.
     *
     * @param test - system test.
     * @param startDate - execution start date.
     * @param endDate - execution end date.
     * @param rawResult - raw result.
     */
    public SystemTestResult(SystemTestDefinition test, Date startDate, Date endDate, File rawResult) {
        if (test == null || startDate == null) {
            throw new IllegalArgumentException(
                String.format("Null or blank value has been passed in as required argument, [test=%s, startDate=%s]", test, startDate));
        }
        this.test = test;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rawResult = rawResult;
    }

    /**
     * Register explanation for system test result.
     *
     * @param explanation - explain.
     */
    public void bindResultExplanation(DetailedResult explanation) {
        if (explanation != null) {
            this.explain = explanation;
        }
    }

    public DetailedResult getExplain() {
        return explain;
    }

    public SystemTestDefinition getTest() {
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

    /**
     * @return is SystemTest was executed successfully.
     */
    public boolean isSuccess() {
        for (SampleResult sample : explain.getSamples()) {
            if (!sample.isSuccess()) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public int getFailedSamples() {
        return this.explain.getFailedSamples();
    }

    public String getExecutionTime() {
        return DurationFormatUtils.formatDuration(endDate.getTime() - startDate.getTime(), "mm:ss.SSS");
    }

}
