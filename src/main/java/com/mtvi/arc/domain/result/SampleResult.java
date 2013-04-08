package com.mtvi.arc.domain.result;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.util.Set;

/**
 * SampleResult class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "httpSample")
public class SampleResult {

    @XmlAttribute(name = "s")
    private boolean success;

    @XmlAttribute(name = "lb")
    private String label;

    @XmlAttribute(name = "hn")
    private String hostName;

    @XmlElement(name = "java.net.URL")
    private String url;

    @XmlElement(name = "queryString")
    private String queryString;

    @XmlElement(name = "method")
    private String method;

    @XmlElement(name = "")
    private String requestHeaders;

    @XmlAttribute(name = "rc")
    private String responseCode;

    @XmlAttribute(name = "rm")
    private String responseMessage;

    @XmlElement(name = "responseHeader")
    private String responseHeaders;

    @XmlElement(name = "responseData")
    private String responseData;

    @XmlElement(name = "responseFile")
    private String responseFile;

    @XmlAttribute(name = "by")
    private int bytesSize;

    @XmlElement(name = "assertionResult")
    private Set<AssertionResult> assertions;

    public SampleResult() {
    }

    public SampleResult(boolean success, String hostName, String url, String queryString,
        String method, String requestHeaders, String responseCode,
        String responseMessage, String responseHeaders, String responseData,
        String responseFile, int bytesSize, Set<AssertionResult> assertions) {

        this.success = success;
        this.hostName = hostName;
        this.url = url;
        this.queryString = queryString;
        this.method = method;
        this.requestHeaders = requestHeaders;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.responseHeaders = responseHeaders;
        this.responseData = responseData;
        this.responseFile = responseFile;
        this.bytesSize = bytesSize;
        this.assertions = assertions;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<AssertionResult> getAssertions() {
        return assertions;
    }

    public void setAssertions(Set<AssertionResult> assertions) {
        this.assertions = assertions;
    }

    public int getBytesSize() {
        return bytesSize;
    }

    public void setBytesSize(int bytesSize) {
        this.bytesSize = bytesSize;
    }

    public String getResponseFile() {
        return responseFile;
    }

    public void setResponseFile(String responseFile) {
        this.responseFile = responseFile;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public String getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(String responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("success", this.success).append("label", this.label).toString();
    }

}
