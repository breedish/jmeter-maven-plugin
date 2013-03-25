package com.mtvi.arc.config;

import java.io.File;

/**
 * ExecutionConfig.
 */
public class ExecutionConfig {

    private final File sourcePath;

    private final File executorHome;

    private final File logsHome;

    public ExecutionConfig(File sourcePath, File executorHome, File logsHome) {
        if (sourcePath == null || executorHome == null || logsHome == null) {
            throw new IllegalArgumentException(
                    String.format("Null or blank value has been passed in as required argument, "
                        + "[sourcePath=%s, executorHome=%s, logsHome=%s]", sourcePath, executorHome, logsHome));
        }
        this.sourcePath = sourcePath;
        this.executorHome = executorHome;
        this.logsHome = logsHome;
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
}
