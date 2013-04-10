package com.mtvi.casl.domain;

/**
 * TestExecutionException.
 */
public class TestExecutionException extends Exception {

    /**
     * Constructor.
     *
     * @param message - error message.
     * @param cause - root cause.
     */
    public TestExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     *
     * @param message - error message.
     */
    public TestExecutionException(String message) {
        super(message);
    }
}
