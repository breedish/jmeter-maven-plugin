package com.mtvi.casl.runner;

import com.mtvi.casl.domain.SystemTest;
import com.mtvi.casl.domain.result.SystemTestResult;

/**
 * Executor class.
 */
public interface Executor {

    SystemTestResult execute(SystemTest test) throws Exception;

    void initExecutor();

    void finish();

}
