package com.breedish.jmeter.configuration;

/**
 * ExecutionConfiguration.
 */
public final class ExecutionConfiguration {

    private final boolean runConcurrent;

    public ExecutionConfiguration() {
        this(false);
    }

    public ExecutionConfiguration(boolean runConcurrent) {
        this.runConcurrent = runConcurrent;
    }

    public boolean isRunConcurrent() {
        return runConcurrent;
    }
}

