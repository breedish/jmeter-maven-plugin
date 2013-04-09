package com.mtvi.casl.processors;

import com.mtvi.casl.config.ExecutionConfig;
import com.mtvi.casl.domain.TestExecutionException;
import com.mtvi.casl.runner.ExecutionResult;

/**
 * ResultProcessor.
 */
public interface ResultProcessor {

    void process(ExecutionResult executionResult, ExecutionConfig config) throws TestExecutionException;

}
