package com.breedish.test.automator.api;

import java.util.Set;

/**
 * TestGroupManager.
 */
public interface TestGroupManager {

    Set<TestGroup> getDefinedGroups();

    TestGroup findGroup(String name);

    Set<TestGroup> findActiveGroups();

}
