package com.breedish.jmeter;

import org.apache.maven.plugin.logging.Log;

/**
 * ExecutionContext.
 */
public class ExecutionContext {

    private final Log logger;

    public ExecutionContext(Log logger) {
        if (logger == null) {
            throw new IllegalArgumentException(
                    String.format("Null or blank value has been "
                            + "passed in as required argument. [logger=%s]", logger));
        }
        this.logger = logger;
    }

    public Log getLogger() {
        return logger;
    }
}
