package com.mtvi.casl.processors;

import com.mtvi.casl.config.ExecutionConfig;
import com.mtvi.casl.domain.TestExecutionException;
import com.mtvi.casl.runner.ExecutionResult;

/**
 * ResultProcessor.
 */
public interface ResultProcessor {

    /**
     * Processes raw JMeter results.
     *
     * @param executionResult - execution result.
     * @param config - config.
     * @throws TestExecutionException - in case of parsing error.
     */
    void process(ExecutionResult executionResult, ExecutionConfig config) throws TestExecutionException;

}
