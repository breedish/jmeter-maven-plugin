package com.breedish.test.automator.api;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * SimpleTestGroupManager.
 */
public class SimpleTestGroupManager extends BaseTestGroupManager {

    private static final String DEFAULT_ALL_IN_ONE_GROUP_NAME = "all-in-one";

    public SimpleTestGroupManager(Configuration configuration, AutomationTestsResolver testsResolver) {
        super(configuration, testsResolver);
    }

    @Override
    public Set<TestGroup> getDefinedGroups() {
        return Sets.newHashSet(new TestGroup(DEFAULT_ALL_IN_ONE_GROUP_NAME,
            getTestsResolver().loadTestDefinitions()));
    }
}
