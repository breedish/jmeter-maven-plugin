package com.mtvi.arc.runner;

import com.mtvi.arc.config.ExecutionConfig;
import com.mtvi.arc.domain.SystemTestManager;
import com.mtvi.arc.domain.TestExecutionException;

/**
 * SystemTestRunner.
 */
public interface SystemTestRunner {

    ExecutionResult execute(SystemTestManager testManager, ExecutionConfig config) throws TestExecutionException;

}
