package com.mtvi.casl.runner;

import com.mtvi.casl.domain.TestExecutionException;

/**
 * SystemTestRunner.
 */
public interface SystemTestRunner {

    /**
     * Execute system tests.
     * @return - execution result.
     * @throws TestExecutionException in case of error during execution.
     */
    ExecutionResult execute() throws TestExecutionException;

}
