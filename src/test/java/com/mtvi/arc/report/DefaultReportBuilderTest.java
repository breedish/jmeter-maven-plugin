package com.mtvi.arc.report;

import com.mtvi.arc.config.ExecutionConfig;
import com.mtvi.arc.domain.SystemTest;
import com.mtvi.arc.domain.result.SystemTestResult;
import com.mtvi.arc.processors.JMeterResultProcessor;
import com.mtvi.arc.processors.JMeterResultProcessorTest;
import com.mtvi.arc.processors.ResultProcessor;
import com.mtvi.arc.runner.ExecutionResult;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.Date;

/**
 * DefaultReportBuilderTest.
 */
public class DefaultReportBuilderTest {

    @Test
    public void testReportGenerated() throws Exception {
        ReportBuilder builder = new DefaultReportBuilder();

        File testResult = new File(JMeterResultProcessorTest.class.getResource("/CRX.jmx.jtl").toURI());

        SystemTestResult result = new SystemTestResult(
                new SystemTest("test", new File("test.jmx")), new Date(), new Date(), testResult);

        ResultProcessor resultProcessor = new JMeterResultProcessor();

        ExecutionConfig config = new ExecutionConfig(new File("test.jmx"), new File("."),  new File("."),
                Collections.<String, String>emptyMap(), Collections.<String, String>emptyMap(), true);

        ExecutionResult executionResult = new ExecutionResult();
        executionResult.bindResult(result);

        resultProcessor.process(executionResult, config);

        builder.buildReport(executionResult, config);
    }
}
