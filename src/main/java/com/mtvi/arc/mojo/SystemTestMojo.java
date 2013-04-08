package com.mtvi.arc.mojo;

import com.mtvi.arc.config.ExecutionConfig;
import com.mtvi.arc.domain.DefaultSystemTestManager;
import com.mtvi.arc.domain.TestExecutionException;
import com.mtvi.arc.processors.JMeterResultProcessor;
import com.mtvi.arc.processors.ResultProcessor;
import com.mtvi.arc.report.DefaultReportBuilder;
import com.mtvi.arc.report.ReportBuilder;
import com.mtvi.arc.runner.ExecutionResult;
import com.mtvi.arc.runner.SystemTestRunner;
import com.mtvi.arc.runner.jmeter.JmeterTestRunner;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

/**
 * JMeter Maven plugin.
 *
 * @author zenind
 * @goal jmeter
 * @requiresProject true
 */
@SuppressWarnings("all")
public class SystemTestMojo extends AbstractMojo {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(SystemTestMojo.class);

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject mavenProject;

    /**
     * Path where all source test files are located.
     *
     * @parameter
     * @required
     */
    protected File sources;

    /**
     * Path under which JMX files are stored.
     *
     * @parameter
     * @required
     */
    protected File jmeterHome;

    /**
     * Path under which JMX files are stored.
     *
     * @parameter
     * @required
     */
    protected File resultsDir;

    /**
     * Suppress JMeter output.
     *
     * @parameter default-value="false"
     */
    protected boolean showOutput;

    /**
     * JMeter properties set for the test run.
     * .
     * @parameter
     */
    protected Map<String, String> propertiesJMeter;

    /**
     * (Java) System properties set for the test run.
     *
     * @parameter
     */
    protected Map<String, String> propertiesSystem;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            ExecutionConfig config = new ExecutionConfig(this.sources, this.jmeterHome,
                this.resultsDir, this.propertiesSystem, this.propertiesJMeter, this.showOutput);

            initEnvironment(config);

            LOG.info("Running system tests..");
            ExecutionResult result = getRunner(config).execute();

            LOG.info("Processing tests results...");
            getResultProcessor().process(result, config);

            LOG.info("Building report...");
            getReportBuilder().buildReport(result, config);

            LOG.info("Verify results...");
            verify(result);
        } catch (Throwable e) {
            LOG.error("Error during build:", e);
            throw new MojoExecutionException("Error during system test execution.", e);
        }
    }

    private void verify(ExecutionResult result) throws TestExecutionException {
        if (!result.isSuccess()) {

            throw new TestExecutionException("Build failure. Some System tests failed. See report for detailed info");
        }
    }

    private SystemTestRunner getRunner(ExecutionConfig config) throws TestExecutionException {
        return new JmeterTestRunner(config, new DefaultSystemTestManager());
    }

    private ResultProcessor getResultProcessor() throws TestExecutionException {
        return new JMeterResultProcessor();
    }

    private ReportBuilder getReportBuilder() throws TestExecutionException {
        return new DefaultReportBuilder();
    }

    private void initEnvironment(ExecutionConfig config) throws MojoExecutionException {
        if (!config.getResultsPath().mkdirs()) {
            throw new MojoExecutionException(
                String.format("Unable to create JMeter results directory %s",
                    config.getResultsPath().getAbsolutePath()));
        }
    }
}
