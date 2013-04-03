package com.mtvi.arc.runner.jmeter;

import com.mtvi.arc.config.ExecutionConfig;
import com.mtvi.arc.domain.SystemTest;
import com.mtvi.arc.domain.SystemTestManager;
import com.mtvi.arc.domain.SystemTestResult;
import com.mtvi.arc.domain.TestExecutionException;
import com.mtvi.arc.runner.ExecutionResult;
import com.mtvi.arc.runner.Executor;
import com.mtvi.arc.runner.SystemTestRunner;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * JmeterTestRunner.
 */
public class JmeterTestRunner implements SystemTestRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmeterTestRunner.class);

    private final ExecutionConfig config;

    private final Executor testExecutor;

    private final SystemTestManager systemTestManager;

    public JmeterTestRunner(ExecutionConfig config, SystemTestManager testManager) {
        this.config = config;
        this.testExecutor = new DefaultJmeterExecutor(config);
        this.systemTestManager = testManager;
    }

    @Override
    public ExecutionResult execute() throws TestExecutionException {
        Set<SystemTest> suite = systemTestManager.findSystemTests(config);

        printTestSuite(suite);

        return doExecution(suite);
    }

    protected ExecutionResult doExecution(Set<SystemTest> suite) {
        testExecutor.initExecutor();
        ExecutionResult executionResult = new ExecutionResult();

        long startTime = System.currentTimeMillis();
        int order = 1;
        for (SystemTest test : suite) {
            try {
                LOGGER.info("{}/{} Execution of \"{}\" started.", order, suite.size(), test.getName());
                SystemTestResult result = testExecutor.execute(test);
                executionResult.bindResult(result);
                LOGGER.info("\t Finished in {}.", DurationFormatUtils.
                    formatDurationHMS(result.getEndDate().getTime() - result.getStartDate().getTime()));
            } catch (Throwable e) {
                LOGGER.error("Error during running \"{}\" test ", test.getName(), e);
            } finally {
                order++;
            }
        }

        LOGGER.info("Test Suite was executed in {}", DurationFormatUtils.formatDurationHMS(System.currentTimeMillis() - startTime));
        testExecutor.finish();
        return executionResult;
    }

    private void printTestSuite(Set<SystemTest> runSuite) {
        LOGGER.info("System Test Execution Sequence:");
        int order = 0;
        for (SystemTest test : runSuite) {
            LOGGER.info("{}. {} \t {}", ++order, test.getName(), test.getPath().getAbsolutePath());
        }
    }
}
