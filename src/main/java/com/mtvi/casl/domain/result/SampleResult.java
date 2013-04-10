package com.mtvi.casl.domain.result;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SampleResult class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "httpSample")
public class SampleResult {

    /**
     * JSON Response Content Type Pattern.
     */
    private static final Pattern RESPONSE_JSON_TYPE_PATTERN = Pattern.compile("(.*)?Content-Type:(.*)?/json(.*)?", Pattern.MULTILINE);

    /**
     * Flag if sample's call was successful.
     */
    @XmlAttribute(name = "s")
    private boolean success;

    /**
     * Label for sample.
     */
    @XmlAttribute(name = "lb")
    private String label;

    /**
     * Host Name.
     */
    @XmlAttribute(name = "hn")
    private String hostName;

    /**
     * Exact URL that sample is executing.
     */
    @XmlElement(name = "java.net.URL")
    private String url;

    /**
     * Query string.
     */
    @XmlElement(name = "queryString")
    private String queryString;

    /**
     * HTTP Execution Method.
     */
    @XmlElement(name = "method")
    private String method;

    /**
     * Request Headers provided by JMeter.
     */
    @XmlElement(name = "requestHeader")
    private String requestHeaders;

    /**
     * Response code.
     */
    @XmlAttribute(name = "rc")
    private String responseCode;

    /**
     * Response message.
     */
    @XmlAttribute(name = "rm")
    private String responseMessage;

    /**
     * Response Headers.
     */
    @XmlElement(name = "responseHeader")
    private String responseHeaders;

    /**
     * Response Data.
     */
    @XmlElement(name = "responseData")
    private String responseData;

    /**
     * Response File.
     */
    @XmlElement(name = "responseFile")
    private String responseFile;

    /**
     * Size of response.
     */
    @XmlAttribute(name = "by")
    private int bytesSize;

    /**
     * List of assertions.
     */
    @XmlElement(name = "assertionResult")
    private Set<AssertionResult> assertions;

    /**
     * Default constructor..
     */
    public SampleResult() {
        //Do nothing.
    }

    /**
     * Constructor.
     *
     * @param success - success flag.
     * @param hostName - host name.
     * @param url - url.
     * @param queryString - query string.
     * @param method - method.
     * @param requestHeaders - request headers.
     * @param responseCode - response code.
     * @param responseMessage - response message.
     * @param responseHeaders - response headers.
     * @param responseData - response data.
     * @param responseFile - response file.
     * @param bytesSize - bytes size.
     * @param assertions - assertions.
     */
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

    /**
     * @return true if response of JSON content type.
     */
    public boolean isJSONResponse() {
        if (this.responseHeaders != null) {
            Matcher matcher = RESPONSE_JSON_TYPE_PATTERN.matcher(this.responseHeaders);
            return matcher.find();
        }

        return false;
    }

}
