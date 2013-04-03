package com.mtvi.arc.config;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * ExecutionConfig.
 */
public class ExecutionConfig {

    private final File sourcePath;

    private final File executorHome;

    private final File logsHome;

    private final Map<String, String> systemProperties;

    private final Map<String, String> jmeterProperties;

    private boolean showOutput;

    public ExecutionConfig(File sourcePath, File executorHome, File logsHome,
        Map<String, String> systemProperties, Map<String, String> jmeterProperties, boolean showOutput) {
        if (sourcePath == null || executorHome == null || logsHome == null) {
            throw new IllegalArgumentException(
                    String.format("Null or blank value has been passed in as required argument, "
                        + "[sourcePath=%s, executorHome=%s, logsHome=%s]", sourcePath, executorHome, logsHome));
        }
        this.sourcePath = sourcePath;
        this.executorHome = executorHome;
        this.logsHome = logsHome;
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

    public File getLogsHome() {
        return logsHome;
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
