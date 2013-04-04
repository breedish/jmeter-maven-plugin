package com.mtvi.arc.domain;

import java.util.Set;

/**
 * SystemTestResultExplanation.
 */
public class SystemTestResultExplanation {

    private String requestHeaders;

    private String responseHeaders;

    private String method;

    private String responseData;

    private String responseFile;

    private String queryString;

    private Set<AssertionResult> assertions;

    private String responseMessage;

    private boolean successResponse;


    private static class AssertionResult {

        private String name;

        private boolean failure;

        private boolean error;
        //TODO Error Message?

    }

}
