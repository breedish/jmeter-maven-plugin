package com.mtvi.arc.runner;

import com.mtvi.arc.config.ExecutionConfig;
import com.mtvi.arc.domain.SystemTest;
import org.apache.commons.collections.MapUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * JmeterCLConfigConverter.
 */
public class JmeterCLConfigConverter {

    String[] prepareConfig(ExecutionConfig config, SystemTest test) {
        List<String> options = new LinkedList<String>();

        options.add(JmeterCLOption.TESTFILE_OPT.getCLOption());
        options.add(test.getPath().getAbsolutePath());

        options.add(JmeterCLOption.LOGFILE_OPT.getCLOption());
        options.add(new File(config.getLogsHome(), test.getName() + ".jtl").getAbsolutePath());

        options.add(JmeterCLOption.JMLOGFILE_OPT.getCLOption());
        options.add(new File(config.getLogsHome(), test.getName() + ".log").getAbsolutePath());

        options.add(JmeterCLOption.JMETER_HOME_OPT.getCLOption());
        options.add(config.getExecutorHome().getAbsolutePath());

        if (MapUtils.isNotEmpty(config.getSystemProperties())) {
            for (Map.Entry<String, String> entry : config.getSystemProperties().entrySet()) {
                options.add(JmeterCLOption.SYSTEM_PROPERTY.getCLOption() + entry.getKey());
                options.add(entry.getValue());
            }
        }

        if (MapUtils.isNotEmpty(config.getJmeterProperties())) {
            for (Map.Entry<String, String> entry : config.getJmeterProperties().entrySet()) {
                options.add(JmeterCLOption.JMETER_PROPERTY.getCLOption() + entry.getKey());
                options.add(entry.getValue());
            }
        }

        options.add("-Jjmeter.save.saveservice.data_type");
        options.add(Boolean.TRUE.toString());
        options.add("-Jjmeter.save.saveservice.label");
        options.add(Boolean.TRUE.toString());
        options.add("-Jjmeter.save.saveservice.response_code");
        options.add(Boolean.TRUE.toString());
        options.add("-Jjmeter.save.saveservice.response_data");
        options.add(Boolean.TRUE.toString());
        options.add("-Jjmeter.save.saveservice.response_message");
        options.add(Boolean.TRUE.toString());
        options.add("-Jjmeter.save.saveservice.successful");
        options.add(Boolean.TRUE.toString());
        options.add("-Jjmeter.save.saveservice.thread_name");
        options.add(Boolean.TRUE.toString());
        options.add("-Jjmeter.save.saveservice.time");
        options.add(Boolean.TRUE.toString());
        options.add("-Jjmeter.save.saveservice.subresults");
        options.add(Boolean.TRUE.toString());
        options.add("-Jjmeter.save.saveservice.assertions");
        options.add(Boolean.TRUE.toString());
        options.add("-Jjmeter.save.saveservice.latency");
        options.add(Boolean.TRUE.toString());
        options.add("-Jjmeter.save.saveservice.samplerData");
        options.add(Boolean.TRUE.toString());
        options.add("-Jjmeter.save.saveservice.responseHeaders");
        options.add(Boolean.TRUE.toString());
        options.add("-Jjmeter.save.saveservice.requestHeaders");
        options.add(Boolean.TRUE.toString());
        options.add("-Jjmeter.save.saveservice.encoding");
        options.add(Boolean.TRUE.toString());

        options.add(JmeterCLOption.NONGUI_OPT.getCLOption());

        return options.toArray(new String[options.size()]);
    }
}
