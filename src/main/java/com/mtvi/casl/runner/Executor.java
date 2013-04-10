package com.mtvi.casl.runner;

import com.mtvi.casl.domain.SystemTestDefinition;
import com.mtvi.casl.domain.result.SystemTestResult;

/**
 * Executor class.
 */
public interface Executor {

    SystemTestResult execute(SystemTestDefinition test) throws Exception;

    void initExecutor();

    void finish();

}
