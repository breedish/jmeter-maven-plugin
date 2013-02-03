package com.breedish.test.automator.api;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * BaseTestGroupManager.
 */
public abstract class BaseTestGroupManager implements TestGroupManager {

    private final AutomationTestsResolver testsResolver;

    private final Configuration configuration;

    protected BaseTestGroupManager(Configuration configuration, AutomationTestsResolver testsResolver) {
        if (testsResolver == null || configuration == null) {
            throw new IllegalArgumentException(
                String.format("Null or blank value has been passed in as required argument"
                    + " [\"testsResolver\"=%s, \"configuration\"=%s].", testsResolver, configuration));
        }

        this.testsResolver = testsResolver;
        this.configuration = configuration;
    }

    @Override
    public TestGroup findGroup(final String name) {
        Set<TestGroup> filteredGroups = Sets.filter(getDefinedGroups(), new Predicate<TestGroup>() {
            @Override
            public boolean apply(TestGroup testGroup) {
                return testGroup.getName().equals(name);
            }
        });

        if (!filteredGroups.isEmpty()) {
            filteredGroups.iterator().next();
        }

        return null;
    }

    @Override
    public Set<TestGroup> findActiveGroups() {
        return Sets.filter(getDefinedGroups(), new Predicate<TestGroup>() {
            @Override
            public boolean apply(TestGroup testGroup) {
                return testGroup.isActive();
            }
        });
    }

    protected Configuration getConfiguration() {
        return configuration;
    }

    protected AutomationTestsResolver getTestsResolver() {
        return testsResolver;
    }
}
