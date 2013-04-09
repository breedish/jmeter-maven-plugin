package com.mtvi.casl.domain;

import com.mtvi.casl.config.ExecutionConfig;

import java.util.Set;

/**
 * SystemTestManager.
 */
public interface SystemTestManager {

    Set<SystemTest> findSystemTests(ExecutionConfig config) throws TestExecutionException;

}
