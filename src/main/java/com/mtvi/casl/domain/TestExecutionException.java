package com.mtvi.casl.domain;

/**
 * TestExecutionException.
 */
public class TestExecutionException extends Exception {

    public TestExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestExecutionException(String message) {
        super(message);
    }
}
