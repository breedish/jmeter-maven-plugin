package com.mtvi.arc.runner;

import com.mtvi.arc.domain.TestExecutionException;

/**
 * SystemTestRunner.
 */
public interface SystemTestRunner {

    ExecutionResult execute() throws TestExecutionException;

}
