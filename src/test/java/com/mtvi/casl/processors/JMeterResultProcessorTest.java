package com.mtvi.casl.processors;

import com.mtvi.casl.domain.SystemTestDefinition;
import com.mtvi.casl.domain.result.DetailedResult;
import com.mtvi.casl.domain.result.SystemTestResult;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Date;

/**
 * JMeterResultProcessorTest class.
 */
public class JMeterResultProcessorTest {

    @Test
    public void testJMeterTestConversion() throws Exception {
        File testResult = new File(JMeterResultProcessorTest.class.getResource("/CRX.jmx.jtl").toURI());

        SystemTestResult result = new SystemTestResult(
            new SystemTestDefinition("test", new File("test.jmx")), new Date(), new Date(), testResult);
        DetailedResult detailedResult = new JMeterResultProcessor.JAXBJMeterTestResultParser().parse(result);

        Assert.assertEquals(detailedResult.getSamples().size(), 87);
        System.out.println(detailedResult);
    }

    @Ignore
    @Test
    public void testJMeterTestWithParentSamokeConversion() throws Exception {
        File testResult = new File(JMeterResultProcessorTest.class.getResource("/CRX_with_parent_sample.jmx.jtl").toURI());

        SystemTestResult result = new SystemTestResult(
                new SystemTestDefinition("test", new File("test.jmx")), new Date(), new Date(), testResult);
        DetailedResult detailedResult = new JMeterResultProcessor.JAXBJMeterTestResultParser().parse(result);

        Assert.assertEquals(detailedResult.getSamples().size(), 10);
        System.out.println(detailedResult);
    }

}
