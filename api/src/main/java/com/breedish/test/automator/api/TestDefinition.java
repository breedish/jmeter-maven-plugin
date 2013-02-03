package com.breedish.test.automator.api;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * TestDefinition.
 */
public class TestDefinition {

    private final String name;

    private final File test;

    private final File result;

    public TestDefinition(String name, File test, File result) {
        if (StringUtils.isBlank(name) || test == null || result == null) {
            throw new IllegalArgumentException(String.format("Null or blank value has been passed in as required " +
                    "argument [name=%s, test=%s, result=%s].", name, test, result));
        }
        this.name = name;
        this.test = test;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public File getTest() {
        return test;
    }

    public File getResult() {
        return result;
    }
}
