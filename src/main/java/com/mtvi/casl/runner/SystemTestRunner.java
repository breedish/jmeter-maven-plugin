package com.mtvi.casl.runner;

import com.mtvi.casl.domain.TestExecutionException;

/**
 * SystemTestRunner.
 */
public interface SystemTestRunner {

    ExecutionResult execute() throws TestExecutionException;

}
