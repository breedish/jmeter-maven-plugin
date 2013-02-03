package com.breedish.jmeter;

import com.breedish.jmeter.common.UtilityFunctions;
import com.breedish.jmeter.execution.TestExecutionManager;
import org.apache.jmeter.NewDriver;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * JMeter Maven plugin.
 *
 * @author Jarrod Ribble
 * @goal gui
 * @requiresProject true
 */
@SuppressWarnings("JavaDoc")
public class JMeterGUIMojo extends JMeterAbstractMojo {

    /**
     * Load the JMeter GUI
     *
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info(" ");
        getLog().info("-------------------------------------------------------");
        getLog().info(" STARTING JMETER GUI");
        getLog().info("-------------------------------------------------------");
        getLog().info(" ");

        ExecutionContext context = new ExecutionContext(getLog());

        propertyConfiguration(context);

        populateJMeterDirectoryTree();

        initialiseJMeterArgumentsArray();

        TestExecutionManager jMeterTestManager = new TestExecutionManager(
                this.testArgs, this.logsDir, this.testFilesDirectory, this.testFilesIncluded,
                this.testFilesExcluded, this.suppressJMeterOutput, context);
        jMeterTestManager.setRemoteConfig(this.remoteConfig);

        try {
            getLog().info("JMeter is called with the following command line arguments: " + UtilityFunctions.humanReadableCommandLineOutput(testArgs.buildArgumentsArray(false)));
            //start GUI
            NewDriver.main(testArgs.buildArgumentsArray(false));
            UtilityFunctions.waitForTestToFinish(UtilityFunctions.getThreadNames(true));
        } catch (InterruptedException e) {
            getLog().info(" ");
            getLog().info("Thread Interrupt Detected!  Shutting GUI Down...");
            getLog().info("(Any interrupt stack trace after this point is expected)");
            getLog().info(" ");
        } finally {
            //Reset everything back to normal
            System.setSecurityManager(jMeterTestManager.overrideSecurityManager());
        }

    }

    /**
     * Generate the initial JMeter Arguments array that is used to create the command line that we pass to JMeter.
     *
     * @throws MojoExecutionException
     */
    @Override
    protected void initialiseJMeterArgumentsArray() throws MojoExecutionException {
        super.initialiseJMeterArgumentsArray();
        this.testArgs.setShowGUI(true);
    }
}