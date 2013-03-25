package com.mtvi.arc.runner;

import com.mtvi.arc.config.ExecutionConfig;
import com.mtvi.arc.domain.SystemTest;
import com.mtvi.arc.domain.SystemTestManager;
import com.mtvi.arc.domain.SystemTestResult;
import com.mtvi.arc.domain.TestExecutionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jmeter.NewDriver;

import java.io.File;
import java.util.Date;
import java.util.Set;

/**
 * JmeterTestRunner.
 */
public class JmeterTestRunner implements SystemTestRunner {

    private static final Log LOGGER = LogFactory.getLog(JmeterTestRunner.class);

    private final JmeterCLConfigConverter configConverter = new JmeterCLConfigConverter();

    @Override
    public ExecutionResult execute(SystemTestManager testManager, ExecutionConfig config)
        throws TestExecutionException {

        Set<SystemTest> suite = testManager.findSystemTests(config);

        printTestSuite(suite);

        return doExecution(suite, config);
    }

    protected ExecutionResult doExecution(Set<SystemTest> suite, ExecutionConfig config) {
        ExecutionResult executionResult = new ExecutionResult();

        for (SystemTest test : suite) {
            Date startDate = new Date();
            LOGGER.info(String.format("\t Execution of \"%s\" started.", test.getName()));
            NewDriver.main(configConverter.prepareConfig(config, test));
            LOGGER.info(String.format("\t Execution of \"%s\" finished.", test.getName()));
            Date endDate = new Date();
            executionResult.bindResult(new SystemTestResult(test, startDate, endDate, new File(config.getLogsHome(), test.getName() + ".jtl")));
        }

        return executionResult;
    }

    private void printTestSuite(Set<SystemTest> runSuite) {
        LOGGER.info("System Test Execution Sequence:");
        int order = 0;
        for (SystemTest test : runSuite) {
            LOGGER.info(String.format("%d. %s - %s", ++order, test.getName(), test.getPath().getAbsolutePath()));
        }
    }
}
