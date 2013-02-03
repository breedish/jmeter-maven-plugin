package com.breedish.jmeter.execution;

import org.codehaus.plexus.util.StringUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * ExecutionResults.
 */
public class ExecutionResults {

    private final Set<TestExecutionResult> results = new LinkedHashSet<TestExecutionResult>();

    public void saveResult(TestExecutionResult result) {
        if (result == null) {
            throw new IllegalArgumentException(
                    String.format("Null values has been "
                            + "passed in as required argument [result=%s]", result));
        }
        results.add(result);
    }

    public Set<TestExecutionResult> getResults() {
        return Collections.unmodifiableSet(results);
    }

    public static class TestExecutionResult {

        private final String testName;

        private final boolean passed;

        private final long executionTime;

        private final String resultFileName;

        public TestExecutionResult(String testName, String resultFileName,
                boolean passed, long executionTime) {
            if (StringUtils.isEmpty(testName)|| StringUtils.isEmpty(resultFileName)) {
                throw new IllegalArgumentException(
                        String.format("Null or blank values has been "
                                + "passed in as required argument, [testName=%s, resultFileName=%s]",
                                testName, resultFileName));
            }
            this.testName = testName;
            this.resultFileName = resultFileName;
            this.passed = passed;
            this.executionTime = executionTime;
        }

        public String getTestName() {
            return testName;
        }

        public boolean isPassed() {
            return passed;
        }

        public long getExecutionTime() {
            return executionTime;
        }

        public String getResultFileName() {
            return resultFileName;
        }
    }



}
