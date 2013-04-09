package com.mtvi.casl.runner.jmeter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mtvi.casl.config.ExecutionConfig;
import com.mtvi.casl.domain.SystemTest;
import org.apache.commons.collections.MapUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JmeterCLConfigConverter.
 */
public class JmeterCLConfigConverter {

    private static final Set<String> PREDEFINED_TURNED_ON_OPTIONS = ImmutableSet.of(
        "jmeter.save.saveservice.data_type", "jmeter.save.saveservice.label",
        "jmeter.save.saveservice.response_code", "jmeter.save.saveservice.response_data",
        "jmeter.save.saveservice.response_message", "jmeter.save.saveservice.successful",
        "jmeter.save.saveservice.thread_name", "jmeter.save.saveservice.time",
        "jmeter.save.saveservice.subresults", "jmeter.save.saveservice.assertions",
        "jmeter.save.saveservice.latency", "jmeter.save.saveservice.samplerData",
        "jmeter.save.saveservice.responseHeaders", "jmeter.save.saveservice.requestHeaders",
        "jmeter.save.saveservice.encoding", "jmeter.save.saveservice.assertion_results_failure_message",
        "jmeter.save.saveservice.assertion_results_failure_message", "jmeter.save.saveservice.bytes",
        "jmeter.save.saveservice.url", "jmeter.save.saveservice.filename",
        "jmeter.save.saveservice.hostname", "jmeter.save.saveservice.print_field_names"
    );

    private static final Map<String, String> PREDEFINED_OPTIONS = ImmutableMap.
            of("jmeter.save.saveservice.assertion_results", "all");

    public String[] prepareConfig(ExecutionConfig config, SystemTest test) {
        List<String> options = new LinkedList<String>();
        addPredefinedOptions(options);

        options.add(JmeterCLOption.TESTFILE_OPT.getCLOption());
        options.add(test.getPath().getAbsolutePath());

        options.add(JmeterCLOption.LOGFILE_OPT.getCLOption());
        options.add(new File(config.getResultsPath(), test.getName() + ".jtl").getAbsolutePath());

        options.add(JmeterCLOption.JMLOGFILE_OPT.getCLOption());
        options.add(new File(config.getResultsPath(), test.getName() + ".log").getAbsolutePath());

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

        options.add(JmeterCLOption.NONGUI_OPT.getCLOption());

        return options.toArray(new String[options.size()]);
    }

    private void addPredefinedOptions(List<String> options) {
        for (String option : PREDEFINED_TURNED_ON_OPTIONS) {
            options.add("-J" + option);
            options.add(Boolean.TRUE.toString());
        }

        for (Map.Entry<String, String> entry : PREDEFINED_OPTIONS.entrySet()) {
            options.add("-J" + entry.getKey());
            options.add(entry.getValue());
        }
    }
}
