package com.breedish.jmeter.execution;

import com.breedish.jmeter.ExecutionContext;
import com.breedish.jmeter.IncludesComparator;
import com.breedish.jmeter.common.UtilityFunctions;
import com.breedish.jmeter.configuration.JMeterArgumentsArray;
import com.breedish.jmeter.configuration.RemoteConfiguration;
import com.breedish.jmeter.console.ConsoleUtils;
import com.breedish.jmeter.threadhandling.ExitException;
import com.breedish.jmeter.threadhandling.JMeterPluginSecurityManager;
import com.breedish.jmeter.threadhandling.JMeterPluginUncaughtExceptionHandler;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.jmeter.NewDriver;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.tools.ant.DirectoryScanner;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * TestExecutionManager encapsulates functions that gather JMeter Test files and execute the tests
 */
public class TestExecutionManager {

    private JMeterArgumentsArray testArgs;

    private File jmeterLog;

    private File logsDir;

    private File testFilesDirectory;

    private List<String> testFilesIncluded;

    private List<String> testFilesExcluded;

    private boolean suppressJMeterOutput;

    private boolean remoteStop = false;

    private boolean remoteStartAll = false;

    private boolean remoteStartAndStopOnce = true;

    private String remoteStart = null;

    private final ExecutionContext context;

    public TestExecutionManager(JMeterArgumentsArray testArgs, File logsDir,
                                File testFilesDirectory, List<String> testFiles, List<String> excludeTestFiles,
                                boolean suppressJMeterOutput, ExecutionContext context) {
        this.testArgs = testArgs;
        this.logsDir = logsDir;
        this.testFilesDirectory = testFilesDirectory;
        this.testFilesIncluded = testFiles;
        this.testFilesExcluded = excludeTestFiles;
        this.suppressJMeterOutput = suppressJMeterOutput;
        this.context = context;
    }

    /**
     * Set remote configuration
     *
     * @param remoteConfig
     */
    public void setRemoteConfig(RemoteConfiguration remoteConfig) {
        this.remoteStop = remoteConfig.isStop();
        this.remoteStartAll = remoteConfig.isStartAll();
        this.remoteStartAndStopOnce = remoteConfig.isStartAndStopOnce();
        if (!UtilityFunctions.isNotSet(remoteConfig.getStart())) {
            this.remoteStart = remoteConfig.getStart();
        }
    }

    /**
     * Executes all tests and returns the resultFile names
     *
     * @return the list of resultFile names
     * @throws MojoExecutionException
     */
    public ExecutionResults executeTests() throws MojoExecutionException {
        List<String> tests = generateTestList();
        ExecutionResults results = new ExecutionResults();

        logTestExecutionSequence(tests);

        for (String test : tests) {
            if (!shouldExecuteTest(test)) {
                continue;
            }

            if (!this.remoteStartAndStopOnce || tests.get(tests.size() - 1).equals(test)) {
                testArgs.setRemoteStop(this.remoteStop);
            }
            if (!this.remoteStartAndStopOnce || tests.get(0).equals(test)) {
                testArgs.setRemoteStartAll(this.remoteStartAll);
                testArgs.setRemoteStart(this.remoteStart);
            }
            results.saveResult(executeSingleTest(new File(testFilesDirectory, test)));
        }
        return results;
    }

    private boolean shouldExecuteTest(String testName) {
        return true;
    }

    private void logTestExecutionSequence(List<String> tests) {
        ConsoleUtils.showTestsInfo(getLog(), "T E S T S   E X E C U T I O N   S E Q U E N C E ", tests);
    }

    //=============================================================================================

    /**
     * Executes a single JMeter tests by building up a list of command line
     * parameters to pass to JMeter.start().
     *
     * @param test JMeter tests XML
     * @return the report file names.
     * @throws org.apache.maven.plugin.MojoExecutionException
     *          Exception
     */
    private ExecutionResults.TestExecutionResult executeSingleTest(File test) throws MojoExecutionException {
        getLog().info(" ");
        testArgs.setTestFile(test);
        //Delete results file if it already exists
        new File(testArgs.getResultsFileName()).delete();
        getLog().debug("JMeter is called with the following command line arguments: " + UtilityFunctions.humanReadableCommandLineOutput(testArgs.buildArgumentsArray()));
        SecurityManager originalSecurityManager = overrideSecurityManager();
        Thread.UncaughtExceptionHandler originalExceptionHandler = overrideUncaughtExceptionHandler();
        PrintStream originalOut = System.out;
        setJMeterLogFile(test.getName() + ".log");
        getLog().info("Executing test: " + test.getName());
        try {
            //Suppress JMeter's annoying System.out messages.
            if (suppressJMeterOutput) System.setOut(new PrintStream(new NullOutputStream()));
            //Start the tests.
            NewDriver.main(testArgs.buildArgumentsArray());
            UtilityFunctions.waitForTestToFinish(UtilityFunctions.getThreadNames(false));
        } catch (ExitException e) {
            if (e.getCode() != 0) {
                throw new MojoExecutionException("Test failed", e);
            }
        } catch (InterruptedException ex) {
            getLog().info(" ");
            getLog().info("System Exit Detected!  Stopping Test...");
            getLog().info(" ");
        } finally {
            //Reset everything back to normal
            System.setSecurityManager(originalSecurityManager);
            Thread.setDefaultUncaughtExceptionHandler(originalExceptionHandler);
            System.setOut(originalOut);
            getLog().info("Completed Test: " + test.getName());
        }
        return new ExecutionResults.TestExecutionResult(test.getName(), testArgs.getResultsFileName(), true, 0);
    }

    /**
     * Create the jmeter.log file and set the log_file system property for JMeter to pick up
     *
     * @param value
     */
    private void setJMeterLogFile(String value) {
        this.jmeterLog = new File(this.logsDir + File.separator + value);
        System.setProperty("log_file", this.jmeterLog.getAbsolutePath());
    }

    /**
     * Scan Project directories for JMeter Test Files according to includes and excludes
     *
     * @return found JMeter tests
     */
    private List<String> generateTestList() {
        List<String> jmeterTestFiles = new ArrayList<String>();
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(this.testFilesDirectory);
        scanner.setIncludes(this.testFilesIncluded == null ? new String[]{"**/*.jmx"} : this.testFilesIncluded.toArray(new String[jmeterTestFiles.size()]));
        if (this.testFilesExcluded != null) {
            scanner.setExcludes(this.testFilesExcluded.toArray(new String[testFilesExcluded.size()]));
        }
        scanner.scan();
        final List<String> includedFiles = Arrays.asList(scanner.getIncludedFiles());
        Collections.sort(includedFiles, new IncludesComparator(this.testFilesIncluded));
        jmeterTestFiles.addAll(includedFiles);
        return jmeterTestFiles;
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

    /**
     * Capture System.exit commands so that we can check to see if JMeter is trying to kill us without warning.
     *
     * @return old SecurityManager so that we can switch back to normal behaviour.
     */
    public SecurityManager overrideSecurityManager() {
        SecurityManager oldManager = System.getSecurityManager();
        System.setSecurityManager(new JMeterPluginSecurityManager());
        return oldManager;
    }

    private Log getLog() {
        return this.context.getLogger();
    }
}