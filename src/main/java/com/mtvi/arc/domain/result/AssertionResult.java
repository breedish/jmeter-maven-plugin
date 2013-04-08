package com.mtvi.arc.domain.result;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * AssertionResult class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "httpSample")
public class AssertionResult {

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "failure")
    private boolean failure;

    @XmlElement(name = "error")
    private boolean error;

    public void setName(String name) {
        this.name = name;
    }

    public void setFailure(boolean failure) {
        this.failure = failure;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getName() {
        return name;
    }

    public boolean isFailure() {
        return failure;
    }

    public boolean isError() {
        return error;
    }
}
