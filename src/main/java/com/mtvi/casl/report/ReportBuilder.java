package com.mtvi.casl.report;

import com.mtvi.casl.config.ExecutionConfig;
import com.mtvi.casl.domain.TestExecutionException;
import com.mtvi.casl.runner.ExecutionResult;

/**
 * ReportBuilder.
 */
public interface ReportBuilder {

    void buildReport(ExecutionResult result, ExecutionConfig config) throws TestExecutionException;

}
