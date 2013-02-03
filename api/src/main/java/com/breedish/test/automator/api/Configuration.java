package com.breedish.test.automator.api;

/**
 * Configuration.
 */
public class Configuration {

    private final String testsRootPath;

    public Configuration(String testsRootPath) {
        if (testsRootPath == null) {
            throw new IllegalArgumentException(
                String.format("Null or blank value has been passed in as required argument"
                    + " [\"testsRootPath\"=%s].", testsRootPath));
        }

        this.testsRootPath = testsRootPath;
    }

    public String getTestsRootPath() {
        return testsRootPath;
    }
}

