package com.mtvi.arc.runner;

import com.mtvi.arc.domain.SystemTest;
import com.mtvi.arc.domain.SystemTestResult;

/**
 * Executor class.
 */
public interface Executor {

    SystemTestResult execute(SystemTest test) throws Exception;

    void initExecutor();

    void finish();

}
