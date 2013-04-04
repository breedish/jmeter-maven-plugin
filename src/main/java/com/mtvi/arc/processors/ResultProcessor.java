package com.mtvi.arc.processors;

import com.mtvi.arc.config.ExecutionConfig;
import com.mtvi.arc.domain.TestExecutionException;
import com.mtvi.arc.runner.ExecutionResult;

/**
 * ResultProcessor.
 */
public interface ResultProcessor {

    void process(ExecutionResult executionResult, ExecutionConfig config) throws TestExecutionException;

}
