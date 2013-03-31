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
import java.util.Map;

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
    protected File customPropertiesFile;

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

    /**
     * Absolute path to JMeter custom (test dependent) properties file.
     * .
     *
     * @parameter
     */
    protected Map<String, String> propertiesJMeter;

    /**
     * JMeter Properties that are merged with precedence into default JMeter file in saveservice.properties
     *
     * @parameter
     */
    protected Map<String, String> propertiesSaveService;

    /**
     * JMeter Properties that are merged with precedence into default JMeter file in upgrade.properties
     *
     * @parameter
     * @description JMeter Properties that are merged with precedence into default JMeter file in 'upgrade.properties'.
     */
    protected Map<String, String> propertiesUpgrade;

    /**
     * JMeter Properties that are merged with precedence into default JMeter file in user.properties
     * user.properties takes precedence over jmeter.properties
     *
     * @parameter
     * @description JMeter Properties that are merged with precedence into default JMeter file in 'user.properties'
     * user.properties takes precedence over 'jmeter.properties'.
     */
    protected Map<String, String> propertiesUser;

    /**
     * JMeter Global Properties that override those given in jmeterProps. <br>
     * This sets local and remote properties (JMeter's definition of global properties is actually remote properties)
     * and overrides any local/remote properties already set
     *
     * @description JMeter Global Properties that override those given in jmeterProps. <br>
     * This sets local and remote properties (JMeter's definition of global properties is actually remote properties)
     * and overrides any local/remote properties already set.
     *
     */
    protected Map<String, String> propertiesGlobal;

    /**
     * (Java) System properties set for the test run.
     * Properties are merged with precedence into default JMeter file system.properties
     *
     * @parameter Java merged with precedence into default JMeter file system.properties.
     */
    protected Map<String, String> propertiesSystem;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        SystemTestManager manager = new DefaultSystemTestManager();

        ExecutionConfig config = new ExecutionConfig(this.sourcePath, this.executorHome,
            this.executorLogs, this.propertiesSystem, this.propertiesJMeter);

        SystemTestRunner runner = new JmeterTestRunner();

        try {
            runner.execute(manager, config);
        } catch (TestExecutionException e) {
            throw new MojoExecutionException("Error during system test execution.", e);
        }

    }
}
