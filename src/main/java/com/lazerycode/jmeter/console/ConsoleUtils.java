package com.lazerycode.jmeter.console;

import org.apache.maven.plugin.logging.Log;

import java.util.List;

/**
 * Console Utils class.
 */
public final class ConsoleUtils {

    public static void showTestsInfo(Log log, String message, List<String> tests) {
        log.info(" ");
        log.info("-------------------------------------------------------");
        log.info(String.format(" \t %s", message));
        log.info("-------------------------------------------------------");
        log.info(" ");

        int testIndex = 1;
        for (String test : tests) {
            log.info(String.format("\t%d .\t %s", testIndex, test));
            testIndex++;
        }
    }

    public static void log(Log log, String message) {
        log.info(message);
    }

}
