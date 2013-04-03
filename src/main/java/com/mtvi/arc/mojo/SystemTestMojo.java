package com.mtvi.arc.mojo;

import com.mtvi.arc.config.ExecutionConfig;
import com.mtvi.arc.domain.DefaultSystemTestManager;
import com.mtvi.arc.domain.SystemTestManager;
import com.mtvi.arc.runner.jmeter.JmeterTestRunner;
import com.mtvi.arc.runner.SystemTestRunner;
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
     * @parameter expression="${jmeter.testfiles.basedir}"
     * default-value="${basedir}/target/jmeter-it"
     */
    protected File sourcePath;

    /**
     * Path under which JMX files are stored.
     *
     * @parameter expression="${jmeter.testfiles.basedir}"
     * default-value="${basedir}/target/jmeter"
     */
    protected File executorHome;

    /**
     * Path under which JMX files are stored.
     *
     * @parameter expression="${jmeter.testfiles.basedir}"
     * default-value="${basedir}/target/jmeter-results"
     */
    protected File executorLogs;

    /**
     * Suppress JMeter output.
     *
     * @parameter default-value="false"
     */
    protected boolean showOutput;

    /**
     * JMeter properties set for the test run.
     * .
     *
     * @parameter
     */
    protected Map<String, String> propertiesJMeter;

    /**
     * (Java) System properties set for the test run.
     *
     * @parameter Java merged with precedence into default JMeter file system.properties.
     */
    protected Map<String, String> propertiesSystem;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        SystemTestManager manager = new DefaultSystemTestManager();

        ExecutionConfig config = new ExecutionConfig(this.sourcePath, this.executorHome,
            this.executorLogs, this.propertiesSystem, this.propertiesJMeter, this.showOutput);

        SystemTestRunner runner = new JmeterTestRunner(config, manager);
        try {
            runner.execute();
        } catch (Throwable e) {
            LOG.error("Error during build", e);
            throw new MojoExecutionException("Error during system test execution.", e);
        }

    }
}
