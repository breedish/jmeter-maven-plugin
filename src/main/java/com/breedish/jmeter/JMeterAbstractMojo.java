package com.breedish.jmeter;

import com.breedish.jmeter.configuration.JMeterArgumentsArray;
import com.breedish.jmeter.configuration.ProxyConfiguration;
import com.breedish.jmeter.configuration.RemoteConfiguration;
import com.breedish.jmeter.properties.PropertyHandler;
import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * JMeter Maven plugin.
 * This is a base class for the JMeter mojos.
 *
 * @author Tim McCune
 */
@SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal", "JavaDoc"}) // Mojos get their fields set via reflection
public abstract class JMeterAbstractMojo extends AbstractMojo {

    /**
     * Sets the list of include patterns to use in directory scan for JMX files.
     * Relative to testFilesDirectory.
     *
     * @parameter
     */
    protected List<String> testFilesIncluded;

    /**
     * Sets the list of exclude patterns to use in directory scan for Test files.
     * Relative to testFilesDirectory.
     *
     * @parameter
     */
    protected List<String> testFilesExcluded;

    /**
     * Path under which JMX files are stored.
     *
     * @parameter expression="${jmeter.testfiles.basedir}"
     * default-value="${basedir}/src/tests/jmeter"
     */
    protected File testFilesDirectory;

    /**
     * Timestamp the tests results.
     *
     * @parameter default-value="true"
     */
    protected boolean testResultsTimestamp;

    /**
     * Set the SimpleDateFormat appended to the results filename.
     * (This assumes that testResultsTimestamp is set to 'true')
     *
     * @parameter default-value="yyMMdd"
     */
    protected String resultsFileNameDateFormat;

    /**
     * Absolute path to JMeter custom (tests dependent) properties file.
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
     * (Java) System properties set for the tests run.
     * Properties are merged with precedence into default JMeter file system.properties
     *
     * @parameter Java merged with precedence into default JMeter file system.properties.
     */
    protected Map<String, String> propertiesSystem;

    /**
     * Replace the default JMeter properties with any custom properties files supplied.
     * (If set to false any custom properties files will be merged with the default JMeter properties files, custom properties will overwrite default ones)
     *
     * @parameter default-value="true"
     */
    protected boolean propertiesReplacedByCustomFiles;

    /**
     * Absolute path to JMeter custom (tests dependent) properties file.
     *
     * @parameter
     */
    protected File customPropertiesFile;

    /**
     * Sets whether ErrorScanner should ignore failures in JMeter result file.
     * <p/>
     * Failures are for example failed requests
     *
     * @parameter expression="${jmeter.ignore.failure}" default-value=false
     */
    protected boolean ignoreResultFailures;

    //TODO: what is an error?
    /**
     * Sets whether ErrorScanner should ignore errors in JMeter result file.
     *
     * @parameter expression="${jmeter.ignore.error}" default-value=false
     */
    protected boolean ignoreResultErrors;

    /**
     * Value class that wraps all proxy configurations.
     *
     * @parameter default-value="${proxyConfig}"
     */
    protected ProxyConfiguration proxyConfig;

    /**
     * Value class that wraps all remote configurations.
     *
     * @parameter default-value="${remoteConfig}"
     */
    protected RemoteConfiguration remoteConfig;

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
     * Get a list of artifacts used by this plugin
     *
     * @parameter default-value="${plugin.artifacts}"
     * @required
     * @readonly
     */
    protected List<Artifact> pluginArtifacts;

    //---------------------------------------------------

    /**
     * JMeter outputs.
     * @parameter expression="${project.build.directory}/jmeter"
     * @description Place where the JMeter files will be generated.
     */
    protected transient File workDir;

    /**
     * Path to save jmeter tests results.
     *
     * @parameter expression="${project.build.directory}/jmeter/jmeter-results"
     * @description Place where Jmeter Test Results will be stored.
     */
    protected transient File resultsStorePath;

    /**
     * Other directories will be created by this plugin and used by JMeter
     */
    protected File binDir;
    protected File libDir;
    protected File libExtDir;
    protected File logsDir;
    protected File resultsDir;

    /**
     * All property files are stored in this artifact, comes with JMeter library
     */
    protected String jmeterConfigArtifact = "ApacheJMeter_config";

    protected JMeterArgumentsArray testArgs;

    protected PropertyHandler pluginProperties;

    protected void init() {
        // Create jmeter directory structure if needed.
        generateJMeterDirectoryTree();
    }

    /**
     * Generate the directory tree utilised by JMeter.
     */
    protected void generateJMeterDirectoryTree() {
        this.logsDir = new File(this.workDir,"logs");
        this.logsDir.mkdirs();
        this.binDir = new File(this.workDir, "bin");
        this.binDir.mkdirs();
        this.libDir = new File(this.workDir, "lib");

        this.resultsDir = this.resultsStorePath == null ? new File(workDir, "results") : resultsStorePath;
        this.resultsDir.mkdirs();
        this.libExtDir = new File(libDir, "ext");
        this.libExtDir.mkdirs();

        //JMeter expects a <workdir>/lib/junit directory and complains if it can't find it.
        new File(libDir, "junit").mkdirs();
    }

