package com.mtvi.arc.runner;

import com.lazerycode.jmeter.threadhandling.JMeterPluginSecurityManager;
import com.lazerycode.jmeter.threadhandling.JMeterPluginUncaughtExceptionHandler;
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

        //empty classpath, JMeter will automatically assemble and add all JARs in #libDir and #libExtDir and add them to the classpath. Otherwise all jars will be in the classpath twice.
//        System.setProperty("java.class.path", "");

        //JMeter uses the system property "user.dir" to set its base working directory
        System.setProperty("user.dir", new File(config.getExecutorHome().getAbsolutePath(), "bin").getAbsolutePath());
        //Prevent JMeter from throwing some System.exit() calls
        System.setProperty("jmeterengine.remote.system.exit", "false");
        System.setProperty("jmeterengine.stopfail.system.exit", "false");

//        System.setProperty("jmeter.home", config.getExecutorHome().getAbsolutePath());
//        overrideSecurityManager();
//        overrideUncaughtExceptionHandler();

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

    /**
     * Capture System.exit commands so that we can check to see if JMeter is trying to kill us without warning.
     *
     * @return old SecurityManager so that we can switch back to normal behaviour.
     */
    protected SecurityManager overrideSecurityManager() {
        SecurityManager oldManager = System.getSecurityManager();
        System.setSecurityManager(new JMeterPluginSecurityManager());
        return oldManager;
    }

    /**
     * Override System.exit(0) to ensure JMeter doesn't kill us without warning.
     *
     * @return old UncaughtExceptionHandler so that we can switch back to normal behaviour.
     */
    protected Thread.UncaughtExceptionHandler overrideUncaughtExceptionHandler() {
        Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new JMeterPluginUncaughtExceptionHandler());
        return oldHandler;
    }

    private void printTestSuite(Set<SystemTest> runSuite) {
        LOGGER.info("System Test Execution Sequence:");
        int order = 0;
        for (SystemTest test : runSuite) {
            LOGGER.info(String.format("%d. %s - %s", ++order, test.getName(), test.getPath().getAbsolutePath()));
        }
    }
}
