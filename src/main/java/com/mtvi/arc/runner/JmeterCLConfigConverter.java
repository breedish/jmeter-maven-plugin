package com.mtvi.arc.runner;

import com.mtvi.arc.config.ExecutionConfig;
import com.mtvi.arc.domain.SystemTest;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * JmeterCLConfigConverter.
 */
public class JmeterCLConfigConverter {

    String[] prepareConfig(ExecutionConfig config, SystemTest test) {
        List<String> options = new LinkedList<String>();

        options.add(JmeterCLOption.TESTFILE_OPT.getCLOption());
        options.add(test.getPath().getAbsolutePath());

        options.add(JmeterCLOption.LOGFILE_OPT.getCLOption());
        options.add(config.getLogsHome() + File.pathSeparator + test.getName() + ".jtl");

        options.add(JmeterCLOption.JMETER_HOME_OPT.getCLOption());
        options.add(config.getExecutorHome().getAbsolutePath());

        options.add(JmeterCLOption.NONGUI_OPT.getCLOption());

        return options.toArray(new String[options.size()]);
    }
}
