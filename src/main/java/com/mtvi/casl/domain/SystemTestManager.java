package com.mtvi.casl.domain;

import com.mtvi.casl.config.ExecutionConfig;

import java.util.Set;

/**
 * SystemTestManager.
 */
public interface SystemTestManager {

    /**
     * Find Registered System Tests.
     *
     * @param config - config.
     * @return set of found tests.
     * @throws TestExecutionException - if error occurs during find operation.
     */
    Set<SystemTestDefinition> findSystemTests(ExecutionConfig config) throws TestExecutionException;

}
