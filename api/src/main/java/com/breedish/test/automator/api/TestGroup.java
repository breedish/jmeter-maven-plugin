package com.breedish.test.automator.api;

import com.google.common.collect.Sets;
import edu.emory.mathcs.backport.java.util.Collections;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * TestGroup.
 */
public class TestGroup {

    private final String name;

    private final Set<TestDefinition> tests;

    private final boolean active;

    public TestGroup(String name) {
        this(name, Sets.<TestDefinition>newHashSet());
    }

    public TestGroup(String name, Set<TestDefinition> tests) {
        this(name, tests, true);
    }

    public TestGroup(String name, Set<TestDefinition> tests, boolean active) {
        if (StringUtils.isBlank(name) || tests == null) {
            throw new IllegalArgumentException(
                String.format("Null or blank value has been passed in as required argument "
                    + "[\"name\"=%s, \"tests\"=%s].", name, tests));
        }

        this.name = name;
        this.active = active;
        this.tests = tests;
    }

    public String getName() {
        return name;
    }

    public Set<TestDefinition> getTests() {
        return Collections.unmodifiableSet(tests);
    }

    public boolean isActive() {
        return active;
    }
}
