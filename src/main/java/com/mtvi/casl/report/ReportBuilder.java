package com.mtvi.casl.report;

import com.mtvi.casl.config.ExecutionConfig;
import com.mtvi.casl.domain.TestExecutionException;
import com.mtvi.casl.runner.ExecutionResult;

/**
 * ReportBuilder.
 */
public interface ReportBuilder {

    /**
     * Builds report based on parsed results.
     *
     * @param result - execution result.
     * @param config - config.
     * @throws TestExecutionException - in case of error during build report.
     */
    void buildReport(ExecutionResult result, ExecutionConfig config) throws TestExecutionException;

}
