package com.mtvi.casl.runner.jmeter;

import com.google.common.io.NullOutputStream;
import com.mtvi.casl.config.ExecutionConfig;
import com.mtvi.casl.domain.SystemTestDefinition;
import com.mtvi.casl.domain.result.SystemTestResult;
import com.mtvi.casl.runner.Executor;
import org.apache.jmeter.JMeter;

import java.io.File;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * DefaultJmeterExecutor class.
 */
public class DefaultJmeterExecutor implements Executor {

    /**
     * JMeter parameters converter.
     */
    private final JmeterCLConfigConverter configConverter = new JmeterCLConfigConverter();

    /**
     * Execution config.
     */
    private final ExecutionConfig config;

    /**
     * Executable.
     */
    private final JMeter executable;

    /**
     * Countdown Latch used for handling output display.
     */
    private final CountDownLatch latch = new CountDownLatch(1);

    /**
     * Executor service.
     */
    private final ExecutorService service;

    /**
     * Initial output stream.
     */
    private final PrintStream initialPrintStream = System.out;


    /**
     * Constructor.
     * @param config - execution config.
     */
    public DefaultJmeterExecutor(ExecutionConfig config) {
        this.config = config;
        this.executable = new JMeter();
        this.service = Executors.newFixedThreadPool(1);
        configureJmeterEnvironment();
    }

    private void configureJmeterEnvironment() {
        //empty classpath, JMeter will automatically assemble and add all JARs in
        // #libDir and #libExtDir and add them to the classpath. Otherwise all jars will be in the classpath twice.
        System.setProperty("java.class.path", "");
        //JMeter uses the system property "user.dir" to set its base working directory
        System.setProperty("user.dir", new File(config.getExecutorHome().getAbsolutePath(), "bin").getAbsolutePath());
        //Prevent JMeter from throwing some System.exit() calls
        System.setProperty("jmeterengine.remote.system.exit", "false");
        System.setProperty("jmeterengine.stopfail.system.exit", "false");
        System.setProperty("jmeter.home", config.getExecutorHome().getAbsolutePath());
    }


    @Override
    public SystemTestResult execute(SystemTestDefinition test) throws Exception {
        Date startDate = new Date();
        executable.start(configConverter.prepareConfig(config, test));
        waitForTestToFinish();
        return new SystemTestResult(test, startDate, new Date(), new File(config.getResultsPath(), test.getName() + ".jtl"));
    }

    /**
     * Wait for one of the JMeterThreads in the list to stop.
     */
    protected void waitForTestToFinish() throws InterruptedException {
        List<String> jmeterThreads = JMeterUtils.getThreadNames(false);

        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (jmeterThreads.contains(thread.getName())) {
                thread.join();
                break;
            }
        }
    }

    @Override
    public void initExecutor() {
        if (!config.isShowOutput()) {
            System.setOut(new PrintStream(new NullOutputStream()));
        }

        service.submit(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);

                boolean showOutput = false;
                while (latch.getCount() > 0) {
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void finish() {
        System.setOut(initialPrintStream);
        latch.countDown();
        service.shutdownNow();
    }
}
