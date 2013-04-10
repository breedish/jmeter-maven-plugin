package com.mtvi.casl.domain;

import java.io.File;

/**
 * System Test Definition.
 */
public class SystemTestDefinition {

    /**
     * Test name.
     */
    private final String name;

    /**
     * Test file location.
     */
    private final File path;

    /**
     * Constructor.
     *
     * @param name - test name.
     * @param path - test file location.
     */
    public SystemTestDefinition(String name, File path) {
        if (name == null || path == null) {
            throw new IllegalArgumentException(
                    String.format("Null or blank value has been passed in as required argument, [name=%s, path=%s]", name, path));
        }
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public File getPath() {
        return path;
    }

}
