package com.mtvi.arc.report;

import com.mtvi.arc.config.ExecutionConfig;
import com.mtvi.arc.domain.TestExecutionException;
import com.mtvi.arc.runner.ExecutionResult;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * DefaultReportBuilder.
 */
public class DefaultReportBuilder implements ReportBuilder {

    private static final String REPORT_NAME = "SystemTestReport.html";

    @Override
    public void buildReport(ExecutionResult result, ExecutionConfig config) throws TestExecutionException {
        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(DefaultReportBuilder.class, "/");
        try {
            Template template = cfg.getTemplate("report.ftl");

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("message", "Hello World!");
            data.put("config", config);
            data.put("result", result);

            Writer file = new FileWriter(new File(config.getResultsPath(), REPORT_NAME));
            template.process(data, file);
            file.flush();
            file.close();
        } catch (Exception e) {
            throw new TestExecutionException("I/O error occurred during saving report", e);
        }
    }

}
