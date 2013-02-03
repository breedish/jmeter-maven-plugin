package com.breedish.test.automator.api;

import java.util.Set;

/**
 * AutomationTestsResolver.
 */
public interface AutomationTestsResolver {

    Set<TestDefinition> loadTestDefinitions();
}
