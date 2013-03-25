package com.mtvi.arc.report;

import com.mtvi.arc.config.ExecutionConfig;
import com.mtvi.arc.runner.ExecutionResult;

/**
 * ReportBuilder.
 */
public interface ReportBuilder {

    void buildReport(ExecutionResult result, ExecutionConfig config);

}
