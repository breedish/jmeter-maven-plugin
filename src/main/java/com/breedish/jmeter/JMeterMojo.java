package com.breedish.jmeter;

import com.breedish.jmeter.console.ConsoleUtils;
import com.breedish.jmeter.execution.ExecutionResults;
import com.breedish.jmeter.execution.TestExecutionManager;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.util.List;

/**
 * JMeter Maven plugin.
 *
 * @author Tim McCune
 * @goal jmeter
 * @requiresProject true
 */
@SuppressWarnings("JavaDoc")
public class JMeterMojo extends JMeterAbstractMojo {

    /**
     * Run all the JMeter tests.
     *
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ConsoleUtils.logExpanded(getLog(), " P E R F O R M A N C E    T E S T S");

        init();

        ExecutionContext context = new ExecutionContext(getLog());

        propertyConfiguration(context);

        populateJMeterDirectoryTree();

        initialiseJMeterArgumentsArray();

        TestExecutionManager jMeterTestManager = new TestExecutionManager(
                this.testArgs, this.logsDir, this.testFilesDirectory, this.testFilesIncluded,
                this.testFilesExcluded, this.suppressJMeterOutput, context);
        jMeterTestManager.setRemoteConfig(this.remoteConfig);

        getLog().info(this.proxyConfig.toString());

        //Execute Tests
        ExecutionResults testResults = jMeterTestManager.executeTests();

        //Parse Tests Results
        parseTestResults(testResults);

        // JMeter sets this system property. to "org.apache.commons.logging.impl.LogKitLogger".
        // If another plugin is executed after this plugin that also uses (a third-party library that uses) commons-logging, but doesn't supply the same logger, execution will fail.
        // TODO: may not work if SecurityManager is enabled. Needs PropertyPermission "key", "read,write" to work.
        System.clearProperty("org.apache.commons.logging.Log");
    }

    /**
     * Scan JMeter result files for "error" and "failure" messages
     *
     * @param results List of JMeter result files.
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    protected void parseTestResults(ExecutionResults results) throws MojoExecutionException, MojoFailureException {
        ErrorScanner scanner = new ErrorScanner(this.ignoreResultErrors, this.ignoreResultFailures, getLog());
        int totalErrorCount = 0;
        int totalFailureCount = 0;
        List<ExecutionResults.TestExecutionResult> failedTests = Lists.newArrayList();
        for (ExecutionResults.TestExecutionResult file : results.getResults()) {
            if (!scanner.hasTestPassed(new File(file.getResultFileName()))) {
                totalErrorCount += scanner.getErrorCount();
                totalFailureCount += scanner.getFailureCount();
                failedTests.add(file);
            }
        }
        getLog().info(" ");
        getLog().info("Test Results:");
        getLog().info(" ");
        getLog().info("Tests Run: " + results.getResults().size() + ", Failures: " + totalFailureCount + ", Errors: " + totalErrorCount + "");
        getLog().info(" ");
        if (!failedTests.isEmpty()) {
            ConsoleUtils.showTestsInfo(getLog(), "F A I L E D  T E S T S",
                Lists.newArrayList(Iterables.transform( results.getResults(), new Function<ExecutionResults.TestExecutionResult, String>() {
                    @Override
                    public String apply(ExecutionResults.TestExecutionResult testExecutionResult) {
                        return testExecutionResult.getResultFileName();
                    }
            })));
            throw new MojoFailureException("There were " + totalErrorCount + " test errors " + "and " + totalFailureCount + " test failures.  See the JMeter logs for details.");
        }
    }
}