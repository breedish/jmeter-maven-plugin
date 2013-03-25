package com.mtvi.arc.domain;

import com.google.common.collect.Sets;
import com.mtvi.arc.config.ExecutionConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

/**
 * DefaultSystemTestManager.
 */
public class DefaultSystemTestManager implements SystemTestManager {

    private final SystemTestScanner systemTestScanner;

    public DefaultSystemTestManager() {
        this.systemTestScanner = new ByExtensionSystemTestScanner();
    }

    @Override
    public Set<SystemTest> findSystemTests(ExecutionConfig config) throws TestExecutionException {
        Set<SystemTest> tests = Sets.newTreeSet(new Comparator<SystemTest>() {
            @Override
            public int compare(SystemTest test1, SystemTest test2) {
                return test1.getName().compareTo(test2.getName().toString());
            }
        });

        for (File testSrc : this.systemTestScanner.scan(config.getSourcePath().getAbsolutePath())) {
            tests.add(new SystemTest(testSrc.getName(), testSrc));
        }

        return tests;
    }

    interface SystemTestScanner {

        Collection<File> scan(String srcPath) throws TestExecutionException;

    }

    class ByExtensionSystemTestScanner implements SystemTestScanner {

        private static final String JMETER_EXTENSION = "jmx";

        @Override
        public Collection<File> scan(String srcPath) throws TestExecutionException {
            File source = new File(srcPath);
            if (!source.exists()) {
                throw new TestExecutionException(String.format("Jmeter source path doesn't exist: [path=%s]", srcPath));
            }


            return FileUtils.listFiles(source, new String[]{JMETER_EXTENSION}, true);
        }

    }



}
