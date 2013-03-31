package com.mtvi.arc.runner;

import com.google.common.io.NullOutputStream;
import com.lazerycode.jmeter.UtilityFunctions;
import com.lazerycode.jmeter.threadhandling.JMeterPluginUncaughtExceptionHandler;
import com.mtvi.arc.config.ExecutionConfig;
import com.mtvi.arc.domain.SystemTest;
import com.mtvi.arc.domain.SystemTestManager;
import com.mtvi.arc.domain.SystemTestResult;
import com.mtvi.arc.domain.TestExecutionException;
import org.apache.jmeter.JMeter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * JmeterTestRunner.
 */
public class JmeterTestRunner implements SystemTestRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmeterTestRunner.class);

    private final JmeterCLConfigConverter configConverter = new JmeterCLConfigConverter();

    @Override
    public ExecutionResult execute(SystemTestManager testManager, ExecutionConfig config)
        throws TestExecutionException {
        configureEnvironment(config);

        Set<SystemTest> suite = testManager.findSystemTests(config);

        printTestSuite(suite);

        return doExecution(suite, config);
    }

    private void configureEnvironment(ExecutionConfig config) {
        config.getLogsHome().mkdirs();

        //empty classpath, JMeter will automatically assemble and add all JARs in #libDir and #libExtDir and add them to the classpath. Otherwise all jars will be in the classpath twice.
        System.setProperty("java.class.path", "");

        //JMeter uses the system property "user.dir" to set its base working directory
        System.setProperty("user.dir", new File(config.getExecutorHome().getAbsolutePath(), "bin").getAbsolutePath());

        //Prevent JMeter from throwing some System.exit() calls
        System.setProperty("jmeterengine.remote.system.exit", "false");
        System.setProperty("jmeterengine.stopfail.system.exit", "false");

        System.setProperty("jmeter.home", config.getExecutorHome().getAbsolutePath());
    }

    protected ExecutionResult doExecution(Set<SystemTest> suite, ExecutionConfig config) {
        ExecutionResult executionResult = new ExecutionResult();

        final PrintStream initialPrintStream = System.out;
        System.setOut(new PrintStream(new NullOutputStream()));

        final CountDownLatch latch = new CountDownLatch(1);

        ExecutorService service = Executors.newFixedThreadPool(2);

        service.submit(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);

                boolean showOutput = false;
                while(latch.getCount() > 0) {
                    try {
                        if (scanner.hasNext() && scanner.next().equalsIgnoreCase("o")) {
                            showOutput = !showOutput;
                        }

                        if (showOutput) {
                            System.setOut(initialPrintStream);
                        } else {
                            System.setOut(new PrintStream(new NullOutputStream()));
                        }

                        TimeUnit.SECONDS.sleep(1);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        JMeter jmeter = new JMeter();
        long startTime = System.currentTimeMillis();
        int suiteSize = suite.size();
        int order = 1;
        for (SystemTest test : suite) {
            try {
                Date startDate = new Date();
                LOGGER.info("{}/{} Execution of \"{}\" started.", order, suiteSize, test.getName());

                jmeter.start(configConverter.prepareConfig(config, test));
//                NewDriver.main(configConverter.prepareConfig(config, test));
                Date endDate = new Date();
                executionResult.bindResult(new SystemTestResult(test, startDate, endDate, new File(config.getLogsHome(), test.getName() + ".jtl")));
                waitForTestToFinish(UtilityFunctions.getThreadNames(false));
            } catch (Throwable e) {
                LOGGER.error("Error during running \"{}\" test ", test.getName(), e);
            } finally {
                LOGGER.info("\t Finished .");
                order++;
            }
        }

        LOGGER.info("Test Suite was executed in {} millis", (System.currentTimeMillis() - startTime));
        System.setOut(initialPrintStream);
        latch.countDown();

        service.shutdownNow();

        return executionResult;
    }

    /**
     * Wait for one of the JMeterThreads in the list to stop.
     */
    protected void waitForTestToFinish(List<String> threadNames) throws InterruptedException {
        Thread waitThread = null;
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread thread : threadSet) {
            for (String threadName : threadNames) {
                if (threadName.equals(thread.getName())) {
                    waitThread = thread;
                    break;
                }
            }
        }
        if (waitThread != null) {
            waitThread.setUncaughtExceptionHandler(new JMeterPluginUncaughtExceptionHandler());
            waitThread.join();
        }
    }


    private void printTestSuite(Set<SystemTest> runSuite) {
        LOGGER.info("System Test Execution Sequence:");
        int order = 0;
        for (SystemTest test : runSuite) {
            LOGGER.info("{}. {} \t {}", ++order, test.getName(), test.getPath().getAbsolutePath());
        }
    }
}
