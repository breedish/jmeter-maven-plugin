package com.mtvi.arc.domain;

import com.mtvi.arc.config.ExecutionConfig;

import java.util.Set;

/**
 * SystemTestManager.
 */
public interface SystemTestManager {

    Set<SystemTest> findSystemTests(ExecutionConfig config) throws TestExecutionException;

}
