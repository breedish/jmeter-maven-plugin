package com.mtvi.arc.domain.result;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * SampleGroupResult.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "sample")
public class SampleGroupResult {

    @XmlElement(name = "httpSample")
    private List<SampleResult> samples = new ArrayList<SampleResult>();

    public void setSamples(List<SampleResult> samples) {
        this.samples = samples;
    }

    public List<SampleResult> getSamples() {
        return this.samples;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("samples", this.samples).toString();
    }
}
