package com.mtvi.casl.report;

import com.mtvi.casl.config.ExecutionConfig;
import com.mtvi.casl.domain.SystemTest;
import com.mtvi.casl.domain.result.SystemTestResult;
import com.mtvi.casl.processors.JMeterResultProcessor;
import com.mtvi.casl.processors.JMeterResultProcessorTest;
import com.mtvi.casl.processors.ResultProcessor;
import com.mtvi.casl.runner.ExecutionResult;
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
