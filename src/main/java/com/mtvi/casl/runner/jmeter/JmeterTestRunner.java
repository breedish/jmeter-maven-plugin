package com.mtvi.casl.runner.jmeter;

import com.mtvi.casl.config.ExecutionConfig;
import com.mtvi.casl.domain.SystemTestDefinition;
import com.mtvi.casl.domain.SystemTestManager;
import com.mtvi.casl.domain.TestExecutionException;
import com.mtvi.casl.domain.result.SystemTestResult;
import com.mtvi.casl.runner.ExecutionResult;
import com.mtvi.casl.runner.Executor;
import com.mtvi.casl.runner.SystemTestRunner;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Set;

/**
 * JmeterTestRunner.
 */
public class JmeterTestRunner implements SystemTestRunner {

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JmeterTestRunner.class);

    /**
     * Execution Config.
     */
    private final ExecutionConfig config;

    /**
     * Test Executor.
     */
    private final Executor testExecutor;

    /**
     * System Test Manager.
     */
    private final SystemTestManager systemTestManager;

    /**
     * Constructor.
     *
     * @param config - execution config.
     * @param testManager - system test manager.
     */
    public JmeterTestRunner(ExecutionConfig config, SystemTestManager testManager) {
        this.config = config;
        this.testExecutor = new DefaultJmeterExecutor(config);
        this.systemTestManager = testManager;
    }

    @Override
    public ExecutionResult execute() throws TestExecutionException {
        Set<SystemTestDefinition> suite = systemTestManager.findSystemTests(config);

        printTestSuite(suite);

        return doExecution(suite);
    }

    protected ExecutionResult doExecution(Set<SystemTestDefinition> suite) {
        testExecutor.initExecutor();
        ExecutionResult executionResult = new ExecutionResult();

        long startTime = System.currentTimeMillis();
        int order = 1;
        for (SystemTestDefinition test : suite) {
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

        executionResult.setEndDate(new Date());

        LOGGER.info("Test Suite was executed in {}", DurationFormatUtils.formatDurationHMS(System.currentTimeMillis() - startTime));
        testExecutor.finish();
        return executionResult;
    }

    private void printTestSuite(Set<SystemTestDefinition> runSuite) {
        LOGGER.info("System Test Execution Sequence:");
        int order = 0;
        for (SystemTestDefinition test : runSuite) {
            LOGGER.info("{}. {} \t {}", ++order, test.getName(), test.getPath().getAbsolutePath());
        }
    }
}
