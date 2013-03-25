package com.mtvi.arc.mojo;

import com.mtvi.arc.config.ExecutionConfig;
import com.mtvi.arc.domain.DefaultSystemTestManager;
import com.mtvi.arc.domain.SystemTestManager;
import com.mtvi.arc.domain.TestExecutionException;
import com.mtvi.arc.runner.JmeterTestRunner;
import com.mtvi.arc.runner.SystemTestRunner;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * JMeter Maven plugin.
 *
 * @author zenind
 * @goal jmeter
 * @requiresProject true
 */
public class SystemTestMojo extends AbstractMojo {

    /**
     * Path under which JMX files are stored.
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
     * Absolute path to JMeter custom (test dependent) properties file.
     *
     * @parameter
     */
    protected File customProdpertiesFile;

    /**
     * Suppress JMeter output
     *
     * @parameter default-value="true"
     */
    protected boolean suppressJMeterOutput;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject mavenProject;

    /**
     * JMeter outputs.
     * @parameter expression="${project.build.directory}/jmeter"
     * @description Place where the JMeter files will be generated.
     */
    protected transient File workDir;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        SystemTestManager manager = new DefaultSystemTestManager();

        ExecutionConfig config = new ExecutionConfig(this.sourcePath, this.executorHome, this.executorLogs);

        SystemTestRunner runner = new JmeterTestRunner();

        try {
            runner.execute(manager, config);
            System.out.println(sourcePath);
            System.out.println(executorLogs);
            System.out.println(executorHome);
        } catch (TestExecutionException e) {
            throw new MojoExecutionException("Error during system test execution.", e);
        }

    }
}