    protected void propertyConfiguration(ExecutionContext context) throws MojoExecutionException {
        this.pluginProperties = new PropertyHandler(this.testFilesDirectory, this.binDir,
                getArtifactNamed(this.jmeterConfigArtifact), this.propertiesReplacedByCustomFiles, context);
        this.pluginProperties.setJMeterProperties(this.propertiesJMeter);
        this.pluginProperties.setJMeterGlobalProperties(this.propertiesGlobal);
        this.pluginProperties.setJMeterSaveServiceProperties(this.propertiesSaveService);
        this.pluginProperties.setJMeterUpgradeProperties(this.propertiesUpgrade);
        this.pluginProperties.setJmeterUserProperties(this.propertiesUser);
        this.pluginProperties.setJMeterSystemProperties(this.propertiesSystem);
        this.pluginProperties.configureJMeterPropertiesFiles();
        this.pluginProperties.setDefaultPluginProperties(this.binDir.getAbsolutePath());
    }

    /**
     * Copy jars/files to correct place in the JMeter directory tree.
     *
     * @throws MojoExecutionException
     */
    protected void populateJMeterDirectoryTree() throws MojoExecutionException {
        for (Artifact artifact : this.pluginArtifacts) {
            try {
                if (artifact.getArtifactId().startsWith("ApacheJMeter_")) {
                    if (artifact.getArtifactId().startsWith("ApacheJMeter_config")) {
                        JarFile configSettings = new JarFile(artifact.getFile());
                        Enumeration<JarEntry> entries = configSettings.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry jarFileEntry = entries.nextElement();
                            // Only interested in files in the /bin directory that are not properties files
                            if (jarFileEntry.getName().startsWith("bin") && !jarFileEntry.getName().endsWith(".properties")) {
                                if (!jarFileEntry.isDirectory()) {
                                    InputStream is = configSettings.getInputStream(jarFileEntry); // get the input stream
                                    OutputStream os = new FileOutputStream(new File(this.workDir.getCanonicalPath() + File.separator + jarFileEntry.getName()));
                                    while (is.available() > 0) {
                                        os.write(is.read());
                                    }
                                    os.close();
                                    is.close();
                                }
                            }
                        }
                        configSettings.close();
                    } else {
                        FileUtils.copyFile(artifact.getFile(), new File(this.libExtDir + File.separator + artifact.getFile().getName()));
                    }
                } else {
                    /**
                     * TODO: exclude jars that maven put in #pluginArtifacts for maven run? (e.g. plexus jars, the plugin artifact itself)
                     * Need more info on above, how do we know which ones to exclude??
                     * Most of the files pulled down by maven are required in /lib to match standard JMeter install
                     */
                    FileUtils.copyFile(artifact.getFile(), new File(this.libDir + File.separator + artifact.getFile().getName()));
                }
            } catch (IOException e) {
                throw new MojoExecutionException("Unable to populate the JMeter directory tree: " + e);
            }
        }
        //TODO Check if we really need to do this
        //empty classpath, JMeter will automatically assemble and add all JARs in #libDir and #libExtDir and add them to the classpath. Otherwise all jars will be in the classpath twice.
        System.setProperty("java.class.path", "");
    }

    /**
     * Search the list of plugin artifacts for an artifact with a specific name
     *
     * @param artifactName
     * @return
     * @throws MojoExecutionException
     */
    protected Artifact getArtifactNamed(String artifactName) throws MojoExecutionException {
        for (Artifact artifact : this.pluginArtifacts) {
            if (artifact.getArtifactId().equals(artifactName)) {
                return artifact;
            }
        }
        throw new MojoExecutionException("Unable to find artifact '" + artifactName + "'!");
    }


    /**
     * Generate the initial JMeter Arguments array that is used to create the command line that we pass to JMeter.
     *
     * @throws MojoExecutionException
     */
    protected void initialiseJMeterArgumentsArray() throws MojoExecutionException {
        this.testArgs = new JMeterArgumentsArray();
        this.testArgs.setResultsDirectory(resultsDir.getAbsolutePath());
        this.testArgs.setResultsTimestamp(this.testResultsTimestamp);
        this.testArgs.setJMeterHome(this.workDir.getAbsolutePath());
        this.testArgs.setProxyConfig(this.proxyConfig);
        this.testArgs.setACustomPropertiesFile(this.customPropertiesFile);
        try {
            this.testArgs.setResultsFileNameDateFormat(new SimpleDateFormat(this.resultsFileNameDateFormat));
        } catch (Exception ex) {
            getLog().error("'" + this.resultsFileNameDateFormat + "' is an invalid date format.  Defaulting to 'yyMMdd'.");
        }
    }

}
