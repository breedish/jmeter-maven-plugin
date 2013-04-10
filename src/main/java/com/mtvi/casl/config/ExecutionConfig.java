package com.mtvi.casl.config;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * ExecutionConfig.
 */
public class ExecutionConfig {

    /**
     * Tests source path.
     */
    private final File sourcePath;

    /**
     * JMeter home.
     */
    private final File executorHome;

    /**
     * Results folder path.
     */
    private final File resultsPath;

    /**
     * Custom system properties for plugin.
     */
    private final Map<String, String> systemProperties;

    /**
     * Custom JMeter properties.
     */
    private final Map<String, String> jmeterProperties;

    /**
     * Flag to show JMeter output by default.
     */
    private boolean showOutput;

    /**
     * Constructor.
     *
     * @param sourcePath - source path.
     * @param executorHome - executor home.
     * @param resultsPath - resultsPath.
     * @param systemProperties - systemProperties.
     * @param jmeterProperties - JMeter properties.
     * @param showOutput - show output flag.
     */
    public ExecutionConfig(File sourcePath, File executorHome, File resultsPath,
        Map<String, String> systemProperties, Map<String, String> jmeterProperties, boolean showOutput) {
        if (sourcePath == null || executorHome == null || resultsPath == null) {
            throw new IllegalArgumentException(
                    String.format("Null or blank value has been passed in as required argument, "
                        + "[sources=%s, jmeterHome=%s, resultsPath=%s]", sourcePath, executorHome, resultsPath));
        }
        this.sourcePath = sourcePath;
        this.executorHome = executorHome;
        this.resultsPath = resultsPath;
        this.systemProperties = systemProperties != null ? systemProperties : new HashMap<String, String>();
        this.jmeterProperties = jmeterProperties != null ? jmeterProperties : new HashMap<String, String>();
        this.showOutput = showOutput;
    }

    public File getSourcePath() {
        return sourcePath;
    }

    public File getExecutorHome() {
        return executorHome;
    }

    public File getResultsPath() {
        return resultsPath;
    }

    public Map<String, String> getSystemProperties() {
        return Collections.unmodifiableMap(systemProperties);
    }

    public Map<String, String> getJmeterProperties() {
        return Collections.unmodifiableMap(jmeterProperties);
    }

    public boolean isShowOutput() {
        return showOutput;
    }
}
