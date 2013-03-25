package com.mtvi.arc.domain;

import java.io.File;

public class SystemTest {

    private final String name;

    private final File path;

    public SystemTest(String name, File path) {
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
